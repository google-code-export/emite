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

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.im.chat.ChatDefault;
import com.calclab.examplechat.client.chatuiplugin.abstractchat.AbstractChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUser;
import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUserList;
import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUserListView;
import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUser.GroupChatUserType;

public class GroupChatPresenter extends AbstractChatPresenter implements GroupChat {

    private int oldColor;
    private String subject;
    private GroupChatUserType sessionUserType;
    private GroupChatUserList userList;
    final GroupChatListener listener;

    public GroupChatPresenter(final ChatDefault chatDefault, final GroupChatListener listener, final GroupChatUser currentSessionUser) {
        super(chatDefault, currentSessionUser, TYPE_GROUP_CHAT);
        if (subject != null) {
            this.subject = getChatTitle();
        } else {
            this.subject = "";
        }
        this.oldColor = 0;
        this.input = "";
        this.listener = listener;
        this.sessionUserType = currentSessionUser.getUserType();
    }

    public void init(final GroupChatView view) {
        this.view = view;
        closeConfirmed = false;
    }

    public void setSessionUserType(final GroupChatUserType groupChatUserType) {
        this.sessionUserType = groupChatUserType;
    }

    public void setUserList(final GroupChatUserList userList) {
        this.userList = userList;
    }

    public void addMessage(final String userId, final String message) {
        String userColor;

        GroupChatUser user = userList.get(userId);
        if (user != null) {
            userColor = user.getColor();
        } else {
            Log.error("User " + userId + " not in our users list");
            userColor = "black";
        }
        view.addMessage(userId, userColor, message);
        listener.onMessageReceived(this);
    }

    public void addUser(final GroupChatUser user) {
        user.setColor(getNextColor());
        userList.add(user);
    }

    public void removeUser(final String userAlias) {
        GroupChatUser user = userList.get(userAlias);
        userList.remove(user);
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(final String subject) {
        this.subject = subject;
    }

    public GroupChatUserListView getUsersListView() {
        return userList.getView();
    }

    public GroupChatUserType getSessionUserType() {
        return sessionUserType;
    }

    private String getNextColor() {
        String color = USERCOLORS[oldColor++];
        if (oldColor >= USERCOLORS.length) {
            oldColor = 0;
        }
        return color;
    }

    public void onActivate() {
        listener.onActivate(this);
    }

    public void onDeactivate() {
        listener.onDeactivate(this);
    }

}
