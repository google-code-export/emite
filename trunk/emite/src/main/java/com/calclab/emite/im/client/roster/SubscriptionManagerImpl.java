package com.calclab.emite.im.client.roster;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Type;
import com.calclab.suco.client.listener.Event;
import com.calclab.suco.client.listener.Listener;

public class SubscriptionManagerImpl implements SubscriptionManager {

    private final Session session;
    private final Event<XmppURI> onSubscriptionRequested;
    private final Roster roster;

    public SubscriptionManagerImpl(final Session session, final Roster roster) {
	this.session = session;
	this.roster = roster;
	this.onSubscriptionRequested = new Event<XmppURI>("subscriptionManager:onSubscriptionRequested");

	session.onPresence(new Listener<Presence>() {
	    public void onEvent(final Presence presence) {
		if (presence.getType() == Type.subscribe) {
		    onSubscriptionRequested.fire(presence.getFrom());
		}
	    }
	});

	roster.onItemAdded(new Listener<RosterItem>() {
	    public void onEvent(final RosterItem item) {
		if (item.getSubscriptionState() == SubscriptionState.none && item.getAsk() == null) {
		    requestSubscribe(item.getJID());
		    item.setSubscriptionState(SubscriptionState.nonePendingIn);
		}
	    }
	});
    }

    public void cancelSubscription(final XmppURI jid) {
	session.send(new Presence(Type.unsubscribe, session.getCurrentUser(), jid.getJID()));
    }

    public void onSubscriptionRequested(final Listener<XmppURI> listener) {
	onSubscriptionRequested.add(listener);
    }

    public void requestSubscribe(final XmppURI jid) {
	session.send(new Presence(Type.subscribe, session.getCurrentUser(), jid.getJID()));
    }

    public void unsubscribe(final XmppURI jid) {
	session.send(new Presence(Type.unsubscribe, session.getCurrentUser(), jid.getJID()));
    }

}
