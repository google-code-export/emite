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
package com.calclab.emiteuimodule.client.users;

import com.calclab.emite.xep.muc.client.Occupant;
import com.calclab.emite.xep.muc.client.Occupant.Role;

public class RoomUserUI extends AbstractChatUser {

    private final Occupant occupant;

    public RoomUserUI(final Occupant occupant, final String color) {
        super("images/person-def.gif", occupant.getURI(), occupant.getURI().getResource(), color);
        this.occupant = occupant;
    }

    public Role getRole() {
        return occupant.getRole();
    }
}
