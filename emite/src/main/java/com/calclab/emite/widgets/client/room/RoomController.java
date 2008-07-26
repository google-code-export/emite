package com.calclab.emite.widgets.client.room;

import java.util.Date;

import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.xep.muc.RoomManager;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.session.Session.State;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.signal.Slot;

public class RoomController {
    private XmppURI room;
    private RoomWidget widget;
    private final Session session;
    private final RoomManager manager;
    protected Chat chat;
    private String nick;

    public RoomController(final Session session, final RoomManager manager) {
	this.session = session;
	this.manager = manager;
	this.nick = "" + new Date().getTime();
    }

    public void setNick(final String nick) {
	this.nick = nick;
    }

    public void setWidget(final RoomWidget widget) {
	this.widget = widget;
	widget.setController(this);
	init();
    }

    void setRoomName(final String roomName) {
	assert this.room == null;
	this.room = XmppURI.uri(roomName);
	showWaitingStatus();
    }

    private void init() {
	widget.setInputEnabled(false);
	session.onStateChanged(new Slot<Session.State>() {
	    public void onEvent(final State state) {
		if (state == State.disconnected) {
		    showWaitingStatus();
		} else if (state == State.ready) {
		    widget.setStatus("Room: " + room);
		    final XmppURI uri = new XmppURI(room.getNode(), room.getHost(), nick);
		    manager.openChat(uri, null, null);
		    widget.write(null, "Opening chat room...");
		}
	    }
	});

	manager.onChatCreated(new Slot<Chat>() {
	    public void onEvent(final Chat chat) {
		setChat(chat);
		widget.setInputEnabled(true);
	    }
	});

	widget.onSendMessage.add(new Slot<String>() {
	    public void onEvent(final String body) {
		chat.send(new Message(body));
	    }
	});
    }

    private void setChat(final Chat chat) {
	this.chat = chat;
	widget.write(null, "room opened");
	chat.onMessageReceived(new Slot<Message>() {
	    public void onEvent(final Message message) {
		widget.write(message.getFromAsString(), message.getBody());
	    }
	});

	chat.onMessageSent(new Slot<Message>() {
	    public void onEvent(final Message message) {
		widget.write("me", message.getBody());
	    }
	});
    }

    private void showWaitingStatus() {
	widget.setStatus("Waiting login to connect: " + room.toString());
    }

}
