package com.calclab.emite.widgets.comenta.client;

import java.util.Date;
import java.util.Map;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.Chat.State;
import com.calclab.emite.xep.muc.client.Room;
import com.calclab.suco.client.listener.Listener;

public class ComentaController {
    private ComentaWidget widget;
    private final Session session;
    private Room room;

    public ComentaController(final Session session) {
	this.session = session;
    }

    public ComentaWidget setWidget(final ComentaWidget widget) {
	this.widget = widget;
	widget.setEnabled(false);

	widget.onSetProperties(new Listener<Map<String, String>>() {
	    public void onEvent(final Map<String, String> properties) {
		final XmppURI roomURI = XmppURI.uri(properties.get("room"));
		if (roomURI == null) {
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

	return widget;
    }

    private void createRoom(final XmppURI roomURI) {
	room = new Room(session, roomURI);
	widget.show(null, "entering room " + roomURI.getNode() + "...");

	room.onStateChanged(new Listener<Chat.State>() {
	    public void onEvent(final State state) {
		widget.show(null, "room " + state + ".");
		widget.setEnabled(state == State.ready);
	    }
	});

	room.onMessageReceived(new Listener<Message>() {
	    public void onEvent(final Message message) {
		widget.show(message.getFrom().getResource(), message.getBody());
	    }
	});

    }

    private String generateNick() {
	return "user" + new Date().getTime();
    }
}
