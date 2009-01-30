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

import com.calclab.emite.widgets.client.base.EmiteWidget;
import com.calclab.emite.xep.muc.client.Occupant;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RoomPresenceWidget extends DockPanel implements EmiteWidget {

    static class OccupantPanel extends HorizontalPanel {
	private final Label nick;

	// private Occupant occupant;

	OccupantPanel(final Occupant occupant) {
	    this.nick = new Label();
	    add(nick);
	    setOccupant(occupant);
	}

	public void setOccupant(final Occupant occupant) {
	    // this.occupant = occupant;
	    nick.setText(occupant.getNick());
	}

    }

    public static final String PARAM_ROOM = "room";

    private RoomPresenceController controller;
    private final VerticalPanel occupants;

    public RoomPresenceWidget() {
	setStylePrimaryName("emite-RoomPresenceWidget");
	this.occupants = new VerticalPanel();
	add(occupants, DockPanel.CENTER);
    }

    public void addOccupant(final OccupantPanel occupantUI) {
	occupants.add(occupantUI);
    }

    public void clearOccupants() {
	occupants.clear();
    }

    public OccupantPanel createOccupantWidget(final Occupant occupant) {
	return new OccupantPanel(occupant);
    }

    public String[] getParamNames() {
	return new String[] { PARAM_ROOM };
    }

    public void setParam(final String name, final String value) {
	if (PARAM_ROOM.equals(name)) {
	    controller.setRoomName(value);
	}
    }

    void setController(final RoomPresenceController controller) {
	this.controller = controller;

    }

}
