package com.calclab.emite.core.client.xmpp.session;

import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.suco.client.listener.Listener;

/**
 * A simple component that sets the session ready after logged in. This
 * component is removed if InstantMessagingModule used.
 */
public class SessionReady {

    public SessionReady(final Session session) {
	session.onStateChanged(new Listener<State>() {
	    public void onEvent(final State state) {
		session.setReady();
	    }
	});
    }

}
