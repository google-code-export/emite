/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.widgets.client.room;

import java.util.Collection;
import java.util.HashMap;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.Chat;
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
	manager.onChatCreated(new Listener<Chat>() {
	    public void onEvent(final Chat chat) {
		if (isOurRoom(chat)) {
		    listenToRoomOccupants((Room) chat);
		}
	    }

	});

	manager.onChatClosed(new Listener<Chat>() {
	    public void onEvent(final Chat chat) {
		if (isOurRoom(chat)) {
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

    private boolean isOurRoom(final Chat chat) {
	return chat.getURI().equalsNoResource(room);
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
