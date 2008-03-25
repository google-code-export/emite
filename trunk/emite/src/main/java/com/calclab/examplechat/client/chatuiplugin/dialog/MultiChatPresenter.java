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

import java.util.HashMap;
import java.util.Iterator;

import org.ourproject.kune.platf.client.View;

import com.calclab.examplechat.client.chatuiplugin.AbstractChat;
import com.calclab.examplechat.client.chatuiplugin.AbstractChatUser;
import com.calclab.examplechat.client.chatuiplugin.ChatDialogFactory;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChat;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChatListener;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChatUser;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChatUser.GroupChatUserType;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChat;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatListener;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;

public class MultiChatPresenter implements MultiChat, GroupChatListener, PairChatListener {
    private MultiChatView view;
    private AbstractChat currentChat;
    private final MultiChatListener listener;
    private boolean closeAllConfirmed;
    private final AbstractChatUser currentSessionUser;
    private final HashMap<String, AbstractChat> chats;

    public MultiChatPresenter(final AbstractChatUser currentSessionUser, final MultiChatListener listener) {
        this.currentSessionUser = currentSessionUser;
        this.listener = listener;
        currentChat = null;
        chats = new HashMap<String, AbstractChat>();
        // view.setSendEnabled(false);
        // view.setInputEditable(false);
        // view.setSubjectEditable(false);
    }

    public void init(final MultiChatView view) {
        this.view = view;
        closeAllConfirmed = false;

        // only for tests
        view.addPresenceBuddy("fulano", "I'm out for dinner", MultiChatView.STATUS_AWAY);
        view.addPresenceBuddy("mengano", "Working", MultiChatView.STATUS_BUSY);
    }

    public GroupChat createGroupChat(final String groupChatName, final String userAlias,
            final GroupChatUserType groupChatUserType) {

        AbstractChat abstractChat = chats.get(groupChatName);
        if (abstractChat != null) {
            activateChat(abstractChat);
            return (GroupChat) abstractChat;
        }
        GroupChatUser groupChatUser = new GroupChatUser(currentSessionUser.getJid(), userAlias, "blue",
                groupChatUserType);
        GroupChat groupChat = ChatDialogFactory.createGroupChat(this, groupChatUser);
        groupChat.setChatTitle(groupChatName);
        currentChat = groupChat;
        view.addGroupChatUsersPanel(groupChat.getUsersListView());
        view.addChat(groupChat);
        chats.put(groupChatName, groupChat);
        return groupChat;
    }

    public PairChat createPairChat(final PairChatUser otherUser) {
        String otherUserAlias = otherUser.getAlias();
        AbstractChat abstractChat = chats.get(otherUserAlias);
        if (abstractChat != null) {
            activateChat(abstractChat);
            return (PairChat) abstractChat;
        }
        PairChatUser currentePairChatUser = new PairChatUser(currentSessionUser.getJid(), currentSessionUser.getAlias());
        PairChat pairChat = ChatDialogFactory.createPairChat(this, currentePairChatUser, otherUser);
        pairChat.setChatTitle(otherUserAlias);
        currentChat = pairChat;
        view.addChat(pairChat);
        return pairChat;
    }

    public void show() {
        view.show();
        closeAllConfirmed = false;
    }

    public void onSend() {
        if (currentChat.getType() == AbstractChat.TYPE_PAIR_CHAT) {
            listener.onSendMessage(((PairChat) currentChat).getOtherUser(), view.getInputText());
        } else {
            listener.onSendMessage(((GroupChat) currentChat), view.getInputText());
        }
        // view.setSendEnabled(false);
        view.clearInputText();
    }

    public void closeGroupChat(final GroupChatPresenter groupChat) {
        groupChat.doClose();
        chats.remove(groupChat.getChatTitle());
        listener.onCloseGroupChat(groupChat);
    }

    public void closePairChat(final PairChatPresenter pairChat) {
        pairChat.doClose();
        chats.remove(pairChat.getChatTitle());
        listener.onClosePairChat(pairChat);
    }

    public void activateChat(final AbstractChat chat) {
        onActivate(chat);
    }

    public void onActivate(final AbstractChat nextChat) {
        if (currentChat != null) {
            currentChat.saveInput(view.getInputText());
            currentChat.saveOtherProperties();
        }
        // view.setSendEnabled(nextRoom.isReady());
        view.setInputText(nextChat.getSavedInput());
        if (nextChat.getType() == AbstractChat.TYPE_GROUP_CHAT) {
            view.setGroupChatUsersPanelVisible(true);
            view.setInviteToGroupChatButtonEnabled(true);
            GroupChatPresenter groupChat = (GroupChatPresenter) nextChat;
            view.setSubject(groupChat.getSubject());
            view.setSubjectEditable(groupChat.getSessionUserType().equals(GroupChatUser.MODERADOR));
            view.showUserList(groupChat.getUsersListView());
        } else {
            view.setGroupChatUsersPanelVisible(false);
            view.setInviteToGroupChatButtonEnabled(false);
            view.clearSubject();
        }
        view.setInputText(nextChat.getSavedInput());
        nextChat.activate();
        currentChat = nextChat;
    }

    public void onMessageReceived(final AbstractChat chat) {
        view.highlightChat(chat);
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

    public void changeGroupChatSubject(final String text) {
        GroupChat groupChat = (GroupChat) currentChat;
        groupChat.setSubject(text);
        listener.setGroupChatSubject(groupChat, text);
        // FIXME callback?
        view.setSubject(text);

    }

    public void onStatusSelected(final int status) {
        view.setStatus(status);
        listener.onStatusSelected(status);
    }

    public void addBuddy(final String shortName, final String longName) {
    }

    public void inviteUserToRoom(final String shortName, final String longName) {
    }

    public void setStatus(final int status) {
        view.setStatus(status);
    }

    public void destroy() {
        view.destroy();

    }

    public void onUserColorChanged(final String color) {
        currentSessionUser.setColor(color);
        for (Iterator<AbstractChat> iterator = chats.values().iterator(); iterator.hasNext();) {
            AbstractChat chat = iterator.next();
            chat.setSessionUserColor(color);
        }
        listener.onUserColorChanged(color);
    }

    public void onCloseAllConfirmed() {
        closeAllConfirmed = true;
        view.closeAllChats();
        // for (Iterator<AbstractChat> iterator = chats.values().iterator();
        // iterator.hasNext();) {
        // AbstractChat chat = iterator.next();
        // chat.doClose();
        // // TODO
        // }
    }

}
