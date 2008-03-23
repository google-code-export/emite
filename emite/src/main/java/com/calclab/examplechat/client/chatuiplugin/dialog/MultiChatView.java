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

import com.calclab.examplechat.client.chatuiplugin.AbstractChat;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChat;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChatUserListView;

public interface MultiChatView {

    // FIXME: emite constants?
    public static final int STATUS_ONLINE = 0;
    public static final int STATUS_OFFLINE = 1;
    public static final int STATUS_BUSY = 2;
    public static final int STATUS_INVISIBLE = 3;
    public static final int STATUS_XA = 4;
    public static final int STATUS_AWAY = 5;
    public static final int STATUS_MESSAGE = 6;

    void addRoomUsersPanel(GroupChatUserListView view);

    void show();

    String getInputText();

    void addGroupChat(GroupChat groupChat);

    void setInputText(String savedInput);

    void setSubject(String subject);

    void showUserList(GroupChatUserListView usersListView);

    void setSendEnabled(boolean enabled);

    void clearInputText();

    void setSubjectEditable(boolean editable);

    void setStatus(int statusOnline);

    void addPresenceBuddy(String buddyName, String title, int status);

    void removePresenceBuddy(String buddyName);

    void destroy();

    void highlightChat(AbstractChat chat);

    void setInputEditable(boolean editable);

}
