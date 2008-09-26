package com.calclab.emite.core.client.xmpp.session;

import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.suco.client.listener.Listener;
import com.calclab.suco.client.log.Logger;

public class InitialPresence {

    public InitialPresence() {
	Logger.debug("Initial presence created!");
    }

    public InitialPresence(final Session session) {
	session.onStateChanged(new Listener<State>() {
	    public void onEvent(final State state) {
		if (state == State.loggedIn) {
		    session.send(new Presence(session.getCurrentUser()));
		    session.setReady();
		}
	    }
	});
    }

}
