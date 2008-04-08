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
package com.calclab.examplechat.client.chatuiplugin.users;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.examplechat.client.chatuiplugin.abstractchat.AbstractChatUser;

public class GroupChatUser extends AbstractChatUser {

    public static enum GroupChatUserType {
        moderator, none, participant, visitor
    }

    private GroupChatUserType type;

    public GroupChatUser(final XmppURI jid, final String alias, final String color,
            final GroupChatUserType groupChatUserType) {
        super("images/person-def.gif", jid, alias, color);
        this.type = groupChatUserType;
    }

    public GroupChatUserType getUserType() {
        return type;
    }

    public void setUserType(final GroupChatUserType groupChatUserType) {
        this.type = groupChatUserType;
    }
}
