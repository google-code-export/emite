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
package com.calclab.emiteuimodule.client.params;

import com.calclab.emiteuimodule.client.UserChatOptions;

public class MultiChatCreationParam {

    private final UserChatOptions userChatOptions;
    private final String roomHost;
    private final String chatDialogTitle;
    private final AvatarProvider avatarProvider;

    public MultiChatCreationParam(final String chatDialogTitle, final String roomHost,
            final AvatarProvider avatarProvider, final UserChatOptions userChatOptions) {
        this.chatDialogTitle = chatDialogTitle;
        this.roomHost = roomHost;
        this.avatarProvider = avatarProvider;
        this.userChatOptions = userChatOptions;
    }

    public AvatarProvider getAvatarProvider() {
        return avatarProvider;
    }

    public String getChatDialogTitle() {
        return chatDialogTitle;
    }

    public String getRoomHost() {
        return roomHost;
    }

    public UserChatOptions getUserChatOptions() {
        return userChatOptions;
    }

}
