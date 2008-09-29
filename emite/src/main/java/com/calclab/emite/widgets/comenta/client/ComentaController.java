package com.calclab.emite.widgets.comenta.client;

import java.util.Map;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.Conversation;
import com.calclab.emite.im.client.chat.Conversation.State;
import com.calclab.emite.xep.muc.client.Room;
import com.calclab.suco.client.listener.Listener;

public class ComentaController {
    private final ComentaWidget widget;
    private final Session session;
    private Room room;

    public ComentaController(final Session session, final ComentaWidget widget) {
	this.session = session;
	this.widget = widget;
	widget.setEnabled(false);

	widget.onSetProperties(new Listener<Map<String, String>>() {
	    public void onEvent(final Map<String, String> properties) {
		final XmppURI roomURI = XmppURI.uri(properties.get("room"));
		if (roomURI == null) {
		    widget.showStatus("Room not specified or not valid.", "error");
		    throw new RuntimeException("room property not specified or not valid.");
		}
		createRoom(XmppURI.uri(roomURI.getNode(), roomURI.getHost(), generateNick()));
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
		    widget.showStatus("Entering " + room.getOtherURI().getNode() + "...", "info");
		    break;
		}
	    }
	});

    }

    private void createRoom(final XmppURI roomURI) {
	room = new Room(session, roomURI);

	room.onStateChanged(new Listener<Conversation.State>() {
	    public void onEvent(final State state) {
		final boolean isReady = state == State.ready;
		if (isReady) {
		    widget.setEnabled(true);
		    widget.showStatus(room.getOtherURI().getNode(), "ready");
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
	return "dani";
    }
}
