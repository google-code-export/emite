package com.calclab.emite.j2se.swing.chat;

import java.util.Collection;

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.im.client.chat.Conversation;
import com.calclab.emite.xep.muc.client.Occupant;
import com.calclab.emite.xep.muc.client.Room;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.suco.client.events.Listener;
import com.calclab.suco.client.events.Listener0;
import com.calclab.suco.client.events.Listener2;

public class RoomControl extends ChatControl {

    public RoomControl(final RoomManager roomManager, final Room room, final RoomPanel roomPanel) {
	super(roomManager, room, roomPanel);

	room.onStateChanged(new Listener<Conversation.State>() {
	    public void onEvent(final Conversation.State state) {
		roomPanel.showIcomingMessage(null, "Room is now: " + state.toString());
	    }

	});

	roomPanel.onClose(new Listener0() {
	    public void onEvent() {
		roomManager.close(room);
	    }
	});
	roomPanel.onSend(new Listener<String>() {
	    public void onEvent(final String text) {
		roomPanel.showOutMessage(text);
		roomPanel.clearMessage();
	    }
	});
	roomPanel.onSubjectChanged(new Listener<String>() {
	    public void onEvent(final String newSubject) {
		room.setSubject(newSubject);
	    }
	});
	roomPanel.onUserInvited(new Listener2<String, String>() {
	    public void onEvent(final String userJid, final String reasonText) {
		room.sendInvitationTo(userJid, reasonText);
	    }
	});

	room.onMessageReceived(new Listener<Message>() {
	    public void onEvent(final Message message) {
		roomPanel.showIcomingMessage(message.getFromAsString(), message.getBody());
	    }
	});

	room.onMessageSent(new Listener<Message>() {
	    public void onEvent(final Message message) {
		if (!room.isComingFromMe(message)) {
		    roomPanel.showOutMessage(message.getBody());
		}
	    }
	});

	room.onOccupantsChanged(new Listener<Collection<Occupant>>() {
	    public void onEvent(final Collection<Occupant> users) {
		roomPanel.setUsers(users);
	    }
	});

	room.onSubjectChanged(new Listener2<Occupant, String>() {
	    public void onEvent(final Occupant occupant, final String newSubject) {
		final String nick = occupant != null ? occupant.getNick() : "";
		roomPanel.showIcomingMessage(nick, "New subject: " + newSubject);
	    }
	});
    }

}
