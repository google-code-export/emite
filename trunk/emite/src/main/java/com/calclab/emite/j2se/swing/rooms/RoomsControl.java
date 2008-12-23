package com.calclab.emite.j2se.swing.rooms;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.suco.client.events.Listener;

public class RoomsControl {

    public RoomsControl(final Session session, final RoomsPanel roomsPanel, final RoomManager manager) {
	roomsPanel.setEnabled(false);

	session.onStateChanged(new Listener<State>() {
	    public void onEvent(final State state) {
		final boolean isReady = state == State.ready;
		roomsPanel.setEnabled(isReady);
	    }

	});

	roomsPanel.onOpenRoom(new Listener<String>() {
	    public void onEvent(final String roomURI) {
		manager.openChat(XmppURI.uri(roomURI), null, null);
	    }
	});
    }

}
