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
import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.chat.ChatListener;
import com.calclab.emite.client.im.chat.ChatManagerListener;
import com.calclab.emite.client.im.presence.PresenceManager;
import com.calclab.emite.client.im.roster.Roster.SubscriptionMode;
import com.calclab.emite.client.xmpp.session.SessionListener;
import com.calclab.emite.client.xmpp.session.Session.State;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.Presence.Show;
import com.calclab.examplechat.client.chatuiplugin.ChatDialogFactory;
import com.calclab.examplechat.client.chatuiplugin.EmiteUiPlugin;
import com.calclab.examplechat.client.chatuiplugin.UserChatOptions;
import com.calclab.examplechat.client.chatuiplugin.abstractchat.AbstractChat;
import com.calclab.examplechat.client.chatuiplugin.dialog.OwnPresence.OwnStatus;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChat;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChatListener;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChat;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatListener;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;
import com.calclab.examplechat.client.chatuiplugin.params.MultiChatCreationParam;
import com.calclab.examplechat.client.chatuiplugin.roster.RosterUI;
import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUser;
import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUser.GroupChatUserType;

public class MultiChatPresenter implements MultiChat, GroupChatListener, PairChatListener {
    private static final OwnPresence OFFLINE_OWN_PRESENCE = new OwnPresence(OwnStatus.offline);
    private static final OwnPresence ONLINE_OWN_PRESENCE = new OwnPresence(OwnStatus.online);
    private final HashMap<Chat, AbstractChat> chats;
    private boolean closeAllConfirmed;
    private AbstractChat currentChat;
    private final String currentUserJid;
    private final String currentUserPasswd;
    private final ChatDialogFactory factory;
    private final MultiChatListener listener;
    private final RosterUI rosterUI;
    private final UserChatOptions userChatOptions;
    private MultiChatView view;
    private final Xmpp xmpp;
    private final PresenceManager presenceManager;

    public MultiChatPresenter(final Xmpp xmpp, final I18nTranslationService i18n, final ChatDialogFactory factory,
            final MultiChatCreationParam param, final MultiChatListener listener) {
        this.xmpp = xmpp;
        presenceManager = xmpp.getPresenceManager();
        this.factory = factory;
        this.currentUserJid = param.getUserJid();
        this.currentUserPasswd = param.getUserPassword();
        this.userChatOptions = param.getUserChatOptions();
        this.listener = listener;
        chats = new HashMap<Chat, AbstractChat>();
        rosterUI = factory.createrRosterUI(xmpp, i18n);
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

    public void addRosterItem(final String name, final String jid) {
        Log.info("Adding " + name + "(" + jid + ") to your roster.");
        xmpp.getRosterManager().requestAddItem(jid, name, null);
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
        final GroupChat groupChat = factory.createGroupChat(chat, this, groupChatUserType);
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
        final PairChatUser otherUser = rosterUI.getUserByJid(chat.getOtherURI().getJID().toString());
        if (otherUser == null) {
            Log.info("Message from user not in roster");
            return null;
        }
        final PairChat pairChat = factory.createPairChat(chat, this, currentUserJid, otherUser);
        return (PairChat) finishChatCreation(pairChat, chat.getOtherURI().toString());
    }

    public void destroy() {
        view.destroy();

    }

    public void doAction(final String eventId, final Object param) {
        listener.doAction(eventId, param);
    }

    public void doAfterLogin() {
        view.setLoadingVisible(false);
        view.setAddRosterItemButtonVisible(true);
        view.setOnlineInfo();
        view.setRosterVisible(true);
        if (chats.size() > 0) {
            view.setInputEditable(true);
        }
        Presence currentPresence = presenceManager.getCurrentPresence();
        view.setOwnPresence(currentPresence != null ? new OwnPresence(currentPresence) : ONLINE_OWN_PRESENCE);
    }

    public void doAfterLogout() {
        view.setLoadingVisible(false);
        view.setAddRosterItemButtonVisible(false);
        view.setOfflineInfo();
        view.setRosterVisible(false);
        view.setInputEditable(false);
        rosterUI.clearRoster();
        view.setOwnPresence(OFFLINE_OWN_PRESENCE);
    }

    public void doConnecting() {
        view.setLoadingVisible(true);
    }

    public UserChatOptions getUserChatOptions() {
        return userChatOptions;
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
        view.setOwnPresence(OFFLINE_OWN_PRESENCE);
        createXmppListeners();
        view.attachRoster(rosterUI.getView());
    }

    public void inviteUserToRoom(final String shortName, final String longName) {
    }

    public boolean isCloseAllConfirmed() {
        return closeAllConfirmed;
    }

    public void messageReceived(final Chat chat, final Message message) {
        final AbstractChat abstractChat = getChat(chat);
        if (abstractChat.isGroupChat()) {
            ((GroupChat) abstractChat).addMessage(message.getFrom().toString(), message.getBody());
        } else {
            ((PairChat) abstractChat).addMessage(XmppURI.parse(message.getFrom()), message.getBody());
        }
    }

    public void onActivate(final AbstractChat nextChat) {
        view.setInputText(nextChat.getSavedInput());
        if (nextChat.isGroupChat()) {
            view.setGroupChatUsersPanelVisible(true);
            view.setInviteToGroupChatButtonVisible(true);
            final GroupChatPresenter groupChat = (GroupChatPresenter) nextChat;
            view.setSubject(groupChat.getSubject());
            view.setSubjectEditable(groupChat.getSessionUserType().equals(GroupChatUserType.moderator));
            view.showUserList(groupChat.getUsersListView());
        } else {
            view.setGroupChatUsersPanelVisible(false);
            view.setInviteToGroupChatButtonVisible(false);
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
        view.clearInputText();
    }

    public void onDeactivate(final AbstractChat chat) {
        chat.saveInput(view.getInputText());
        chat.saveOtherProperties();
    }

    public void onMessageAdded(final AbstractChat chat) {
        view.highlightChat(chat);
    }

    public void onStatusSelected(final OwnPresence ownPresence) {
        Show status;
        switch (ownPresence.getStatus()) {
        case online:
        case onlinecustom:
            status = Show.available;
            loginIfnecessary(status, ownPresence.getStatusText());
            break;
        case busy:
        case busycustom:
            status = Show.dnd;
            loginIfnecessary(status, ownPresence.getStatusText());
            break;
        case offline:
            xmpp.logout();
            break;
        }
        view.setOwnPresence(ownPresence);
    }

    private void loginIfnecessary(final Show status, final String statusText) {
        switch (xmpp.getSession().getState()) {
        case disconnected:
            xmpp.login(currentUserJid, currentUserPasswd, status, statusText);
            break;
        case authorized:
        case connecting:
        case connected:
            presenceManager.setOwnPresence(statusText, status);
        case error:
            Log.error("Trying to set status and whe have a internal error");
        }
    }

    public void onSubjectChangedByCurrentUser(final String text) {
        final GroupChat groupChat = (GroupChat) currentChat;
        groupChat.setSubject(text);
        listener.setGroupChatSubject(groupChat.getChat(), text);
        // FIXME callback? erase this:
        view.setSubject(text);
    }

    public void onUserColorChanged(final String color) {
        for (final AbstractChat chat : chats.values()) {
            if (chat.isGroupChat()) {
                // TODO
            } else {
                ((PairChat) chat).setSessionUserColor(color);
            }
        }
        userChatOptions.setColor(color);
        listener.onUserColorChanged(color);
    }

    public void onUserSubscriptionModeChanged(final SubscriptionMode subscriptionMode) {
        xmpp.getRoster().setSubscriptionMode(subscriptionMode);
        userChatOptions.setSubscriptionMode(subscriptionMode);
        listener.onUserSubscriptionModeChanged(subscriptionMode);
    }

    public void show() {
        view.show();
        closeAllConfirmed = false;
    }

    private void checkIsGroupChat(final AbstractChat chat) {
        if (!chat.isGroupChat()) {
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

    private void createXmppListeners() {
        xmpp.getSession().addListener(new SessionListener() {
            public void onStateChanged(final State old, final State current) {
                Log.info("STATE CHANGED: " + current + " - old: " + old);
                switch (current) {
                case connected:
                    doAfterLogin();
                    listener.doAction(EmiteUiPlugin.ON_STATE_CONNECTED, null);
                    break;
                case connecting:
                    doConnecting();
                    break;
                case disconnected:
                    doAfterLogout();
                    listener.doAction(EmiteUiPlugin.ON_STATE_DISCONNECTED, null);
                    break;
                }
            }
        });

        xmpp.getChatManager().addListener(new ChatManagerListener() {
            public void onChatClosed(final Chat chat) {
            }

            public void onChatCreated(final Chat chat) {
                createPairChat(chat);
                chat.addListener(new ChatListener() {
                    public void onMessageReceived(final Chat chat, final Message message) {
                        messageReceived(chat, message);
                    }

                    public void onMessageSent(final Chat chat, final Message message) {
                        messageReceived(chat, message);
                    }
                });
            }
        });

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
        view.setCloseAllOptionEnabled(false);
        view.setSubjectEditable(false);
        setInputEnabled(false);
    }

    private void setInputEnabled(final boolean enabled) {
        view.setSendEnabled(enabled);
        view.setInputEditable(enabled);
        view.setEmoticonButtonEnabled(enabled);
    }

}
