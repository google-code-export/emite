package com.calclab.emite.widgets.comenta.client;

import java.util.Map;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.Conversation;
import com.calclab.emite.im.client.chat.Conversation.State;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.suco.client.events.Listener;

public class ComentaController {
    private static final String CONSONANTES = "bcdfghjklmnpqrstwxyz";
    private static final String VOCALES = "aeiuo";
    private final ComentaWidget widget;
    private Conversation room;

    public ComentaController(final Session session, final RoomManager roomManager, final ComentaWidget widget) {
	this.widget = widget;
	widget.setEnabled(false);

	widget.onSetProperties(new Listener<Map<String, String>>() {
	    public void onEvent(final Map<String, String> properties) {
		final XmppURI roomJID = XmppURI.uri(properties.get("room"));
		if (roomJID == null) {
		    widget.showStatus("Room not specified or not valid.", "error");
		    throw new RuntimeException("room property not specified or not valid.");
		}
		final XmppURI roomURI = XmppURI.uri(roomJID.getNode(), roomJID.getHost(), generateNick());
		createRoom(roomManager, roomURI);
	    }
	});

	widget.onMessage(new Listener<String>() {
	    public void onEvent(final String messageBody) {
		room.send(new Message(messageBody));
	    }
	});

	session.onStateChanged(new Listener<Session.State>() {
	    public void onEvent(final Session.State state) {
		widget.showStatus(state.toString(), "");
		switch (state) {
		case ready:
		    widget.showStatus("Entering " + room.getURI().getNode() + "...", "info");
		    break;
		}
	    }
	});

    }

    private void createRoom(final RoomManager roomManager, final XmppURI roomURI) {
	room = roomManager.openChat(roomURI);

	room.onStateChanged(new Listener<Conversation.State>() {
	    public void onEvent(final State state) {
		final boolean isReady = state == State.ready;
		if (isReady) {
		    widget.setEnabled(true);
		    final XmppURI uri = room.getURI();
		    widget.showStatus("You are " + uri.getResource() + " in " + uri.getNode(), "ready");
		} else {
		    widget.setEnabled(false);
		    widget.showStatus("waiting for room...", "info");
		}
	    }
	});

	room.onMessageReceived(new Listener<Message>() {
	    public void onEvent(final Message message) {
		widget.show(message.getFrom().getResource(), message.getBody());
	    }
	});

    }

    private String generateNick() {
	return select(CONSONANTES) + select(VOCALES) + select(VOCALES) + select(CONSONANTES) + select(VOCALES);
    }

    private String select(final String posibilities) {
	final int pos = (int) (Math.random() * posibilities.length());
	return posibilities.substring(pos, pos + 1);
    }
}
