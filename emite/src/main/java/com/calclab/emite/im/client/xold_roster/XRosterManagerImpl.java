package com.calclab.emite.im.client.xold_roster;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;

import java.util.ArrayList;
import java.util.List;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Type;
import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.emite.im.client.roster.SubscriptionState;
import com.calclab.suco.client.listener.Event;
import com.calclab.suco.client.listener.Listener;

public class XRosterManagerImpl implements XRosterManager {

    private final XRoster xRoster;

    private SubscriptionMode subscriptionMode;

    private final Event<Presence> onSubscriptionRequested;

    private final Event<XmppURI> onUnsubscribedReceived;

    private final Session session;

    private final Event<XRoster> onRosterReady;

    public XRosterManagerImpl(final Session session, final XRoster xRoster) {
	this.session = session;
	this.xRoster = xRoster;
	this.subscriptionMode = DEF_SUBSCRIPTION_MODE;
	this.onSubscriptionRequested = new Event<Presence>("rosterManager:onSubscriptionRequested");
	this.onUnsubscribedReceived = new Event<XmppURI>("rosterManager:onUnsubscribedReceived");
	this.onRosterReady = new Event<XRoster>("rosterManager:onRosterReady");

	session.onLoggedIn(new Listener<XmppURI>() {
	    public void onEvent(final XmppURI parameter) {
		requestRoster();
	    }
	});

	session.onLoggedOut(new Listener<XmppURI>() {
	    public void onEvent(final XmppURI parameter) {
		xRoster.clear();
	    }
	});

	session.onPresence(new Listener<Presence>() {
	    public void onEvent(final Presence presence) {
		switch (presence.getType()) {
		case subscribe:
		    handleSubscriptionRequest(presence);
		    break;
		case unsubscribed:
		    // Inform to user but not update roster (only iq set update
		    // roster)
		    handleUnsubscribedReceived(presence.getFrom());
		    break;
		case subscribed:
		    // Fine, but do nothing (only iq set update roster)
		    break;
		case available:
		case unavailable:
		    xRoster.changePresence(presence.getFrom(), presence);
		    break;
		}
	    }
	});

    }

    /**
     * subscribed -- The sender has allowed the recipient to receive their
     * presence.
     */
    public void acceptSubscription(final Presence presence) {
	if (presence.getType() == Presence.Type.subscribe) {
	    final XmppURI from = presence.getFrom();
	    if (xRoster.findItemByJID(from.getJID()) == null) {
		final RosterItem item = new RosterItem(from, SubscriptionState.none, from.getNode(), null);
		xRoster.add(item);
	    }
	    final Presence response = new Presence(Presence.Type.subscribed, session.getCurrentUser(), from);
	    session.send(response);
	} else {
	    // throw exception: its a programming error
	    throw new RuntimeException("Trying to accept/deny a non subscription request");
	}
	requestSubscribe(presence.getFrom());

    }

    /**
     * If a user would like to cancel a previously-granted subscription request,
     * it sends a presence stanza of type "unsubscribed".
     */
    public void cancelSubscriptor(final XmppURI to) {
	final Presence unsubscription = new Presence(Presence.Type.unsubscribed, session.getCurrentUser(), to);
	session.send(unsubscription);
    }

    /**
     * unsubscribed -- The subscription request has been denied or a
     * previously-granted subscription has been cancelled.
     */
    public void denySubscription(final Presence presence) {
	if (presence.getType() == Presence.Type.subscribe) {
	    final Presence response = new Presence(Presence.Type.unsubscribed, session.getCurrentUser(), presence
		    .getFrom());
	    session.send(response);
	} else {
	    // throw exception: its a programming error
	    throw new RuntimeException("Trying to accept/deny a non subscription request");
	}
    }

    public SubscriptionMode getSubscriptionMode() {
	return subscriptionMode;
    }

    public void onRosterReady(final Listener<XRoster> listener) {
	this.onRosterReady.add(listener);
    }

    public void onSubscriptionRequested(final Listener<Presence> listener) {
	onSubscriptionRequested.add(listener);
    }

    public void onUnsubscribedReceived(final Listener<XmppURI> listener) {
	onUnsubscribedReceived.add(listener);
    }

    public void requestAddItem(final XmppURI jid, final String name, final String group) {

	final IQ iq = new IQ(IQ.Type.set, session.getCurrentUser(), null);
	final IPacket item = iq.addQuery("jabber:iq:roster").addChild("item", null).With("jid", jid.toString()).With(
		"name", name);

	if (group != null) {
	    item.addChild("group", null).setText(group);
	}

	xRoster.add(new RosterItem(jid, SubscriptionState.none, name, null));
	session.sendIQ("roster", iq, new Listener<IPacket>() {
	    public void onEvent(final IPacket received) {
		if (IQ.isSuccess(received)) {
		    final Presence presenceRequest = new Presence(Type.subscribe, null, jid);
		    session.send(presenceRequest);
		} else {
		    xRoster.removeItem(jid);
		}
	    }
	});
    }

    public void requestRemoveItem(final XmppURI jid) {
	final IQ iq = new IQ(IQ.Type.set);
	iq.addQuery("jabber:iq:roster").addChild("item", null).With("jid", jid.toString()).With("subscription",
		"remove");
	session.sendIQ("roster", iq, new Listener<IPacket>() {
	    public void onEvent(final IPacket iq) {
		if (IQ.isSuccess(iq)) {
		    xRoster.removeItem(jid);
		}
	    }
	});
    }

    public void requestSubscribe(final XmppURI to) {
	final Presence unsubscribeRequest = new Presence(Presence.Type.subscribe, session.getCurrentUser(), to.getJID());
	session.send(unsubscribeRequest);
    }

    public void requestUnsubscribe(final XmppURI to) {
	final Presence unsubscribeRequest = new Presence(Presence.Type.unsubscribe, session.getCurrentUser(), to);
	session.send(unsubscribeRequest);
    }

    public void setSubscriptionMode(final SubscriptionMode subscriptionMode) {
	this.subscriptionMode = subscriptionMode;
    }

    private RosterItem convert(final IPacket item) {
	final String jid = item.getAttribute("jid");
	final XmppURI uri = uri(jid);
	final SubscriptionState subscriptionState = SubscriptionState.valueOf(item.getAttribute("subscription"));
	return new RosterItem(uri, subscriptionState, item.getAttribute("name"), null);
    }

    private List<? extends IPacket> getItems(final IPacket iPacket) {
	final List<? extends IPacket> items = iPacket.getFirstChild("query").getChildren();
	return items;
    }

    private void handleSubscriptionRequest(final Presence presence) {
	switch (subscriptionMode) {
	case autoAcceptAll:
	    acceptSubscription(presence);
	    break;
	case autoRejectAll:
	    denySubscription(presence);
	    break;
	}
	onSubscriptionRequested.fire(presence);
    }

    private void handleUnsubscribedReceived(final XmppURI userUnsubscribed) {
	onUnsubscribedReceived.fire(userUnsubscribed);
    }

    private void requestRoster() {
	session.sendIQ("roster", new IQ(IQ.Type.get).WithQuery("jabber:iq:roster"), new Listener<IPacket>() {
	    public void onEvent(final IPacket received) {
		setRosterItems(xRoster, received);
	    }

	});
    }

    private void setRosterItems(final XRoster xRoster, final IPacket received) {
	final ArrayList<RosterItem> items = new ArrayList<RosterItem>();
	for (final IPacket item : getItems(received)) {
	    items.add(convert(item));
	}
	xRoster.setItems(items);
	onRosterReady.fire(xRoster);
    }

}