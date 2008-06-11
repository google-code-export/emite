package com.calclab.emite.client.xep.muc;

import java.util.Collection;

import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.suco.client.signal.Slot;
import com.calclab.suco.client.signal.Slot2;

public class RoomListenerAdaptor {

    public RoomListenerAdaptor(final Chat roomChat, final RoomListener listener) {
	final Room room = (Room) roomChat;

	room.onMessageReceived(new Slot<Message>() {
	    public void onEvent(final Message message) {
		listener.onMessageReceived(room, message);
	    }
	});

	room.onMessageSent(new Slot<Message>() {
	    public void onEvent(final Message message) {
		listener.onMessageSent(room, message);
	    }
	});

	room.onOccupantModified(new Slot<Occupant>() {
	    public void onEvent(final Occupant occupant) {
		listener.onOccupantModified(occupant);
	    }
	});

	room.onOccupantsChanged(new Slot<Collection<Occupant>>() {
	    public void onEvent(final Collection<Occupant> occupants) {
		listener.onOccupantsChanged(occupants);
	    }
	});

	room.onSubjectChanged(new Slot2<Occupant, String>() {
	    public void onEvent(final Occupant occupant, final String subject) {
		listener.onSubjectChanged(occupant.getNick(), subject);
	    }
	});
    }

}
