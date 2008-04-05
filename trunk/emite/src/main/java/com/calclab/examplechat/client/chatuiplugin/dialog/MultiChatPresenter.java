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
package com.calclab.examplechat.client.chatuiplugin.dialog;

import java.util.HashMap;

import org.ourproject.kune.platf.client.View;
import org.ourproject.kune.platf.client.extend.UIExtensionElement;
import org.ourproject.kune.platf.client.extend.UIExtensionPoint;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.examplechat.client.chatuiplugin.ChatDialogFactory;
import com.calclab.examplechat.client.chatuiplugin.abstractchat.AbstractChat;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChat;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChatListener;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChat;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatListener;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;
import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUser;
import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUser.GroupChatUserType;

public class MultiChatPresenter implements MultiChat, GroupChatListener, PairChatListener {
    private final HashMap<Chat, AbstractChat> chats;
    private boolean closeAllConfirmed;
    private AbstractChat currentChat;
    private final PairChatUser currentSessionUser;
    private final ChatDialogFactory factory;
    private final MultiChatListener listener;
    private MultiChatView view;
    private final HashMap<String, PairChatUser> roster;

    public MultiChatPresenter(final ChatDialogFactory factory, final PairChatUser currentSessionUser,
            final MultiChatListener listener) {
        this.factory = factory;
        this.currentSessionUser = currentSessionUser;
        this.listener = listener;
        chats = new HashMap<Chat, AbstractChat>();
        roster = new HashMap<String, PairChatUser>();
    }

    public void activateChat(final AbstractChat chat) {
        view.activateChat(chat);
        onActivate(chat);
    }

    public void activateChat(final Chat chat) {
        final AbstractChat abstractChat = getChat(chat);
        activateChat(abstractChat);
    }

    public void addBuddy(final String shortName, final String longName) {
    }

    public void addRosterItem(final PairChatUser user) {
        roster.put(user.getUri().getJid(), user);
        view.addPresenceBuddy(user);
    }

    public void removePresenceBuddy(final PairChatUser user) {
        roster.remove(user.getUri().getJid());
        view.removePresenceBuddy(user);
    }

    public void addUsetToGroupChat(final String chatId, final GroupChatUser groupChatUser) {
        final GroupChat chat = (GroupChat) chats.get(chatId);
        checkIsGroupChat(chat);
        chat.addUser(groupChatUser);
    }

    public void attachIconToBottomBar(final View view) {
        listener.attachToExtPoint(new UIExtensionElement(UIExtensionPoint.CONTENT_BOTTOM_ICONBAR, view));
    }

    public void closeAllChats(final boolean withConfirmation) {
        if (withConfirmation) {
            view.confirmCloseAll();
        } else {
            onCloseAllConfirmed();
        }
    }

    public void closeGroupChat(final GroupChatPresenter groupChat) {
        groupChat.doClose();
        chats.remove(groupChat.getChat());
        listener.onCloseGroupChat(groupChat);
        checkNoChats();
    }

    public void closePairChat(final PairChatPresenter pairChat) {
        pairChat.doClose();
        chats.remove(pairChat.getChat());
        listener.onClosePairChat(pairChat);
        checkNoChats();
    }

    public GroupChat createGroupChat(final Chat chat, final String userAlias, final GroupChatUserType groupChatUserType) {
        final AbstractChat abstractChat = chats.get(chat);
        if (abstractChat != null) {
            activateChat(abstractChat);
            return (GroupChat) abstractChat;
        }
        final GroupChatUser groupChatUser = new GroupChatUser(currentSessionUser.getUri(), userAlias,
                MultiChatView.DEF_USER_COLOR, groupChatUserType);
        final GroupChat groupChat = factory.createGroupChat(chat, this, groupChatUser);
        view.addGroupChatUsersPanel(groupChat.getUsersListView());
        view.setSubject("");
        return (GroupChat) finishChatCreation(groupChat, chat.getOtherURI().toString());
    }

    public PairChat createPairChat(final Chat chat) {
        final AbstractChat abstractChat = chats.get(chat);
        if (abstractChat != null) {
            activateChat(abstractChat);
            return (PairChat) abstractChat;
        }
        PairChatUser otherUser = roster.get(chat.getOtherURI().getJid());
        if (otherUser == null) {

            String error = "Message from user not in roster";
            Log.error(error);
            throw new RuntimeException(error);
        }
        final PairChat pairChat = factory.createPairChat(chat, this, currentSessionUser, otherUser);
        return (PairChat) finishChatCreation(pairChat, chat.getOtherURI().toString());
    }

    public void destroy() {
        view.destroy();

    }

    public void doAction(final String eventId, final Object param) {
        listener.doAction(eventId, param);
    }

    public void groupChatSubjectChanged(final Chat groupChat, final String newSubject) {
        final AbstractChat abstractChat = getChat(groupChat);
        checkIsGroupChat(abstractChat);
        ((GroupChat) abstractChat).setSubject(newSubject);
        view.setSubject(newSubject);

    }

    public void init(final MultiChatView view) {
        this.view = view;
        reset();
    }

    public void inviteUserToRoom(final String shortName, final String longName) {
    }

    public boolean isCloseAllConfirmed() {
        return closeAllConfirmed;
    }

    public void messageReceived(final Chat chat, final Message message) {
        final AbstractChat abstractChat = getChat(chat);
        if (abstractChat.getType() == AbstractChat.TYPE_GROUP_CHAT) {
            ((GroupChat) abstractChat).addMessage(message.getFrom().toString(), message.getBody());
        } else {
            ((PairChat) abstractChat).addMessage(XmppURI.parse(message.getFrom()), message.getBody());
        }
    }

    public void onActivate(final AbstractChat nextChat) {
        view.setInputText(nextChat.getSavedInput());
        if (nextChat.getType() == AbstractChat.TYPE_GROUP_CHAT) {
            view.setGroupChatUsersPanelVisible(true);
            view.setInviteToGroupChatButtonEnabled(true);
            final GroupChatPresenter groupChat = (GroupChatPresenter) nextChat;
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

    public void onCloseAllConfirmed() {
        closeAllConfirmed = true;
        view.closeAllChats();
        reset();
    }

    public void onCloseAllNotConfirmed() {
        closeAllConfirmed = false;
    }

    public void onCurrentUserSend(final String message) {
        currentChat.getChat().send(message);
        // messageReceived(outputMessage);
        view.clearInputText();
    }

    public void onDeactivate(final AbstractChat chat) {
        chat.saveInput(view.getInputText());
        chat.saveOtherProperties();
    }

    public void onMessageAdded(final AbstractChat chat) {
        view.highlightChat(chat);
    }

    public void onStatusSelected(final int status) {
        view.setStatus(status);
        listener.onStatusSelected(status);
    }

    public void onSubjectChangedByCurrentUser(final String text) {
        final GroupChat groupChat = (GroupChat) currentChat;
        groupChat.setSubject(text);
        listener.setGroupChatSubject(groupChat.getChat(), text);
        // FIXME callback? erase this:
        view.setSubject(text);
    }

    public void onUserColorChanged(final String color) {
        currentSessionUser.setColor(color);
        for (final AbstractChat chat : chats.values()) {
            chat.setSessionUserColor(color);
        }
        listener.onUserColorChanged(color);
    }

    public void setStatus(final int status) {
        view.setStatus(status);
    }

    public void show() {
        view.show();
        closeAllConfirmed = false;
    }

    public void onSubscriptionRequest(final Presence presence) {
        view.confirmSusbscriptionRequest(presence);
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

    private AbstractChat finishChatCreation(final AbstractChat abstractChat, final String chatTitle) {
        abstractChat.setChatTitle(chatTitle);
        currentChat = abstractChat;
        view.addChat(abstractChat);
        chats.put(abstractChat.getChat(), abstractChat);
        checkThereAreChats();
        return abstractChat;
    }

    private AbstractChat getChat(final Chat chat) {
        final AbstractChat uiChat = chats.get(chat);
        if (chat == null) {
            final String error = "Unexpected chatId '" + chat.getID().toString() + "'";
            Log.error(error);
            throw new RuntimeException(error);
        }
        return uiChat;
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

    public void onPresenceAccepted(final Presence presence) {
        listener.onPresenceAccepted(presence);
    }

    public void onPresenceNotAccepted(final Presence presence) {
        listener.onPresenceNotAccepted(presence);
    }

}
