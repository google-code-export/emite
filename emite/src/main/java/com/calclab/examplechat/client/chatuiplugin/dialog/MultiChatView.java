/*
 *
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

package com.calclab.examplechat.client.chatuiplugin.dialog;

import com.calclab.examplechat.client.chatuiplugin.abstractchat.AbstractChat;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;
import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUserListView;

public interface MultiChatView {

    // FIXME: emite constants?
    public static final int STATUS_ONLINE = 0;
    public static final int STATUS_OFFLINE = 1;
    public static final int STATUS_BUSY = 2;
    public static final int STATUS_INVISIBLE = 3;
    public static final int STATUS_XA = 4;
    public static final int STATUS_AWAY = 5;
    public static final int STATUS_MESSAGE = 6;

    public static final String DEF_USER_COLOR = "blue";

    void addChat(AbstractChat chat);

    void addGroupChatUsersPanel(GroupChatUserListView view);

    void clearInputText();

    void destroy();

    String getInputText();

    void highlightChat(AbstractChat chat);

    void removePresenceBuddy(PairChatUser user);

    void setInputEditable(boolean editable);

    void setInputText(String savedInput);

    void setSendEnabled(boolean enabled);

    void setStatus(int statusOnline);

    void setSubject(String subject);

    void setSubjectEditable(boolean editable);

    void show();

    void showUserList(GroupChatUserListView usersListView);

    void clearSubject();

    void setGroupChatUsersPanelVisible(boolean visible);

    void setInviteToGroupChatButtonEnabled(boolean enabled);

    void closeAllChats();

    void setCloseAllOptionEnabled(boolean enabled);

    void confirmCloseAll();

    void activateChat(AbstractChat chat);

    void addPresenceBuddy(PairChatUser user);

}
