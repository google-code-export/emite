package com.calclab.emite.core.client.xmpp.session;

import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.suco.client.listener.Event0;
import com.calclab.suco.client.listener.Listener;
import com.calclab.suco.client.listener.Listener0;

public class InitialPresence implements SessionReadyManager {
    private Event0 onSessionReady;

    public InitialPresence() {
	this.onSessionReady = new Event0("initialPresence:sessionReady");
    }

    public InitialPresence(final Session session) {
	session.onStateChanged(new Listener<State>() {
	    public void onEvent(final State state) {
		if (state == State.loggedIn) {
		    session.send(new Presence(session.getCurrentUser()));
		    onSessionReady.fire();
		}
	    }
	});
    }

    public void onSessionReady(final Listener0 listener) {
	onSessionReady.add(listener);
    }

}
