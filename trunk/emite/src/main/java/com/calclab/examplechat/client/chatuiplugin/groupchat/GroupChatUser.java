/*
 * Copyright (C) 2007 The kune development team (see CREDITS for details)
 * This file is part of kune.
 *
 * Kune is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Kune is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.calclab.examplechat.client.chatuiplugin.groupchat;

import com.calclab.examplechat.client.chatuiplugin.AbstractChatUser;

public class GroupChatUser extends AbstractChatUser {

    public static final GroupChatUserType MODERADOR = new GroupChatUserType();
    public static final GroupChatUserType PARTICIPANT = new GroupChatUserType();
    public static final GroupChatUserType VISITOR = new GroupChatUserType();
    public static final GroupChatUserType NONE = new GroupChatUserType();

    public static class GroupChatUserType {
        private GroupChatUserType() {
        }
    }

    private GroupChatUserType type;

    public GroupChatUser(final String jid, final String alias, final String color,
            final GroupChatUserType groupChatUserType) {
        super(jid, alias, color);
        this.type = groupChatUserType;
    }

    public GroupChatUserType getUserType() {
        return type;
    }

    public void setUserType(final GroupChatUserType groupChatUserType) {
        this.type = groupChatUserType;
    }
}
