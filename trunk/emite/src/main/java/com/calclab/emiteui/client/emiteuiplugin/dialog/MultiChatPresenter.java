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
package com.calclab.emiteui.client.emiteuiplugin.dialog;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

import org.ourproject.kune.platf.client.View;
import org.ourproject.kune.platf.client.extend.UIExtensionElement;
import org.ourproject.kune.platf.client.extend.UIExtensionPoint;
import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.extra.muc.Occupant;
import com.calclab.emite.client.extra.muc.RoomListener;
import com.calclab.emite.client.extra.muc.RoomManager;
import com.calclab.emite.client.extra.muc.RoomManagerListener;
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
import com.calclab.emiteui.client.emiteuiplugin.ChatDialogFactory;
import com.calclab.emiteui.client.emiteuiplugin.EmiteUiPlugin;
import com.calclab.emiteui.client.emiteuiplugin.UserChatOptions;
import com.calclab.emiteui.client.emiteuiplugin.chat.ChatUI;
import com.calclab.emiteui.client.emiteuiplugin.chat.ChatUIListener;
import com.calclab.emiteui.client.emiteuiplugin.dialog.OwnPresence.OwnStatus;
import com.calclab.emiteui.client.emiteuiplugin.params.MultiChatCreationParam;
import com.calclab.emiteui.client.emiteuiplugin.room.RoomUI;
import com.calclab.emiteui.client.emiteuiplugin.roster.RosterUI;
import com.calclab.emiteui.client.emiteuiplugin.users.RoomUserUI.RoomUserType;
import com.calclab.emiteui.client.emiteuiplugin.utils.XmppJID;

public class MultiChatPresenter implements MultiChat {
    private static final OwnPresence OFFLINE_OWN_PRESENCE = new OwnPresence(OwnStatus.offline);
    private static final OwnPresence ONLINE_OWN_PRESENCE = new OwnPresence(OwnStatus.online);
    private final HashMap<Chat, ChatUI> chats;
    private ChatUI currentChat;
    private XmppJID currentUserJid;
    private String currentUserPasswd;
    private final ChatDialogFactory factory;
    private final I18nTranslationService i18n;
    private final MultiChatListener listener;
    private final PresenceManager presenceManager;
    private final RosterUI rosterUI;
    private UserChatOptions userChatOptions;
    private MultiChatView view;
    private final Xmpp xmpp;

    public MultiChatPresenter(final Xmpp xmpp, final I18nTranslationService i18n, final ChatDialogFactory factory,
            final MultiChatCreationParam param, final MultiChatListener listener) {
        this.xmpp = xmpp;
        this.i18n = i18n;
        presenceManager = xmpp.getPresenceManager();
        this.factory = factory;
        setUserChatOptions(param.getUserChatOptions());
        this.listener = listener;
        chats = new HashMap<Chat, ChatUI>();
        rosterUI = factory.createrRosterUI(xmpp, i18n);
    }

    public void activateChat(final Chat chat) {
        final ChatUI abstractChat = getChat(chat);
        activateChat(abstractChat);
    }

    public void activateChat(final ChatUI chatUI) {
        view.activateChat(chatUI);
    }

    public void addBuddy(final String shortName, final String longName) {
    }

    public void addRosterItem(final String name, final String jid) {
        Log.info("Adding " + name + "(" + jid + ") to your roster.");
        xmpp.getRosterManager().requestAddItem(jid, name, null);
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

    public ChatUI createChat(final Chat chat) {
        final ChatUI chatUI = chats.get(chat) == null ? factory.createChatUI(currentUserJid.getNode(), userChatOptions
                .getColor(), new ChatUIListener() {
            public void onActivate(ChatUI chatUI) {
                view.setInputText(chatUI.getSavedInput());
                view.setInviteToGroupChatButtonVisible(false);
                view.setRoomUserListVisible(false);
                view.clearSubject();
                view.setSubjectEditable(false);
                view.expandRoster();
                view.focusInput();
                currentChat = chatUI;
            }

            public void onCloseConfirmed(ChatUI chatUI) {
                doAfterCloseConfirmed(chat, chatUI);
            }

            public void onCurrentUserSend(String message) {
                chat.send(message);
            }

            public void onDeactivate(ChatUI chatUI) {
                chatUI.saveInput(view.getInputText());
            }

            public void onMessageAdded(ChatUI chatUI) {
                view.highlightChat(chatUI);
            }
        }) : chats.get(chat);
        finishChatCreation(chat, chatUI, chat.getOtherURI().getNode());
        return chatUI;
    }

    public RoomUI createRoom(final Chat chat, final String userAlias, final RoomUserType roomUserType) {
        final RoomUI roomUI = (RoomUI) (chats.get(chat) == null ? factory.createRoomUI(currentUserJid.getNode(),
                userChatOptions.getColor(), i18n, new ChatUIListener() {
                    public void onActivate(ChatUI chatUI) {
                        RoomUI roomUI = (RoomUI) chatUI;
                        view.setInputText(roomUI.getSavedInput());
                        view.setInviteToGroupChatButtonVisible(true);
                        view.setSubject(roomUI.getSubject());
                        view.setSubjectEditable(false);
                        view.setSubjectEditable(roomUserType.equals(RoomUserType.moderator));
                        view.setRoomUserListVisible(true);
                        view.attachRoomUserList(((RoomUI) chatUI).getUserListView());
                        view.focusInput();
                        currentChat = chatUI;
                    }

                    public void onCloseConfirmed(ChatUI chatUI) {
                        doAfterCloseConfirmed(chat, chatUI);
                    }

                    public void onCurrentUserSend(String message) {
                        chat.send(message);
                    }

                    public void onDeactivate(ChatUI chatUI) {
                        chatUI.saveInput(view.getInputText());
                        view.dettachRoomUserList(((RoomUI) chatUI).getUserListView());
                    }

                    public void onMessageAdded(ChatUI chatUI) {
                        view.highlightChat(chatUI);
                    }
                }) : chats.get(chat));
        // view.setSubject("");
        finishChatCreation(chat, roomUI, chat.getOtherURI().getNode());
        return roomUI;
    }

    public void destroy() {
        view.destroy();
    }

    public void doAction(final String eventId, final Object param) {
        listener.doAction(eventId, param);
    }

    public void groupChatSubjectChanged(final Chat groupChat, final String newSubject) {
        final ChatUI chatUI = getChat(groupChat);
        ((RoomUI) chatUI).setSubject(newSubject);
        view.setSubject(newSubject);
    }

    public void init(final MultiChatView view) {
        this.view = view;
        reset();
        view.setOwnPresence(OFFLINE_OWN_PRESENCE);
        createXmppListeners();
        view.attachRoster(rosterUI.getView());
    }

    public void joinRoom(final String roomName, final String serverName) {
        xmpp.getRoomManager().openChat(XmppURI.parse(roomName + "@" + serverName + "/" + currentUserJid.getNode()));
    }

    public void onSubjectChangedByCurrentUser(final String text) {
        final RoomUI roomUI = (RoomUI) currentChat;
        roomUI.setSubject(text);
        // FIXME callback? erase this:
        view.setSubject(text);
    }

    public void setUserChatOptions(final UserChatOptions userChatOptions) {
        this.userChatOptions = userChatOptions;
        this.currentUserJid = new XmppJID(userChatOptions.getUserJid());
        this.currentUserPasswd = userChatOptions.getUserPassword();

    }

    public void show() {
        view.show();
    }

    protected void onCloseAllConfirmed() {
        for (final ChatUI chatUI : chats.values()) {
            closeChatUI(chatUI);
            view.removeChat(chatUI);
        }
    }

    protected void onCurrentUserSend(final String message) {
        currentChat.onCurrentUserSend(message);
        view.clearInputText();
    }

    protected void onMessageAdded(final ChatUI chat) {
        view.highlightChat(chat);
    }

    protected void onStatusSelected(final OwnPresence ownPresence) {
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

    protected void onUserColorChanged(final String color) {
        for (final ChatUI chat : chats.values()) {
            chat.setUserColor(currentUserJid.getNode(), color);
        }
        userChatOptions.setColor(color);
        listener.onUserColorChanged(color);
    }

    protected void onUserSubscriptionModeChanged(final SubscriptionMode subscriptionMode) {
        xmpp.getRoster().setSubscriptionMode(subscriptionMode);
        userChatOptions.setSubscriptionMode(subscriptionMode);
        listener.onUserSubscriptionModeChanged(subscriptionMode);
    }

    void closeChatUI(final ChatUI chatUI) {
        chatUI.setCloseConfirmed(true);
        chatUI.onCloseCloseConfirmed();
    }

    void doAfterCloseConfirmed(final Chat chat, final ChatUI chatUI) {
        xmpp.getChatManager().close(chat);
        chats.remove(chat);
        chatUI.destroy();
        checkNoChats();
    }

    void doAfterLogout() {
        view.setLoadingVisible(false);
        view.setAddRosterItemButtonVisible(false);
        view.setJoinRoomEnabled(false);
        view.setRosterVisible(false);
        view.setOfflineInfo();
        view.setInputEditable(false);
        rosterUI.clearRoster();
        view.setOwnPresence(OFFLINE_OWN_PRESENCE);
    }

    void doConnecting() {
        view.setLoadingVisible(true);
    }

    UserChatOptions getUserChatOptions() {
        return userChatOptions;
    }

    void messageReceived(final Chat chat, final Message message) {
        final ChatUI chatUI = getChat(chat);
        chatUI.addMesage(message.getFromURI().getNode(), message.getBody());
    }

    private void checkNoChats() {
        if (chats.size() == 0) {
            reset();
        }
    }

    private void checkThereAreChats() {
        if (chats.size() >= 1) {
            view.setCloseAllOptionEnabled(true);
            setInputEnabled(true);
            view.setInfoPanelVisible(false);
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
                createChat(chat);
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

        final RoomManager roomManager = xmpp.getRoomManager();
        roomManager.addListener(new RoomManagerListener() {
            public void onChatClosed(final Chat chat) {
            }

            public void onChatCreated(final Chat room) {
                final RoomUI roomUI = createRoom(room, currentUserJid.getNode(), RoomUserType.participant);
                room.addListener(new RoomListener() {
                    public void onMessageReceived(final Chat chat, final Message message) {
                        messageReceived(chat, message);
                    }

                    public void onMessageSent(final Chat chat, final Message message) {
                        messageReceived(chat, message);
                    }

                    public void onOccupantModified(final Occupant occupant) {
                    }

                    public void onOccupantsChanged(final Collection<Occupant> users) {
                        roomUI.setUsers(users);
                    }
                });
            }
        });

    }

    private void doAfterLogin() {
        view.setLoadingVisible(false);
        view.setAddRosterItemButtonVisible(true);
        view.setJoinRoomEnabled(true);
        view.setOnlineInfo();
        view.setRosterVisible(true);
        if (chats.size() > 0) {
            view.setInputEditable(true);
        } else {
            view.setInputEditable(false);
        }
        final Presence currentPresence = presenceManager.getCurrentPresence();
        view.setOwnPresence(currentPresence != null ? new OwnPresence(currentPresence) : ONLINE_OWN_PRESENCE);
    }

    private void finishChatCreation(final Chat chat, final ChatUI chatUI, final String chatTitle) {
        chatUI.setChatTitle(chatTitle);
        view.addChat(chatUI);
        currentChat = chatUI;
        chats.put(chat, chatUI);
        checkThereAreChats();
    }

    private ChatUI getChat(final Chat chat) {
        final ChatUI chatUI = chats.get(chat);
        if (chatUI == null) {
            final String error = "Unexpected chatId '" + chat.getID().toString() + "'";
            Log.error(error);
            throw new RuntimeException(error);
        }
        return chatUI;
    }

    private void loginIfnecessary(final Show status, final String statusText) {
        switch (xmpp.getSession().getState()) {
        case disconnected:
            final String resource = "emite-ui" + new Date().getTime();
            xmpp.login(new XmppURI(currentUserJid.toString(), currentUserJid.getHost(), resource), currentUserPasswd,
                    status, statusText);
            break;
        case authorized:
        case connecting:
        case connected:
            presenceManager.setOwnPresence(statusText, status);
            break;
        case error:
            Log.error("Trying to set status and whe have a internal error");
        }
    }

    private void reset() {
        currentChat = null;
        view.setCloseAllOptionEnabled(false);
        view.setSubjectEditable(false);
        setInputEnabled(false);
        view.setInfoPanelVisible(true);
        view.setRoomUserListVisible(false);
        view.setInviteToGroupChatButtonVisible(false);
    }

    private void setInputEnabled(final boolean enabled) {
        view.setSendEnabled(enabled);
        view.setInputEditable(enabled);
        view.setEmoticonButtonEnabled(enabled);
    }

}
