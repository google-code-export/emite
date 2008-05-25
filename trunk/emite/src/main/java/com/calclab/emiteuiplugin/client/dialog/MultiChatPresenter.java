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

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.chat.ChatListener;
import com.calclab.emite.client.im.chat.ChatManagerListener;
import com.calclab.emite.client.im.presence.PresenceManager;
import com.calclab.emite.client.im.roster.RosterManager.SubscriptionMode;
import com.calclab.emite.client.xep.avatar.AvatarModule;
import com.calclab.emite.client.xep.chatstate.ChatState;
import com.calclab.emite.client.xep.muc.Occupant;
import com.calclab.emite.client.xep.muc.Room;
import com.calclab.emite.client.xep.muc.RoomListener;
import com.calclab.emite.client.xep.muc.RoomManager;
import com.calclab.emite.client.xep.muc.RoomManagerListener;
import com.calclab.emite.client.xmpp.session.SessionListener;
import com.calclab.emite.client.xmpp.session.Session.State;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.Presence.Show;
import com.calclab.emiteuiplugin.client.EmiteUIFactory;
import com.calclab.emiteuiplugin.client.UserChatOptions;
import com.calclab.emiteuiplugin.client.chat.ChatNotification;
import com.calclab.emiteuiplugin.client.chat.ChatUI;
import com.calclab.emiteuiplugin.client.chat.ChatUIListener;
import com.calclab.emiteuiplugin.client.chat.ChatUIStartedByMe;
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
    private int openedChats;

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
        openedChats = 0;
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

    public void closeChatAfterConfirmed(final Chat chat) {
        ChatUI chatUI = getChatUI(chat);
        if (chatUI != null && chatUI.isDocked()) {
            closeChatUI(chatUI);
            view.removeChat(chatUI);
        }
    }

    public ChatUI createChat(final Chat chat) {
        final ChatUI chatUI = getChatUI(chat) == null ? factory.createChatUI(chat.getOtherURI(), currentUserJid
                .getNode(), userChatOptions.getColor(), chat.getData(ChatState.class), new ChatUIListener() {

            public void onActivate(final ChatUI chatUI) {
                view.setInputText(chatUI.getSavedInput());
                view.setBottomChatNotification(chatUI.getSavedChatNotification());
                currentChat = chatUI;
                view.focusInput();
            }

            public void onChatNotificationClear(final ChatUI chatUI) {
                if (chatUI.equals(currentChat)) {
                    view.clearBottomChatNotification();
                }
            }

            public void onClose(final ChatUI chatUI) {
                xmpp.getChatManager().close(chat);
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

            public void onNewChatNotification(final ChatUI chatUI, final ChatNotification chatNotification) {
                if (chatUI.equals(currentChat)) {
                    view.setBottomChatNotification(chatNotification);
                }
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
        }) : getChatUI(chat);
        finishChatCreation(chat, chatUI);
        return chatUI;
    }

    public RoomUI createRoom(final Chat chat, final String userAlias) {
        ChatUI chatUI = getChatUI(chat);
        final RoomUI roomUI = (RoomUI) (chatUI == null ? factory.createRoomUI(chat.getOtherURI(), currentUserJid
                .getNode(), userChatOptions.getColor(), i18n, new RoomUIListener() {
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

            public void onChatNotificationClear(final ChatUI chatUI) {
                if (chatUI.equals(currentChat)) {
                    view.clearBottomChatNotification();
                }
            }

            public void onClose(final ChatUI chatUI) {
                xmpp.getInstance(RoomManager.class).close(chat);
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

            public void onNewChatNotification(final ChatUI chatUI, final ChatNotification chatNotification) {
                if (chatUI.equals(currentChat)) {
                    view.setBottomChatNotification(chatNotification);
                }
            }

            public void onUnHighLight(final ChatUI chatUI) {
                view.unHighLight();
                listener.onConversationAttended(chatUI.getChatTitle());
            }

            public void onUserDrop(final ChatUI chatUI, final XmppURI userURI) {
                ((RoomUI) chatUI).askInvitation(userURI);
            }

        }) : chatUI);

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
        resetWhenNoChats();
        resetAfterLogout();
        createXmppListeners();
        setRosterItemVisibility();
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

    public boolean isVisible() {
        return view.isVisible();
    }

    public void joinChat(final XmppURI userURI) {
        xmpp.getChatManager().openChat(userURI, ChatUIStartedByMe.class, new ChatUIStartedByMe(true));
    }

    public void joinRoom(final String roomName, final String serverName) {
        final XmppURI uri = new XmppURI(roomName, serverName, currentUserJid.getNode());
        xmpp.getInstance(RoomManager.class).openChat(uri, ChatUIStartedByMe.class, new ChatUIStartedByMe(true));
    }

    public void onComposing() {
        currentChat.onComposing();
    }

    public void onInputFocus() {
        currentChat.onInputFocus();
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
        for (final Chat chat : xmpp.getChatManager().getChats()) {
            closeChatAfterConfirmed(chat);
        }
        for (final Chat room : xmpp.getInstance(RoomManager.class).getChats()) {
            closeChatAfterConfirmed(room);
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
        for (final Chat chat : xmpp.getChatManager().getChats()) {
            setChatColor(chat, color);
        }
        for (final Chat room : xmpp.getInstance(RoomManager.class).getChats()) {
            setChatColor(room, color);
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

    void doConnecting() {
        view.setLoadingVisible(true);
    }

    UserChatOptions getUserChatOptions() {
        return userChatOptions;
    }

    void messageReceived(final Chat chat, final Message message) {
        final String body = message.getBody();
        // FIXME: remove this?
        if (body != null) {
            final ChatUI chatUI = getChatUI(chat);
            final String node = message.getFromURI().getNode();
            chatUI.addMessage(node != null ? node : message.getFromURI().toString(), body);
        }
    }

    void messageReceivedInRoom(final Chat chat, final Message message) {
        final RoomUI roomUI = (RoomUI) getChatUI(chat);
        final XmppURI fromURI = message.getFromURI();
        if (fromURI.getResource() == null && fromURI.getNode().equals(chat.getOtherURI().getNode())) {
            // Info messsage from room
            roomUI.addInfoMessage(message.getBody());
        } else {
            roomUI.addMessage(fromURI.getResource(), message.getBody());
        }
    }

    private void checkNoChats() {
        if (openedChats == 0) {
            Log.info("No more chats");
            resetWhenNoChats();
        }
    }

    private void checkThereAreChats() {
        if (openedChats >= 1) {
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
                    resetAfterLogout();
                    break;
                }
            }
        });

        xmpp.getChatManager().addListener(new ChatManagerListener() {
            public void onChatClosed(final Chat chat) {
                doAfterChatClosed(chat);
            }

            public void onChatCreated(final Chat chat) {
                final ChatUI chatUI = createChat(chat);
                dockChatUIifIsStartedByMe(chat, chatUI);
                chat.addListener(new ChatListener() {
                    public void onMessageReceived(final Chat chat, final Message message) {
                        if (message.getBody() != null) {
                            dockChatUI(chat, chatUI);
                            messageReceived(chat, message);
                        }
                    }

                    public void onMessageSent(final Chat chat, final Message message) {
                        messageReceived(chat, message);
                    }
                });
            }
        });

        final RoomManager roomManager = xmpp.getInstance(RoomManager.class);
        roomManager.addListener(new RoomManagerListener() {
            public void onChatClosed(final Chat chat) {
		doAfterChatClosed(chat);
            }

            public void onChatCreated(final Chat room) {
                final RoomUI roomUI = createRoom(room, currentUserJid.getNode());
                dockChatUIifIsStartedByMe(room, roomUI);
                room.addListener(new RoomListener() {
                    public void onMessageReceived(final Chat chat, final Message message) {
                        if (message.getBody() != null) {
                            dockChatUI(room, roomUI);
                            messageReceivedInRoom(chat, message);
                        }
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
                        if (nick != null) {
                            roomUI.addInfoMessage(i18n.t("[%s] has changed the subject to: ", nick) + newSubject);
                        } else {
                            roomUI.addInfoMessage(i18n.t("Subject changed to: ") + newSubject);
                        }
                    }
                });
            }

            public void onInvitationReceived(final XmppURI invitor, final XmppURI roomURI, final String reason) {
                view.click();
                view.roomJoinConfirm(invitor, roomURI, reason);
            }
        });

    }

    private void doAfterChatClosed(final Chat chat) {
        ChatUI chatUI = getChatUI(chat);
        if (chatUI != null) {
            openedChats--;
            chatUI.destroy();
            chat.setData(ChatUI.class, null);
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
        if (openedChats > 0) {
            view.setInputEditable(true);
        } else {
            view.setInputEditable(false);
        }
        final Presence currentPresence = presenceManager.getCurrentPresence();
        view.setOwnPresence(currentPresence != null ? new OwnPresence(currentPresence) : ONLINE_OWN_PRESENCE);
    }

    private void dockChatUI(final Chat chat, final ChatUI chatUI) {
        if (!chatUI.isDocked()) {
            openedChats++;
            chatUI.setDocked(true);
            view.addChat(chatUI);
            currentChat = chatUI;
            checkThereAreChats();
            if (!isChatStartedByMe(chat)) {
                chatUI.highLightChatTitle();
            }
        }
    }

    private void dockChatUIifIsStartedByMe(final Chat chat, final ChatUI chatUI) {
        if (isChatStartedByMe(chat)) {
            dockChatUI(chat, chatUI);
        }
    }

    private void finishChatCreation(final Chat chat, final ChatUI chatUI) {
        chat.setData(ChatUI.class, chatUI);
    }

    private ChatUI getChatUI(final Chat chat) {
        return chat.getData(ChatUI.class);
    }

    private boolean isChatStartedByMe(final Chat chat) {
        ChatUIStartedByMe chatUIData = chat.getData(ChatUIStartedByMe.class);
        return chatUIData != null && chatUIData.isStartedByMe();
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

    private void resetAfterLogout() {
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

    private void resetWhenNoChats() {
        currentChat = null;
        view.setCloseAllOptionEnabled(false);
        view.setInfoPanelVisible(true);
        view.setSendEnabled(false);
        view.setInputEditable(false);
        view.clearInputText();
        view.setEmoticonButtonEnabled(false);
        view.clearBottomChatNotification();
    }

    private void setChatColor(final Chat chat, final String color) {
        ChatUI chatUI = getChatUI(chat);
        if (chatUI != null) {
            chatUI.setUserColor(currentUserJid.getNode(), color);
        }
    }

    private void setInputEnabled(final boolean enabled) {
        view.setSendEnabled(enabled);
        view.setInputEditable(enabled);
        view.setEmoticonButtonEnabled(enabled);
    }

    private void setRosterItemVisibility() {
        final boolean visible = this.getUserChatOptions().isUnavailableBuddiesVisible();
        roster.showUnavailableRosterItems(visible);
        view.setShowUnavailableItemsButtonPressed(visible);
    }

}
