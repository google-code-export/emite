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
package com.calclab.emiteuiplugin.client.dialog;

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
import com.calclab.emite.client.extra.muc.Room;
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
import com.calclab.emiteuiplugin.client.ChatDialogFactory;
import com.calclab.emiteuiplugin.client.EmiteUIPlugin;
import com.calclab.emiteuiplugin.client.UserChatOptions;
import com.calclab.emiteuiplugin.client.chat.ChatUI;
import com.calclab.emiteuiplugin.client.chat.ChatUIListener;
import com.calclab.emiteuiplugin.client.dialog.OwnPresence.OwnStatus;
import com.calclab.emiteuiplugin.client.params.MultiChatCreationParam;
import com.calclab.emiteuiplugin.client.room.RoomUI;
import com.calclab.emiteuiplugin.client.room.RoomUIListener;

public class MultiChatPresenter implements MultiChat {
    private static final OwnPresence OFFLINE_OWN_PRESENCE = new OwnPresence(OwnStatus.offline);
    private static final OwnPresence ONLINE_OWN_PRESENCE = new OwnPresence(OwnStatus.online);
    private final HashMap<Chat, ChatUI> chats;
    private ChatUI currentChat;
    private XmppURI currentUserJid;
    private String currentUserPasswd;
    private final ChatDialogFactory factory;
    private final I18nTranslationService i18n;
    private final MultiChatListener listener;
    private final PresenceManager presenceManager;
    private UserChatOptions userChatOptions;
    private MultiChatView view;
    private final Xmpp xmpp;
    private final String roomHost;

    public MultiChatPresenter(final Xmpp xmpp, final I18nTranslationService i18n, final ChatDialogFactory factory,
            final MultiChatCreationParam param, final MultiChatListener listener) {
        this.xmpp = xmpp;
        this.i18n = i18n;
        this.factory = factory;
        this.listener = listener;
        setUserChatOptions(param.getUserChatOptions());
        roomHost = param.getRoomHost();
        presenceManager = xmpp.getPresenceManager();
        chats = new HashMap<Chat, ChatUI>();
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
        xmpp.getRosterManager().requestAddItem(XmppURI.parse(jid), name, null);
    }

    public void attachIconToBottomBar(final View view) {
        listener.attachToExtPoint(new UIExtensionElement(UIExtensionPoint.CONTENT_BOTTOM_ICONBAR, view));
    }

    public void center() {
        view.center();
    }

    public void closeAllChats(final boolean withConfirmation) {
        if (withConfirmation) {
            view.confirmCloseAll();
        } else {
            onCloseAllConfirmed();
        }
    }

    public ChatUI createChat(final Chat chat) {
        final ChatUI chatUI = chats.get(chat) == null ? factory.createChatUI(chat.getOtherURI().getNode(),
                currentUserJid.getNode(), userChatOptions.getColor(), new ChatUIListener() {
                    public void onActivate(final ChatUI chatUI) {
                        view.setInputText(chatUI.getSavedInput());
                        view.setInviteToGroupChatButtonVisible(false);
                        view.setRoomUserListVisible(false);
                        view.clearSubject();
                        view.setSubjectVisible(false);
                        view.expandRoster();
                        chatUI.unHighLightChatTitle();
                        view.focusInput();
                        currentChat = chatUI;
                    }

                    public void onCloseConfirmed(final ChatUI chatUI) {
                        doAfterCloseConfirmed(chat, chatUI);
                    }

                    public void onCurrentUserSend(final String message) {
                        chat.send(message);
                    }

                    public void onDeactivate(final ChatUI chatUI) {
                        chatUI.saveInput(view.getInputText());
                        chatUI.unHighLightChatTitle();
                    }

                    public void onMessageAdded(final ChatUI chatUI) {
                        chatUI.highLightChatTitle();
                    }
                }) : chats.get(chat);
        finishChatCreation(chat, chatUI);
        return chatUI;
    }

    public RoomUI createRoom(final Chat chat, final String userAlias) {
        final RoomUI roomUI = (RoomUI) (chats.get(chat) == null ? factory.createRoomUI(chat.getOtherURI().getNode(),
                currentUserJid.getNode(), userChatOptions.getColor(), i18n, new RoomUIListener() {
                    // FIXME: some code are duplicated with ChatUI Listener
                    // (make an
                    // abstract listener)
                    public void onActivate(final ChatUI chatUI) {
                        final RoomUI roomUI = (RoomUI) chatUI;
                        view.setInputText(roomUI.getSavedInput());
                        view.setInviteToGroupChatButtonVisible(true);
                        view.setSubject(roomUI.getSubject());
                        view.setSubjectVisible(true);
                        view.setSubjectEditable(roomUI.isSubjectEditable());
                        view.setRoomUserListVisible(true);
                        roomUI.setUserListVisible(true);
                        chatUI.unHighLightChatTitle();
                        view.focusInput();
                        currentChat = chatUI;
                    }

                    public void onCloseConfirmed(final ChatUI chatUI) {
                        doAfterCloseConfirmed(chat, chatUI);
                    }

                    public void onCreated(final ChatUI chatUI) {
                        view.attachRoomUserList(((RoomUI) chatUI).getUserListView());
                    }

                    public void onCurrentUserSend(final String message) {
                        chat.send(message);
                    }

                    public void onDeactivate(final ChatUI chatUI) {
                        chatUI.saveInput(view.getInputText());
                        ((RoomUI) chatUI).setUserListVisible(false);
                        chatUI.unHighLightChatTitle();
                    }

                    public void onInviteUserRequested(final String userJid, final String reasonText) {
                        ((Room) chat).inviteUser(userJid, reasonText);
                    }

                    public void onMessageAdded(final ChatUI chatUI) {
                        chatUI.highLightChatTitle();
                    }

                    public void onModifySubjectRequested(final String newSubject) {
                        ((Room) chat).setSubject(newSubject);
                    }

                    public void setSubjectEditable(final boolean editable) {
                        view.setSubjectEditable(editable);
                    }
                }) : chats.get(chat));

        finishChatCreation(chat, roomUI);

        return roomUI;
    }

    public void destroy() {
        view.destroy();
    }

    public void doAction(final String eventId, final Object param) {
        listener.doAction(eventId, param);
    }

    public String getRoomHost() {
        return roomHost;
    }

    public void hide() {
        view.hide();
    }

    public void init(final MultiChatView view) {
        this.view = view;
        reset();
        createXmppListeners();
    }

    public void inviteUserToRoom(final String userJid, final String reasonText) {
        final RoomUI roomUI = (RoomUI) currentChat;
        roomUI.onInviteUserRequested(userJid, reasonText);
    }

    public void joinRoom(final String roomName, final String serverName) {
        xmpp.getRoomManager().openChat(XmppURI.parse(roomName + "@" + serverName + "/" + currentUserJid.getNode()));
    }

    public void onModifySubjectRequested(final String newSubject) {
        final RoomUI roomUI = (RoomUI) currentChat;
        roomUI.onModifySubjectRequested(newSubject);
        view.clearSubject();
    }

    public void setOwnPresence(final OwnPresence ownPresence) {
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

    public void setUserChatOptions(final UserChatOptions userChatOptions) {
        this.userChatOptions = userChatOptions;
        this.currentUserJid = XmppURI.parse(userChatOptions.getUserJid());
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
        view.clearRoster();
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

    void messageReceivedInRoom(final Chat chat, final Message message) {
        final RoomUI roomUI = (RoomUI) getChat(chat);
        final XmppURI fromURI = message.getFromURI();
        if (fromURI.getResource() == null && fromURI.getNode().equals(chat.getOtherURI().getNode())) {
            // Info messsage from room
            roomUI.addInfoMessage(message.getBody());
        } else {
            roomUI.addMesage(fromURI.getResource(), message.getBody());
        }
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
                case notAuthorized:
                    view.showAlert(i18n.t("Error in authentication"));
                    break;
                case connected:
                    doAfterLogin();
                    listener.doAction(EmiteUIPlugin.ON_STATE_CONNECTED, null);
                    break;
                case connecting:
                    doConnecting();
                    break;
                case disconnected:
                    doAfterLogout();
                    listener.doAction(EmiteUIPlugin.ON_STATE_DISCONNECTED, null);
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
                final RoomUI roomUI = createRoom(room, currentUserJid.getNode());
                room.addListener(new RoomListener() {
                    public void onMessageReceived(final Chat chat, final Message message) {
                        messageReceivedInRoom(chat, message);
                    }

                    public void onMessageSent(final Chat chat, final Message message) {
                        // messageReceived(chat, message);
                    }

                    public void onOccupantModified(final Occupant occupant) {
                        roomUI.onOccupantModified(occupant);
                    }

                    public void onOccupantsChanged(final Collection<Occupant> occupants) {
                        roomUI.onOccupantsChanged(occupants);
                    }

                    public void onSubjectSet(final String nick, final String newSubject) {
                        roomUI.setSubject(newSubject);
                        roomUI.addInfoMessage(i18n.t("[%s] as changed the subject to: ", nick) + newSubject);
                        if (currentChat.equals(roomUI)) {
                            view.setSubject(newSubject);
                        }
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

    private void finishChatCreation(final Chat chat, final ChatUI chatUI) {
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
            final String resource = "emiteui-" + new Date().getTime();
            xmpp.login(new XmppURI(currentUserJid.getNode(), currentUserJid.getHost(), resource), currentUserPasswd,
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
        view.setInfoPanelVisible(true);
        view.setRoomUserListVisible(false);
        view.setInviteToGroupChatButtonVisible(false);
        view.setSendEnabled(false);
        view.setInputEditable(false);
        view.setEmoticonButtonEnabled(false);
        view.clearSubject();
    }

    private void setInputEnabled(final boolean enabled) {
        view.setSendEnabled(enabled);
        view.setInputEditable(enabled);
        view.setEmoticonButtonEnabled(enabled);
    }

}
