package com.calclab.emite.j2se.swing;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.suco.client.listener.Listener;

public class ClientControl {
    public ClientControl(final Session session, final ClientPanel clientPanel) {
	clientPanel.showTabs(false);
	session.onStateChanged(new Listener<Session.State>() {
	    public void onEvent(final State state) {
		clientPanel.showTabs(state == State.ready);
	    }
	});
    }

}
