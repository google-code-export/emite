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
package com.calclab.examplechat.client.chatuiplugin.groupchat;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.examplechat.client.chatuiplugin.abstractchat.AbstractChat;
import com.calclab.examplechat.client.chatuiplugin.abstractchat.AbstractChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUser;
import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUserList;
import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUserListView;
import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUser.GroupChatUserType;

public class GroupChatPresenter extends AbstractChatPresenter implements GroupChat {

    private int oldColor;
    private GroupChatUserType sessionUserType;
    private String subject;
    private GroupChatUserList userList;
    final GroupChatListener listener;

    public GroupChatPresenter(final Chat chat, final GroupChatListener listener,
            final GroupChatUserType sessionGroupChatUserType) {
        super(chat, AbstractChat.Type.groupchat);
        if (subject != null) {
            this.subject = getChatTitle();
        } else {
            this.subject = "";
        }
        this.oldColor = 0;
        this.input = "";
        this.listener = listener;
        this.sessionUserType = sessionGroupChatUserType;
    }

    public void addMessage(final String userId, final String message) {
        String userColor;

        final GroupChatUser user = userList.get(userId);
        if (user != null) {
            userColor = user.getColor();
        } else {
            Log.error("User " + userId + " not in our users list");
            userColor = "black";
        }
        view.addMessage(userId, userColor, message);
        listener.onMessageAdded(this);
    }

    public void addUser(final GroupChatUser user) {
        user.setColor(getNextColor());
        userList.add(user);
    }

    public GroupChatUserType getSessionUserType() {
        return sessionUserType;
    }

    public String getSubject() {
        return subject;
    }

    public GroupChatUserListView getUsersListView() {
        return userList.getView();
    }

    public void init(final GroupChatView view) {
        this.view = view;
        closeConfirmed = false;
    }

    public void onActivate() {
        listener.onActivate(this);
    }

    public void onDeactivate() {
        listener.onDeactivate(this);
    }

    public void removeUser(final String userAlias) {
        final GroupChatUser user = userList.get(userAlias);
        userList.remove(user);
    }

    public void setSessionUserType(final GroupChatUserType groupChatUserType) {
        this.sessionUserType = groupChatUserType;
    }

    public void setSubject(final String subject) {
        this.subject = subject;
    }

    public void setUserList(final GroupChatUserList userList) {
        this.userList = userList;
    }

    private String getNextColor() {
        final String color = USERCOLORS[oldColor++];
        if (oldColor >= USERCOLORS.length) {
            oldColor = 0;
        }
        return color;
    }

}
