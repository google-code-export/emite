package com.calclab.emite.im.client.roster;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.MatcherFactory;
import com.calclab.emite.core.client.packet.PacketMatcher;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Type;
import com.calclab.suco.client.listener.Event2;
import com.calclab.suco.client.listener.Listener;
import com.calclab.suco.client.listener.Listener2;

public class SubscriptionManagerImpl implements SubscriptionManager {
    protected static final PacketMatcher FILTER_NICK = MatcherFactory.byNameAndXMLNS("nick",
	    "http://jabber.org/protocol/nick");
    private final Session session;
    private final Event2<XmppURI, String> onSubscriptionRequested;
    private final Roster roster;

    public SubscriptionManagerImpl(final Session session, final Roster roster) {
	this.session = session;
	this.roster = roster;
	this.onSubscriptionRequested = new Event2<XmppURI, String>("subscriptionManager:onSubscriptionRequested");

	session.onPresence(new Listener<Presence>() {
	    public void onEvent(final Presence presence) {
		if (presence.getType() == Type.subscribe) {
		    final IPacket nick = presence.getFirstChild(FILTER_NICK);
		    onSubscriptionRequested.fire(presence.getFrom(), nick.getText());
		}
	    }
	});

	roster.onItemAdded(new Listener<RosterItem>() {
	    public void onEvent(final RosterItem item) {
		if (item.getSubscriptionState() == SubscriptionState.none && item.getAsk() == null) {
		    requestSubscribe(item.getJID());
		    item.setSubscriptionState(SubscriptionState.nonePendingIn);
		} else if (item.getSubscriptionState() == SubscriptionState.from) {
		    approveSubscriptionRequest(item.getJID(), item.getName());
		    item.setSubscriptionState(SubscriptionState.fromPendingOut);
		}
	    }
	});
    }

    public void approveSubscriptionRequest(final XmppURI jid, String nick) {
	nick = nick != null ? nick : jid.getNode();
	final RosterItem item = roster.findByJID(jid);
	if (item == null) {
	    // add the item to the roster
	    roster.addItem(jid, nick);
	    // request a subscription to that entity of the roster
	    requestSubscribe(jid);
	}
	// answer "subscribed" to the subscrition request
	session.send(new Presence(Type.subscribed, session.getCurrentUser(), jid.getJID()));
    }

    public void cancelSubscription(final XmppURI jid) {
	session.send(new Presence(Type.unsubscribe, session.getCurrentUser(), jid.getJID()));
    }

    public void onSubscriptionRequested(final Listener2<XmppURI, String> listener) {
	onSubscriptionRequested.add(listener);
    }

    public void refuseSubscriptionRequest(final XmppURI jid) {
	session.send(new Presence(Type.unsubscribed, session.getCurrentUser(), jid.getJID()));
    }

    public void requestSubscribe(final XmppURI jid) {
	session.send(new Presence(Type.subscribe, session.getCurrentUser(), jid.getJID()));
    }

    public void unsubscribe(final XmppURI jid) {
	session.send(new Presence(Type.unsubscribe, session.getCurrentUser(), jid.getJID()));
    }

}
