/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.client.im.roster;

import static com.calclab.emite.client.core.dispatcher.matcher.Matchers.when;
import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;

import java.util.ArrayList;
import java.util.List;

import com.calclab.emite.client.components.Installable;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.Event;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.im.roster.RosterItem.Subscription;
import com.calclab.emite.client.xmpp.session.SessionComponent;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.Presence.Type;

public class RosterManager extends SessionComponent implements Installable {
    public static class Events {
	public static final Event ready = new Event("roster:on:ready");
    }

    public static enum SubscriptionMode {
	autoAcceptAll, autoRejectAll, manual
    }

    public static final SubscriptionMode DEF_SUBSCRIPTION_MODE = SubscriptionMode.manual;

    private final Roster roster;

    private SubscriptionMode subscriptionMode;
    private final ArrayList<RosterManagerListener> listeners;

    public RosterManager(final Emite emite, final Roster roster) {
	super(emite);
	this.roster = roster;
	this.subscriptionMode = DEF_SUBSCRIPTION_MODE;
	this.listeners = new ArrayList<RosterManagerListener>();
    }

    /**
     * subscribed -- The sender has allowed the recipient to receive their
     * presence.
     */
    public void acceptSubscription(final Presence presence) {
	if (presence.getType() == Presence.Type.subscribe) {
	    final XmppURI from = presence.getFromURI();
	    final RosterItem item = new RosterItem(from, Subscription.none, from.getNode());
	    roster.add(item);
	    final Presence response = new Presence(Presence.Type.subscribed, userURI, from);
	    emite.send(response);
	} else {
	    // throw exception: its a programming error
	    throw new RuntimeException("Trying to accept/deny a non subscription request");
	}
	requestSubscribe(presence.getFromURI());
    }

    public void addListener(final RosterManagerListener listener) {
	listeners.add(listener);
    }

    /**
     * If a user would like to cancel a previously-granted subscription request,
     * it sends a presence stanza of type "unsubscribed".
     */
    public void cancelSubscriptor(final XmppURI to) {
	final Presence unsubscription = new Presence(Presence.Type.unsubscribed, userURI, to);
	emite.send(unsubscription);
    }

    /**
     * unsubscribed -- The subscription request has been denied or a
     * previously-granted subscription has been cancelled.
     */
    public void denySubscription(final Presence presence) {
	if (presence.getType() == Presence.Type.subscribe) {
	    final Presence response = new Presence(Presence.Type.unsubscribed, userURI, presence.getFromURI());
	    emite.send(response);
	} else {
	    // throw exception: its a programming error
	    throw new RuntimeException("Trying to accept/deny a non subscription request");
	}
    }

    public SubscriptionMode getSubscriptionMode() {
	return subscriptionMode;
    }

    @Override
    public void install() {
	super.install();

	emite.subscribe(when(new IQ(IQ.Type.set).WithQuery("jabber:iq:roster", null).With("xmlns", null)),
		new PacketListener() {
		    public void handle(final IPacket received) {
			emite.send(new IQ(IQ.Type.result).With("id", received.getAttribute("id")));
			final IPacket item = received.getFirstChild("query").getFirstChild("item");
			final String jid = item.getAttribute("jid");
			roster.changeSubscription(uri(jid), item.getAttribute("subscription"));
		    }
		});

	emite.subscribe(when("presence"), new PacketListener() {
	    public void handle(final IPacket received) {
		final Presence presence = new Presence(received);
		switch (presence.getType()) {
		case subscribe:
		    handleSubscriptionRequest(presence);
		    break;
		case subscribed:
		    fireSubscribedReceived(presence);
		    break;
		case unsubscribed:
		    fireUnsubscribedReceived(presence);
		    break;
		case available:
		case unavailable:
		    roster.changePresence(presence.getFromURI(), presence);
		    break;
		}
	    }
	});
    }

    @Override
    public void loggedIn(final XmppURI uri) {
	super.loggedIn(uri);
	requestRoster();
    }

    /**
     * 7.4. Adding a Roster Item
     * 
     * At any time, a user MAY add an item to his or her roster.
     * 
     * @param the
     *                JID of the user you want to add
     * @param name
     * @param group
     * @see http://www.xmpp.org/rfcs/rfc3921.html#roster
     */
    public void requestAddItem(final XmppURI jid, final String name, final String group) {
	final IPacket item = new Packet("item").With("jid", jid.toString()).With("name", name);
	if (group != null) {
	    item.addChild(new Packet("group").WithText(group));
	}

	roster.add(new RosterItem(jid, Subscription.none, name));
	final IPacket iq = new IQ(IQ.Type.set, userURI, null).WithQuery("jabber:iq:roster", item);
	emite.sendIQ("roster", iq, new PacketListener() {
	    public void handle(final IPacket received) {
		if (IQ.isSuccess(received)) {
		    final Presence presenceRequest = new Presence(Type.subscribe, null, jid);
		    emite.send(presenceRequest);
		} else {
		    roster.removeItem(jid);
		}
	    }
	});
    }

    /**
     * 7.4. Adding a Roster Item
     * 
     * At any time, a user MAY add an item to his or her roster.
     * 
     * @param JID
     * @param name
     * @param group
     * @see http://www.xmpp.org/rfcs/rfc3921.html#roster
     */
    public void requestRemoveItem(final XmppURI jid) {
	final IQ iq = new IQ(IQ.Type.set).WithQuery("jabber:iq:roster", new Packet("item").With("jid", jid.toString())
		.With("subscription", "remove"));
	emite.sendIQ("roster", iq, new PacketListener() {
	    public void handle(final IPacket received) {
		if (IQ.isSuccess(received)) {
		    roster.removeItem(jid);
		}
	    }
	});
    }

    /**
     * A request to subscribe to another entity's presence is made by sending a
     * presence stanza of type "subscribe".
     * 
     */
    public void requestSubscribe(final XmppURI to) {
	final Presence unsubscribeRequest = new Presence(Presence.Type.subscribe, userURI, to);
	emite.send(unsubscribeRequest);
    }

    /**
     * If a user would like to unsubscribe from the presence of another entity,
     * it sends a presence stanza of type "unsubscribe".
     */
    public void requestUnsubscribe(final XmppURI to) {
	final Presence unsubscribeRequest = new Presence(Presence.Type.unsubscribe, userURI, to);
	emite.send(unsubscribeRequest);
    }

    public void setSubscriptionMode(final SubscriptionMode subscriptionMode) {
	this.subscriptionMode = subscriptionMode;
    }

    private RosterItem convert(final IPacket item) {
	final String jid = item.getAttribute("jid");
	final XmppURI uri = uri(jid);
	final Subscription subscription = RosterItem.Subscription.valueOf(item.getAttribute("subscription"));
	return new RosterItem(uri, subscription, item.getAttribute("name"));
    }

    private void fireSubscribedReceived(final Presence presence) {
	for (final RosterManagerListener listener : listeners) {
	    listener.onSubscribedReceived(presence, subscriptionMode);
	}
    }

    private void fireSubscriptionRequest(final Presence presence) {
	for (final RosterManagerListener listener : listeners) {
	    listener.onSubscriptionRequest(presence, subscriptionMode);
	}
    }

    private void fireUnsubscribedReceived(final Presence presence) {
	for (final RosterManagerListener listener : listeners) {
	    listener.onUnsubscribedReceived(presence, subscriptionMode);
	}
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
	fireSubscriptionRequest(presence);
    }

    private void requestRoster() {
	emite.sendIQ("roster", new IQ(IQ.Type.get).WithQuery("jabber:iq:roster", null), new PacketListener() {
	    public void handle(final IPacket received) {
		setRosterItems(roster, received);
		emite.publish(RosterManager.Events.ready);
	    }

	});
    }

    private void setRosterItems(final Roster roster, final IPacket received) {
	final ArrayList<RosterItem> items = new ArrayList<RosterItem>();
	for (final IPacket item : getItems(received)) {
	    items.add(convert(item));
	}
	roster.setItems(items);
    }
}
