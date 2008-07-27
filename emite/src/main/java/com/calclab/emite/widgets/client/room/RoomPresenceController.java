package com.calclab.emite.widgets.client.room;

import java.util.Collection;
import java.util.HashMap;

import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.xep.muc.Occupant;
import com.calclab.emite.client.xep.muc.Room;
import com.calclab.emite.client.xep.muc.RoomManager;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.widgets.client.room.RoomPresenceWidget.OccupantPanel;
import com.calclab.suco.client.signal.Slot;

public class RoomPresenceController {

    private XmppURI room;
    private final RoomManager manager;
    private RoomPresenceWidget widget;
    private final HashMap<XmppURI, OccupantPanel> occupantsUIByXmpp;

    public RoomPresenceController(final RoomManager manager) {
	this.occupantsUIByXmpp = new HashMap<XmppURI, OccupantPanel>();
	this.manager = manager;
	room = null;
    }

    public void setRoomName(final String name) {
	assert room == null;
	this.room = XmppURI.uri(name);
	setWaitingStatus();
    }

    public void setWidget(final RoomPresenceWidget widget) {
	this.widget = widget;
	widget.setController(this);
	manager.onChatCreated(new Slot<Chat>() {
	    public void onEvent(final Chat chat) {
		if (isOurRoom(chat)) {
		    listenToRoomOccupants((Room) chat);
		    widget.setStatus("ready");
		}
	    }

	});

	manager.onChatClosed(new Slot<Chat>() {
	    public void onEvent(final Chat chat) {
		if (isOurRoom(chat)) {
		    widget.clearOccupants();
		    setWaitingStatus();
		}
	    }
	});
    }

    private OccupantPanel addNewOccupant(final Occupant o) {
	final OccupantPanel occupantUI = widget.createOccupantWidget(o);
	widget.addOccupant(occupantUI);
	return occupantUI;
    }

    private boolean isOurRoom(final Chat chat) {
	return chat.getOtherURI().equalsNoResource(room);
    }

    private void listenToRoomOccupants(final Room room) {
	room.onOccupantsChanged(new Slot<Collection<Occupant>>() {
	    public void onEvent(final Collection<Occupant> occupants) {
		widget.clearOccupants();
		for (final Occupant o : occupants) {
		    addNewOccupant(o);
		}
	    }
	});

	room.onOccupantModified(new Slot<Occupant>() {
	    public void onEvent(final Occupant occupant) {
		final OccupantPanel occupantUI = occupantsUIByXmpp.get(occupant.getUri());
		if (occupantUI == null) {
		    addNewOccupant(occupant);
		} else {
		    occupantUI.setOccupant(occupant);
		}
	    }
	});
    }

    private void setWaitingStatus() {
	widget.setStatus("waiting for " + room + " presence information...");
    }

}
