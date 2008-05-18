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

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.jid;
import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;

import java.util.Collection;
import java.util.HashMap;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.extra.avatar.AvatarModule;
import com.calclab.emite.client.extra.muc.Occupant;
import com.calclab.emite.client.extra.muc.Room;
import com.calclab.emite.client.extra.muc.RoomListener;
import com.calclab.emite.client.extra.muc.RoomManager;
import com.calclab.emite.client.extra.muc.RoomManagerListener;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.chat.ChatListener;
import com.calclab.emite.client.im.chat.ChatManagerListener;
import com.calclab.emite.client.im.presence.PresenceManager;
import com.calclab.emite.client.im.roster.RosterManager.SubscriptionMode;
import com.calclab.emite.client.xmpp.session.SessionListener;
import com.calclab.emite.client.xmpp.session.Session.State;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.Presence.Show;
import com.calclab.emiteuiplugin.client.EmiteUIFactory;
import com.calclab.emiteuiplugin.client.UserChatOptions;
import com.calclab.emiteuiplugin.client.chat.ChatUI;
import com.calclab.emiteuiplugin.client.chat.ChatUIListener;
import com.calclab.emiteuiplugin.client.params.MultiChatCreationParam;
import com.calclab.emiteuiplugin.client.room.JoinRoomPanel;
import com.calclab.emiteuiplugin.client.room.RoomUI;
import com.calclab.emiteuiplugin.client.room.RoomUIListener;
import com.calclab.emiteuiplugin.client.roster.RosterPresenter;
import com.calclab.emiteuiplugin.client.status.OwnPresence;
import com.calclab.emiteuiplugin.client.status.StatusPanel;
import com.calclab.emiteuiplugin.client.status.StatusPanelListener;
import com.calclab.emiteuiplugin.client.status.OwnPresence.OwnStatus;

public class MultiChatPresenter {
    private static final OwnPresence OFFLINE_OWN_PRESENCE = new OwnPresence(OwnStatus.offline);
    private static final OwnPresence ONLINE_OWN_PRESENCE = new OwnPresence(OwnStatus.online);

    private final HashMap<Chat, ChatUI> chats;
    private ChatUI currentChat;
    private XmppURI currentUserJid;
    private String currentUserPasswd;
    private final EmiteUIFactory factory;
    private final I18nTranslationService i18n;
    private final MultiChatListener listener;
    private final PresenceManager presenceManager;
    private UserChatOptions userChatOptions;
    private MultiChatPanel view;
    private final Xmpp xmpp;
    private final String roomHost;
    private final RosterPresenter roster;

    public MultiChatPresenter(final Xmpp xmpp, final I18nTranslationService i18n, final EmiteUIFactory factory,
            final MultiChatCreationParam param, final MultiChatListener listener, final RosterPresenter roster) {
        this.xmpp = xmpp;
        this.i18n = i18n;
        this.factory = factory;
        this.listener = listener;
        this.roster = roster;
        setUserChatOptions(param.getUserChatOptions());
        roomHost = param.getRoomHost();
        presenceManager = xmpp.getPresenceManager();
        chats = new HashMap<Chat, ChatUI>();
    }

    public void addBuddy(final String shortName, final String longName) {
    }

    public void addRosterItem(final String name, final String jid) {
        Log.info("Adding " + name + "(" + jid + ") to your roster.");
        xmpp.getRosterManager().requestAddItem(uri(jid), name, null);
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
        final ChatUI chatUI = chats.get(chat) == null ? factory.createChatUI(chat.getOtherURI(), currentUserJid
                .getNode(), userChatOptions.getColor(), new ChatUIListener() {

            public void onActivate(final ChatUI chatUI) {
                view.setInputText(chatUI.getSavedInput());
                view.setBottomChatNotification(chatUI.getSavedChatNotification());
                currentChat = chatUI;
                view.focusInput();
            }

            public void onChatNotificationClear() {
                view.clearBottomChatNotification();
            }

            public void onClose(final ChatUI chatUI) {
                xmpp.getChatManager().close(chat);
                doAfterChatClosed(chat);
            }

            public void onCurrentUserSend(final String message) {
                chat.send(message);
            }

            public void onDeactivate(final ChatUI chatUI) {
                chatUI.saveInput(view.getInputText());
            }

            public void onHighLight(final ChatUI chatUI) {
                view.highLight();
                listener.onConversationUnnatended(chatUI.getChatTitle());
            }

            public void onMessageAdded(final ChatUI chatUI) {
            }

            public void onNewChatNotification(String message) {
                view.setBottomChatNotification(message);
            }

            public void onUnHighLight(final ChatUI chatUI) {
                view.unHighLight();
                listener.onConversationAttended(chatUI.getChatTitle());
            }

            public void onUserDrop(final ChatUI chatUI, final XmppURI userURI) {
                if (!chat.getOtherURI().equals(userURI)) {
                    joinChat(userURI);
                }
            }
        }) : chats.get(chat);
        chatUI.setChatState(xmpp.getChatStateManager().getChatState(chat));
        finishChatCreation(chat, chatUI);
        return chatUI;
    }

    public RoomUI createRoom(final Chat chat, final String userAlias) {
        final RoomUI roomUI = (RoomUI) (chats.get(chat) == null ? factory.createRoomUI(chat.getOtherURI(),
                currentUserJid.getNode(), userChatOptions.getColor(), i18n, new RoomUIListener() {
                    // FIXME: some code are duplicated with ChatUI Listener
                    // (make an
                    // abstract listener)
                    public void onActivate(final ChatUI chatUI) {
                        final RoomUI roomUI = (RoomUI) chatUI;
                        view.setInputText(roomUI.getSavedInput());
                        view.setBottomChatNotification(chatUI.getSavedChatNotification());
                        currentChat = chatUI;
                        view.focusInput();
                    }

                    public void onChatNotificationClear() {
                        view.clearBottomChatNotification();
                    }

                    public void onClose(final ChatUI chatUI) {
                        xmpp.getRoomManager().close(chat);
                        doAfterChatClosed(chat);
                    }

                    public void onCreated(final ChatUI chatUI) {

                    }

                    public void onCurrentUserSend(final String message) {
                        chat.send(message);
                    }

                    public void onDeactivate(final ChatUI chatUI) {
                        chatUI.saveInput(view.getInputText());
                    }

                    public void onHighLight(final ChatUI chatUI) {
                        view.highLight();
                        listener.onConversationUnnatended(chatUI.getChatTitle());
                    }

                    public void onInviteUserRequested(final XmppURI userJid, final String reasonText) {
                        ((Room) chat).sendInvitationTo(userJid.toString(), reasonText);
                        view.setBottomInfoMessage(i18n.t("Invitation sended"));
                    }

                    public void onMessageAdded(final ChatUI chatUI) {
                    }

                    public void onModifySubjectRequested(final String newSubject) {
                        ((Room) chat).setSubject(newSubject);
                    }

                    public void onNewChatNotification(String message) {
                        view.setBottomChatNotification(message);
                    }

                    public void onUnHighLight(final ChatUI chatUI) {
                        view.unHighLight();
                        listener.onConversationAttended(chatUI.getChatTitle());
                    }

                    public void onUserDrop(final ChatUI chatUI, final XmppURI userURI) {
                        ((RoomUI) chatUI).askInvitation(userURI);
                    }

                }) : chats.get(chat));

        finishChatCreation(chat, roomUI);

        return roomUI;
    }

    public void destroy() {
        view.destroy();
    }

    public String getRoomHost() {
        return roomHost;
    }

    public void hide() {
        view.hide();
    }

    public void init(final MultiChatPanel view) {
        this.view = view;
        reset();
        view.setRosterVisible(false);
        view.setOfflineInfo();
        createXmppListeners();
    }

    public void initStatusPanel(final StatusPanel status) {
        status.addListener(new StatusPanelListener() {
            private JoinRoomPanel joinRoomPanel;

            public void onCloseAllConfirmed() {
                MultiChatPresenter.this.onCloseAllConfirmed();
            }

            public void onJoinRoom() {
                if (joinRoomPanel == null) {
                    joinRoomPanel = new JoinRoomPanel(i18n, MultiChatPresenter.this);
                }
                joinRoomPanel.show();

            }

            public void onUserColorChanged(final String color) {
                MultiChatPresenter.this.onUserColorChanged(color);
            }

            public void onUserSubscriptionModeChanged(final SubscriptionMode mode) {
                MultiChatPresenter.this.onUserSubscriptionModeChanged(mode);
            }

            public void setOwnPresence(final OwnPresence ownPresence) {
                MultiChatPresenter.this.setOwnPresence(ownPresence);
            }
        });
        status.setSubscritionMode(getUserChatOptions().getSubscriptionMode());
    }

    public void joinChat(final XmppURI userURI) {
        xmpp.getChatManager().openChat(userURI);
    }

    public void joinRoom(final String roomName, final String serverName) {
        xmpp.getRoomManager().openChat(uri(roomName + "@" + serverName + "/" + currentUserJid.getNode()));
    }

    public void onComposing() {
        // FIXME: try to remove input listener or a way to no affect performance
        // currently gwt-ext only have a input.removeAllListeners ... and tryint
        // to re-add focus listener don't works
        currentChat.onComposing();
    }

    public void onInputFocus() {
        currentChat.onInputFocus();
        view.addInputComposingListener();
    }

    public void onInputUnFocus() {
        currentChat.onInputUnFocus();
    }

    public void onModifySubjectRequested(final String newSubject) {
        final RoomUI roomUI = (RoomUI) currentChat;
        roomUI.onModifySubjectRequested(newSubject);
    }

    public void onUserDropped(final XmppURI userURI) {
        if (currentChat != null) {
            currentChat.onUserDrop(userURI);
        } else {
            joinChat(userURI);
        }
    }

    public void setOwnPresence(final OwnPresence ownPresence) {
        Show show;
        switch (ownPresence.getStatus()) {
        case online:
        case onlinecustom:
            // Show notSpecified, with/without statusText is like "online"
            show = Show.notSpecified;
            loginIfnecessary(show, ownPresence.getStatusText());
            break;
        case busy:
        case busycustom:
            show = Show.dnd;
            loginIfnecessary(show, ownPresence.getStatusText());
            break;
        case offline:
            xmpp.logout();
            break;
        }
        view.setOwnPresence(ownPresence);
    }

    public void setUserChatOptions(final UserChatOptions userChatOptions) {
        this.userChatOptions = userChatOptions;
        this.currentUserJid = jid(userChatOptions.getUserJid());
        this.currentUserPasswd = userChatOptions.getUserPassword();
    }

    public void setVCardAvatar(final String photoBinary) {
        AvatarModule.getAvatarManager(xmpp).setVCardAvatar(photoBinary);
    }

    public void show() {
        view.show();
    }

    public void showUnavailableRosterItems(final boolean show) {
        roster.showUnavailableRosterItems(show);
    }

    protected void onCloseAllConfirmed() {
        for (final ChatUI chatUI : chats.values()) {
            closeChatUI(chatUI);
            view.removeChat(chatUI);
        }
    }

    protected void onCurrentUserSendWithButton(final String message) {
        onCurrentUserSend(message);
        view.focusInput();
    }

    protected void onCurrentUserSendWithEnter(final String message) {
        onCurrentUserSend(message);
    }

    protected void onUserColorChanged(final String color) {
        for (final ChatUI chat : chats.values()) {
            chat.setUserColor(currentUserJid.getNode(), color);
        }
        userChatOptions.setColor(color);
        listener.onUserColorChanged(color);
    }

    protected void onUserSubscriptionModeChanged(final SubscriptionMode subscriptionMode) {
        xmpp.getRosterManager().setSubscriptionMode(subscriptionMode);
        userChatOptions.setSubscriptionMode(subscriptionMode);
        listener.onUserSubscriptionModeChanged(subscriptionMode);
    }

    void closeChatUI(final ChatUI chatUI) {
        chatUI.onClose();
    }

    void doAfterLogout() {
        view.setOfflineTitle();
        view.setLoadingVisible(false);
        view.setAddRosterItemButtonVisible(false);
        view.setShowUnavailableItemsButtonVisible(false);
        view.setJoinRoomEnabled(false);
        view.setRosterVisible(false);
        view.setOfflineInfo();
        view.setInputEditable(false);
        view.setOwnPresence(OFFLINE_OWN_PRESENCE);
        roster.clearRoster();
    }

    void doConnecting() {
        view.setLoadingVisible(true);
    }

    UserChatOptions getUserChatOptions() {
        return userChatOptions;
    }

    void messageReceived(final Chat chat, final Message message) {
        final ChatUI chatUI = getChat(chat);
        final String node = message.getFromURI().getNode();
        chatUI.addMessage(node != null ? node : message.getFromURI().toString(), message.getBody());
    }

    void messageReceivedInRoom(final Chat chat, final Message message) {
        final RoomUI roomUI = (RoomUI) getChat(chat);
        final XmppURI fromURI = message.getFromURI();
        if (fromURI.getResource() == null && fromURI.getNode().equals(chat.getOtherURI().getNode())) {
            // Info messsage from room
            roomUI.addInfoMessage(message.getBody());
        } else {
            roomUI.addMessage(fromURI.getResource(), message.getBody());
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
                    view.showAlert(i18n.t("Error in authentication. Wrong user jabber id or password."));
                    break;
                case connected:
                    doAfterLogin();
                    break;
                case connecting:
                    doConnecting();
                    break;
                case disconnected:
                    doAfterLogout();
                    break;
                }
            }
        });

        xmpp.getChatManager().addListener(new ChatManagerListener() {
            public void onChatClosed(final Chat chat) {
                doAfterChatClosed(chat);
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
                doAfterChatClosed(chat);
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
                        Log.info("Room occupant changed (" + occupant.getUri() + ")");
                        roomUI.onOccupantModified(occupant);
                    }

                    public void onOccupantsChanged(final Collection<Occupant> occupants) {
                        roomUI.onOccupantsChanged(occupants);
                    }

                    public void onSubjectChanged(final String nick, final String newSubject) {
                        roomUI.setSubject(newSubject);
                        roomUI.addInfoMessage(i18n.t("[%s] has changed the subject to: ", nick) + newSubject);
                    }
                });
            }

            public void onInvitationReceived(final XmppURI invitor, final XmppURI roomURI, final String reason) {
                view.highLight();
                view.roomJoinConfirm(invitor, roomURI, reason);
            }
        });

    }

    private void doAfterChatClosed(final Chat chat) {
        ChatUI chatUI = chats.get(chat);
        if (chatUI != null) {
            chats.remove(chat);
            chatUI.destroy();
        }
        checkNoChats();
    }

    private void doAfterLogin() {
        view.setTitleConectedAs(currentUserJid);
        view.setLoadingVisible(false);
        view.setAddRosterItemButtonVisible(true);
        view.setShowUnavailableItemsButtonVisible(true);
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
        chatUI.highLightChatTitle();
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
            xmpp.login(new XmppURI(currentUserJid.getNode(), currentUserJid.getHost(), userChatOptions.getResource()),
                    currentUserPasswd, status, statusText);
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

    private void onCurrentUserSend(final String message) {
        final boolean isEmpty = message == null || message.equals("");
        if (!isEmpty) {
            view.clearInputText();
            currentChat.onCurrentUserSend(message);
        }
    }

    private void reset() {
        currentChat = null;
        view.setCloseAllOptionEnabled(false);
        view.setInfoPanelVisible(true);
        view.setSendEnabled(false);
        view.setInputEditable(false);
        view.setEmoticonButtonEnabled(false);
        view.clearBottomChatNotification();
    }

    private void setInputEnabled(final boolean enabled) {
        view.setSendEnabled(enabled);
        view.setInputEditable(enabled);
        view.setEmoticonButtonEnabled(enabled);
    }

}
