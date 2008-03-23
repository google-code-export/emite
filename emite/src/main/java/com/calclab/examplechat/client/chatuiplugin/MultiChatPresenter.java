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

package com.calclab.examplechat.client.chatuiplugin;

import org.ourproject.kune.platf.client.View;

import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChat;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChatListener;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChatUser;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChatUser.UserType;

public class MultiChatPresenter implements MultiChat, GroupChatListener {
    private MultiChatView view;
    private GroupChat currentRoom;
    private final MultiChatListener listener;
    private boolean closeAllConfirmed;

    public MultiChatPresenter(final MultiChatListener listener) {
        this.listener = listener;
    }

    public void init(final MultiChatView view) {
        this.view = view;
        closeAllConfirmed = false;

        // only for tests
        view.addPresenceBuddy("fulano", "I'm out for dinner", MultiChatView.STATUS_AWAY);
        view.addPresenceBuddy("mengano", "Working", MultiChatView.STATUS_BUSY);
    }

    public GroupChat createGroupChat(final String roomName, final String userAlias, final UserType userType) {
        // Room room = ChatFactory.createRoom(this);
        // room.setRoomName(roomName);
        // room.setUserAlias(userAlias);
        // room.setUserType(userType);
        // currentRoom = room;
        // view.addRoomUsersPanel(room.getUsersListView());
        // view.addRoom(room);
        // return currentRoom;
        return null;
    }

    public void show() {
        view.show();
        closeAllConfirmed = false;
    }

    public void onSend() {
        listener.onSendMessage(currentRoom, view.getInputText());
        // view.setSendEnabled(false);
        view.clearInputText();
    }

    public void closeRoom(final GroupChatPresenter groupChat) {
        groupChat.doClose();
        listener.onCloseGroupChat(groupChat);
    }

    public void activateGroupChat(final GroupChat nextRoom) {
        if (currentRoom != null) {
            currentRoom.saveInput(view.getInputText());
        }
        view.setSendEnabled(nextRoom.isReady());
        view.setInputText(nextRoom.getSavedInput());
        view.setSubject(nextRoom.getSubject());
        view.setSubjectEditable(nextRoom.getUserType().equals(GroupChatUser.MODERADOR));
        view.showUserList(nextRoom.getUsersListView());
        nextRoom.activate();
        currentRoom = nextRoom;
    }

    public void onGroupChatReady(final GroupChat groupChat) {
        if (currentRoom == groupChat) {
            view.setSendEnabled(true);
        }
    }

    public void onMessageReceived(final GroupChat groupChat) {
        view.highlightGroupChat(groupChat);
    }

    public void onCloseAllNotConfirmed() {
        closeAllConfirmed = false;
        view.setSubject("");
    }

    public boolean isCloseAllConfirmed() {
        return closeAllConfirmed;
    }

    public void attachIconToBottomBar(final View view) {
        // DefaultDispatcher.getInstance().fire(PlatformEvents.ATTACH_TO_EXT_POINT,
        // UIExtensionPoint.CONTENT_BOTTOM_ICONBAR, view);
    }

    public void changeRoomSubject(final String text) {
        // FIXME Do the real subject rename
        view.setSubject(text);
        currentRoom.setSubject(text);
    }

    public void onStatusSelected(final int status) {
        view.setStatus(status);
        listener.onStatusSelected(status);
    }

    public void addBuddy(final String shortName, final String longName) {
        // TODO Auto-generated method stub
    }

    public void inviteUserToRoom(final String shortName, final String longName) {
        // TODO Auto-generated method stub
    }

    public void setStatus(final int status) {
        view.setStatus(status);
    }

    public void destroy() {
        view.destroy();

    }

}
