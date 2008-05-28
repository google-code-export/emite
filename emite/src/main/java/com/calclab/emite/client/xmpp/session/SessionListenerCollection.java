package com.calclab.emite.client.xmpp.session;

import java.util.ArrayList;

import com.calclab.emite.client.xmpp.session.Session.State;

class SessionListenerCollection extends ArrayList<SessionListener> implements SessionListener {
    private static final long serialVersionUID = 1L;

    public void onStateChanged(final State old, final State current) {
	for (final SessionListener listener : this) {
	    listener.onStateChanged(old, current);
	}
    }

}
