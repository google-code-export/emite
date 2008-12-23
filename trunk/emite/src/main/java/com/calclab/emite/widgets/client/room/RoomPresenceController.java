package com.calclab.emite.widgets.client.room;

import java.util.Collection;
import java.util.HashMap;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.Conversation;
import com.calclab.emite.widgets.client.room.RoomPresenceWidget.OccupantPanel;
import com.calclab.emite.xep.muc.client.Occupant;
import com.calclab.emite.xep.muc.client.Room;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.suco.client.events.Listener;

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
    }

    public void setWidget(final RoomPresenceWidget widget) {
	this.widget = widget;
	widget.setController(this);
	manager.onChatCreated(new Listener<Conversation>() {
	    public void onEvent(final Conversation conversation) {
		if (isOurRoom(conversation)) {
		    listenToRoomOccupants((Room) conversation);
		}
	    }

	});

	manager.onChatClosed(new Listener<Conversation>() {
	    public void onEvent(final Conversation conversation) {
		if (isOurRoom(conversation)) {
		    widget.clearOccupants();
		}
	    }
	});
    }

    private OccupantPanel addNewOccupant(final Occupant o) {
	final OccupantPanel occupantUI = widget.createOccupantWidget(o);
	widget.addOccupant(occupantUI);
	return occupantUI;
    }

    private boolean isOurRoom(final Conversation conversation) {
	return conversation.getURI().equalsNoResource(room);
    }

    private void listenToRoomOccupants(final Room room) {
	room.onOccupantsChanged(new Listener<Collection<Occupant>>() {
	    public void onEvent(final Collection<Occupant> occupants) {
		widget.clearOccupants();
		for (final Occupant o : occupants) {
		    addNewOccupant(o);
		}
	    }
	});

	room.onOccupantModified(new Listener<Occupant>() {
	    public void onEvent(final Occupant occupant) {
		final OccupantPanel occupantUI = occupantsUIByXmpp.get(occupant.getURI());
		if (occupantUI == null) {
		    addNewOccupant(occupant);
		} else {
		    occupantUI.setOccupant(occupant);
		}
	    }
	});
    }

}
