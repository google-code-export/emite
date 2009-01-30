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

import com.calclab.emite.widgets.client.chat.GWTAbstractChatWidget;

public class GWTRoomWidget extends GWTAbstractChatWidget implements RoomWidget {

    public static final String PARAM_NICK = "nick";
    public static final String PARAM_ROOM = "room";
    public static final String PARAM_PRESENCE = "presence";

    public GWTRoomWidget() {
	setStylePrimaryName("emite-RoomWidget");
    }

    public String[] getParamNames() {
	return new String[] { PARAM_ROOM, PARAM_NICK, PARAM_PRESENCE };
    }

    public void setParam(final String name, final String value) {
	final RoomController controller = (RoomController) getController();
	if (PARAM_ROOM.equals(name)) {
	    controller.setRoomJID(value);
	} else if (PARAM_NICK.equals(name)) {
	    controller.setNick(value);
	}
	if (PARAM_PRESENCE.equals(name)) {
	    final boolean showPresnece = "true".equals(value);
	    controller.showPresence(showPresnece);
	}
    }

}
