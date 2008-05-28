package com.calclab.emite.client.im.roster;

import java.util.ArrayList;

import com.calclab.emite.client.im.roster.RosterManager.SubscriptionMode;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public class RosterManagerListenerCollection extends ArrayList<RosterManagerListener> implements RosterManagerListener {
    private static final long serialVersionUID = 1L;

    public void onSubscriptionRequest(final Presence presence, final SubscriptionMode currentMode) {
	for (final RosterManagerListener listener : this) {
	    listener.onSubscriptionRequest(presence, currentMode);
	}
    }

    public void onUnsubscribedReceived(final XmppURI userUnsubscribed) {
	for (final RosterManagerListener listener : this) {
	    listener.onUnsubscribedReceived(userUnsubscribed);
	}
    }

}
