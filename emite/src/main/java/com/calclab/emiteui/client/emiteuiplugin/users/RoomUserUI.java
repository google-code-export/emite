/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emiteui.client.emiteuiplugin.users;

import com.calclab.emiteui.client.emiteuiplugin.utils.XmppJID;

public class RoomUserUI extends AbstractChatUser {

    public static enum RoomUserType {
        moderator, none, participant, visitor
    }

    private RoomUserType type;

    public RoomUserUI(final XmppJID jid, final String alias, final String color,
            final RoomUserType roomUserType) {
        super("images/person-def.gif", jid, alias, color);
        this.type = roomUserType;
    }

    public RoomUserType getUserType() {
        return type;
    }

    public void setUserType(final RoomUserType roomUserType) {
        this.type = roomUserType;
    }
}
