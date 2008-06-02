package com.calclab.emite.client.im.roster;

import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.modular.client.signal.Slot;

public class RosterManagerListenerAdapter {

    public RosterManagerListenerAdapter(final RosterManager manager, final RosterManagerListener listener) {
	manager.onSubscriptionRequested(new Slot<Presence>() {
	    public void onEvent(final Presence presence) {
		listener.onSubscriptionRequest(presence, manager.getSubscriptionMode());
	    }
	});

	manager.onUnsubscribedReceived(new Slot<XmppURI>() {
	    public void onEvent(final XmppURI parameter) {
		listener.onUnsubscribedReceived(parameter);
	    }
	});
    }

}
