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

import java.util.HashMap;
import java.util.Map;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.examplechat.client.chatuiplugin.AbstractChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.AbstractChatUser;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChatUser.GroupChatUserType;

public class GroupChatPresenter extends AbstractChatPresenter implements GroupChat {

    private int oldColor;
    private String subject;
    private GroupChatUserType sessionUserType;
    // FIXME: this in GroupChatUserList?
    final Map<String, GroupChatUser> users;
    private GroupChatUserList userList;
    final GroupChatListener listener;

    public GroupChatPresenter(final GroupChatListener listener, final AbstractChatUser currentSessionUser) {
        super(currentSessionUser, TYPE_GROUP_CHAT);
        this.subject = "Subject: " + getChatTitle();
        this.oldColor = 0;
        this.input = "";
        users = new HashMap<String, GroupChatUser>();
        this.listener = listener;
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

    public void addMessage(final String userAlias, final String message) {
        String userColor;

        GroupChatUser user = users.get(userAlias);
        if (user != null) {
            userColor = user.getColor();
        } else {
            Log.error("User " + userAlias + " not in our users list");
            userColor = "black";
        }
        view.showMessage(userAlias, userColor, message);
        listener.onMessageReceived(this);
    }

    public void addUser(final GroupChatUser user) {
        user.setColor(getNextColor());
        userList.add(user);
        users.put(user.getAlias(), user);
    }

    public void removeUser(final String alias) {
        userList.remove(users.get(alias));
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

}
