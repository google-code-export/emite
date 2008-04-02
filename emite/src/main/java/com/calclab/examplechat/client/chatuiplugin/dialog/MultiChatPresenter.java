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
import org.ourproject.kune.platf.client.extend.UIExtensionElement;
import org.ourproject.kune.platf.client.extend.UIExtensionPoint;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.xmpp.stanzas.XmppJID;
import com.calclab.examplechat.client.chatuiplugin.ChatDialogFactory;
import com.calclab.examplechat.client.chatuiplugin.abstractchat.AbstractChat;
import com.calclab.examplechat.client.chatuiplugin.abstractchat.ChatId;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChat;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChatListener;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChat;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatListener;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;
import com.calclab.examplechat.client.chatuiplugin.params.ChatMessageParam;
import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUser;
import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUser.GroupChatUserType;

public class MultiChatPresenter implements MultiChat, GroupChatListener, PairChatListener {
    private MultiChatView view;
    private AbstractChat currentChat;
    private final MultiChatListener listener;
    private boolean closeAllConfirmed;
    private final PairChatUser currentSessionUser;
    private final HashMap<ChatId, AbstractChat> chats;

    public MultiChatPresenter(final PairChatUser currentSessionUser, final MultiChatListener listener) {
        this.currentSessionUser = currentSessionUser;
        this.listener = listener;
        chats = new HashMap<ChatId, AbstractChat>();
    }

    public void init(final MultiChatView view) {
        this.view = view;
        reset();
    }

    public GroupChat createGroupChat(final String groupJid, final String userAlias,
            final GroupChatUserType groupChatUserType) {
        ChatId chatId = new ChatId(XmppJID.parseJID(groupJid));
        AbstractChat abstractChat = chats.get(chatId);
        if (abstractChat != null) {
            activateChat(abstractChat);
            return (GroupChat) abstractChat;
        }
        GroupChatUser groupChatUser = new GroupChatUser(currentSessionUser.getJid(), userAlias,
                MultiChatView.DEF_USER_COLOR, groupChatUserType);
        GroupChat groupChat = ChatDialogFactory.createGroupChat(chatId, this, groupChatUser);
        view.addGroupChatUsersPanel(groupChat.getUsersListView());
        view.setSubject("");
        return (GroupChat) finishChatCreation(groupChat, groupJid);
    }

    public PairChat createPairChat(final PairChatUser otherUser) {
        ChatId chatId = new ChatId(otherUser.getJid());
        AbstractChat abstractChat = chats.get(chatId);
        if (abstractChat != null) {
            activateChat(abstractChat);
            return (PairChat) abstractChat;
        }
        PairChat pairChat = ChatDialogFactory.createPairChat(chatId, this, currentSessionUser, otherUser);
        return (PairChat) finishChatCreation(pairChat, otherUser.getAlias());
    }

    public void show() {
        view.show();
        closeAllConfirmed = false;
    }

    public void closeGroupChat(final GroupChatPresenter groupChat) {
        groupChat.doClose();
        chats.remove(groupChat.getChatTitle());
        listener.onCloseGroupChat(groupChat);
        checkNoChats();
    }

    public void closePairChat(final PairChatPresenter pairChat) {
        pairChat.doClose();
        chats.remove(pairChat.getOtherUser().getJid());
        listener.onClosePairChat(pairChat);
        checkNoChats();
    }

    public void closeAllChats(final boolean withConfirmation) {
        if (withConfirmation) {
            view.confirmCloseAll();
        } else {
            onCloseAllConfirmed();
        }
    }

    public void onCloseAllNotConfirmed() {
        closeAllConfirmed = false;
    }

    public void onCloseAllConfirmed() {
        closeAllConfirmed = true;
        view.closeAllChats();
        reset();
    }

    public boolean isCloseAllConfirmed() {
        return closeAllConfirmed;
    }

    public void activateChat(final String chatId) {
        AbstractChat chat = getChat(chatId);
        activateChat(chat);
    }

    public void activateChat(final AbstractChat chat) {
        view.activateChat(chat);
        onActivate(chat);
    }

    public void onActivate(final AbstractChat nextChat) {
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
            view.setSubjectEditable(false);
        }
        view.setInputText(nextChat.getSavedInput());
        nextChat.activate();
        currentChat = nextChat;
    }

    public void onDeactivate(final AbstractChat chat) {
        chat.saveInput(view.getInputText());
        chat.saveOtherProperties();
    }

    public void onCurrentUserSend(final String message) {
        ChatMessageParam outputMessage = new ChatMessageParam(currentSessionUser.getJid(),
                currentChat.getId().getJid(), message);
        listener.onSendMessage(outputMessage);
        messageReceived(outputMessage);
        view.clearInputText();
    }

    public void messageReceived(final ChatMessageParam param) {
        String chatId;
        String from = param.getFrom().toString();
        String to = param.getTo().toString();
        String message = param.getMessage();
        if (currentSessionUser.getJid().equals(from)) {
            chatId = to;
        } else {
            chatId = from;
        }
        AbstractChat chat = getChat(chatId);
        if (chat.getType() == AbstractChat.TYPE_GROUP_CHAT) {
            ((GroupChat) chat).addMessage(from, message);
        } else {
            ((PairChat) chat).addMessage(from, message);
        }
    }

    public void onMessageReceived(final AbstractChat chat) {
        view.highlightChat(chat);
    }

    public void addUsetToGroupChat(final String chatId, final GroupChatUser groupChatUser) {
        GroupChat chat = (GroupChat) chats.get(chatId);
        checkIsGroupChat(chat);
        chat.addUser(groupChatUser);
    }

    public void attachIconToBottomBar(final View view) {
        listener.attachToExtPoint(new UIExtensionElement(UIExtensionPoint.CONTENT_BOTTOM_ICONBAR, view));
    }

    public void groupChatSubjectChanged(final String groupChatName, final String newSubject) {
        AbstractChat groupChat = getChat(groupChatName);
        checkIsGroupChat(groupChat);
        ((GroupChat) groupChat).setSubject(newSubject);
        view.setSubject(newSubject);

    }

    public void onSubjectChangedByCurrentUser(final String text) {
        GroupChat groupChat = (GroupChat) currentChat;
        groupChat.setSubject(text);
        listener.setGroupChatSubject(groupChat, text);
        // FIXME callback? erase this:
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

    public void doAction(final String eventId, final Object param) {
        listener.doAction(eventId, param);
    }

    private AbstractChat getChat(final String chatId) {
        AbstractChat chat = chats.get(chatId);
        if (chat == null) {
            String error = "Unexpected chatId '" + chatId + "'";
            Log.error(error);
            throw new RuntimeException(error);
        }
        return chat;
    }

    private AbstractChat finishChatCreation(final AbstractChat chat, final String chatTitle) {
        chat.setChatTitle(chatTitle);
        currentChat = chat;
        view.addChat(chat);
        chats.put(chat.getId(), chat);
        checkThereAreChats();
        return chat;
    }

    private void checkIsGroupChat(final AbstractChat chat) {
        if (chat.getType() != AbstractChat.TYPE_GROUP_CHAT) {
            new RuntimeException("You cannot do this operation in a this kind of chat");
        }
    }

    private void checkNoChats() {
        if (chats.size() == 0) {
            view.setCloseAllOptionEnabled(false);
            setInputEnabled(false);
        }
    }

    private void checkThereAreChats() {
        if (chats.size() == 1) {
            view.setCloseAllOptionEnabled(true);
            setInputEnabled(true);
        }
    }

    private void reset() {
        currentChat = null;
        closeAllConfirmed = false;
        view.setStatus(MultiChatView.STATUS_OFFLINE);
        view.setCloseAllOptionEnabled(false);
        view.setSubjectEditable(false);
        setInputEnabled(false);
    }

    private void setInputEnabled(final boolean enabled) {
        view.setSendEnabled(enabled);
        view.setInputEditable(enabled);
        view.setEmoticonButton(enabled);
    }

    public void addPresenceBuddy(final PairChatUser user) {
        view.addPresenceBuddy(user);
    }

}
