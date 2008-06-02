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
package com.calclab.emiteuimodule.client.dialog;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.jid;
import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;

import java.util.Collection;
import java.util.HashSet;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.chat.ChatListener;
import com.calclab.emite.client.im.chat.ChatListenerAdaptor;
import com.calclab.emite.client.im.presence.PresenceManager;
import com.calclab.emite.client.im.roster.RosterManager.SubscriptionMode;
import com.calclab.emite.client.xep.avatar.AvatarModule;
import com.calclab.emite.client.xep.chatstate.ChatState;
import com.calclab.emite.client.xep.chatstate.ChatStateManager;
import com.calclab.emite.client.xep.muc.Occupant;
import com.calclab.emite.client.xep.muc.Room;
import com.calclab.emite.client.xep.muc.RoomInvitation;
import com.calclab.emite.client.xep.muc.RoomListener;
import com.calclab.emite.client.xep.muc.RoomManager;
import com.calclab.emite.client.xep.mucç.RoomListenerAdaptor;
import com.calclab.emite.client.xmpp.session.Session.State;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.Presence.Show;
import com.calclab.emiteuimodule.client.EmiteUIFactory;
import com.calclab.emiteuimodule.client.UserChatOptions;
import com.calclab.emiteuimodule.client.chat.ChatNotification;
import com.calclab.emiteuimodule.client.chat.ChatUI;
import com.calclab.emiteuimodule.client.chat.ChatUIListener;
import com.calclab.emiteuimodule.client.chat.ChatUIStartedByMe;
import com.calclab.emiteuimodule.client.params.MultiChatCreationParam;
import com.calclab.emiteuimodule.client.room.JoinRoomPanel;
import com.calclab.emiteuimodule.client.room.RoomUI;
import com.calclab.emiteuimodule.client.room.RoomUIListener;
import com.calclab.emiteuimodule.client.roster.RosterPresenter;
import com.calclab.emiteuimodule.client.status.OwnPresence;
import com.calclab.emiteuimodule.client.status.StatusPanel;
import com.calclab.emiteuimodule.client.status.StatusPanelListener;
import com.calclab.emiteuimodule.client.status.OwnPresence.OwnStatus;
import com.calclab.modular.client.signal.Slot;
import com.calclab.modular.client.signal.Signal;

public class MultiChatPresenter {
    private static final OwnPresence OFFLINE_OWN_PRESENCE = new OwnPresence(OwnStatus.offline);

    private ChatUI currentChat;
    private XmppURI currentUserJid;
    private String currentUserPasswd;
    private final EmiteUIFactory factory;
    private final I18nTranslationService i18n;
    private final PresenceManager presenceManager;
    private UserChatOptions userChatOptions;
    private MultiChatPanel view;
    private final Xmpp xmpp;
    private final String roomHost;
    private final RosterPresenter roster;
    private int openedChats;
    private final Signal<String> onChatAttended;
    private final Signal<String> onChatUnattendedWithActivity;
    private final Signal<Boolean> onShowUnavailableRosterItemsChanged;
    private final Signal<String> onUserColorChanged;
    private final Signal<SubscriptionMode> onUserSubscriptionModeChanged;

    public MultiChatPresenter(final Xmpp xmpp, final I18nTranslationService i18n, final EmiteUIFactory factory,
	    final MultiChatCreationParam param, final RosterPresenter roster) {
	this.xmpp = xmpp;
	this.i18n = i18n;
	this.factory = factory;
	this.roster = roster;
	setUserChatOptions(param.getUserChatOptions());
	roomHost = param.getRoomHost();
	presenceManager = xmpp.getPresenceManager();
	openedChats = 0;
	onChatAttended = new Signal<String>("onChatAttended");
	onChatUnattendedWithActivity = new Signal<String>("onChatUnattendedWithActivity");
	onShowUnavailableRosterItemsChanged = new Signal<Boolean>("onShowUnavailableRosterItemsChanged");
	onUserColorChanged = new Signal<String>("onUserColorChanged");
	onUserSubscriptionModeChanged = new Signal<SubscriptionMode>("onUserSubscriptionModeChanged");
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
	final ChatUI chatUIalreadyOpened = getChatUI(chat);
	logIfChatAlreadyOpened(chatUIalreadyOpened);
	final ChatStateManager stateManager = xmpp.getInstance(ChatStateManager.class);
	final ChatState chatState = stateManager.getChatState(chat);
	final ChatUI chatUI = chatUIalreadyOpened == null ? factory.createChatUI(chat.getOtherURI(), currentUserJid
		.getNode(), userChatOptions.getColor(), chatState, new ChatUIListener() {

	    public void onActivate(final ChatUI chatUI) {
		view.setInputText(chatUI.getSavedInput());
		currentChat = chatUI;
		updateViewWithChatStatus(chat, chatUI);
		view.setBottomChatNotification(chatUI.getSavedChatNotification());
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
		chat.send(new Message(message));
	    }

	    public void onDeactivate(final ChatUI chatUI) {
		chatUI.saveInput(view.getInputText());
	    }

	    public void onHighLight(final ChatUI chatUI) {
		view.highLight();
		onChatUnattendedWithActivity.fire(chatUI.getChatTitle());
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
		onChatAttended.fire(chatUI.getChatTitle());
	    }

	    public void onUserDrop(final ChatUI chatUI, final XmppURI userURI) {
		if (!chat.getOtherURI().equals(userURI)) {
		    joinChat(userURI);
		}
	    }
	}) : chatUIalreadyOpened;
	finishChatCreation(chat, chatUI);
	return chatUI;
    }

    public RoomUI createRoom(final Chat chat, final String userAlias) {
	final ChatUI chatUIalreadyOpened = getChatUI(chat);
	logIfChatAlreadyOpened(chatUIalreadyOpened);
	final RoomUI roomUI = (RoomUI) (chatUIalreadyOpened == null ? factory.createRoomUI(chat.getOtherURI(),
		currentUserJid.getNode(), userChatOptions.getColor(), i18n, new RoomUIListener() {
		    // FIXME: some code are duplicated with ChatUI Listener
		    // (make an
		    // abstract listener)
		    public void onActivate(final ChatUI chatUI) {
			final RoomUI roomUI = (RoomUI) chatUI;
			view.setInputText(roomUI.getSavedInput());
			currentChat = chatUI;
			updateViewWithChatStatus(chat, chatUI);
			view.setBottomChatNotification(chatUI.getSavedChatNotification());
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
			chat.send(new Message(message));
		    }

		    public void onDeactivate(final ChatUI chatUI) {
			chatUI.saveInput(view.getInputText());
		    }

		    public void onHighLight(final ChatUI chatUI) {
			view.highLight();
			onChatUnattendedWithActivity.fire(chatUI.getChatTitle());
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
			onChatAttended.fire(chatUI.getChatTitle());
		    }

		    public void onUserDrop(final ChatUI chatUI, final XmppURI userURI) {
			((RoomUI) chatUI).askInvitation(userURI);
		    }

		}) : chatUIalreadyOpened);

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
	setUnavailableRosterItemVisibility();
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

    public void onChatAttended(final Slot<String> listener) {
	onChatAttended.add(listener);
    }

    public void onChatUnattendedWithActivity(final Slot<String> listener) {
	onChatUnattendedWithActivity.add(listener);
    }

    public void onComposing() {
	currentChat.onComposing();
    }

    public void onInputFocus() {
	if (currentChat != null) {
	    currentChat.onInputFocus();
	}
    }

    public void onInputUnFocus() {
	if (currentChat != null) {
	    if (view.isEmoticonDialogVisible()) {
		// Do nothing because we are selecting a emoticon
	    } else {
		currentChat.onInputUnFocus();
	    }
	}
    }

    public void onModifySubjectRequested(final String newSubject) {
	final RoomUI roomUI = (RoomUI) currentChat;
	roomUI.onModifySubjectRequested(newSubject);
    }

    public void onShowUnavailableRosterItemsChanged(final Slot<Boolean> listener) {
	onShowUnavailableRosterItemsChanged.add(listener);
    }

    public void onUserColorChanged(final Slot<String> listener) {
	onUserColorChanged.add(listener);
    }

    public void onUserDropped(final XmppURI userURI) {
	if (currentChat != null) {
	    currentChat.onUserDrop(userURI);
	} else {
	    joinChat(userURI);
	}
    }

    public void onUserSubscriptionModeChanged(final Slot<SubscriptionMode> listener) {
	onUserSubscriptionModeChanged.add(listener);
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
	onShowUnavailableRosterItemsChanged.fire(show);
	userChatOptions.setUnavailableRosterItemsVisible(show);
    }

    protected void onCloseAllConfirmed() {
	final Collection<Chat> chatsToClose = new HashSet<Chat>();
	chatsToClose.addAll(xmpp.getChatManager().getChats());
	chatsToClose.addAll(xmpp.getInstance(RoomManager.class).getChats());
	Log.info("Trying to close " + chatsToClose.size() + " chats");
	for (final Chat chat : chatsToClose) {
	    final ChatUI chatUI = getChatUI(chat);
	    if (chatUI != null && chatUI.isDocked()) {
		closeChatUI(chatUI);
		view.removeChat(chatUI);
	    }
	}
    }

    protected void onCurrentUserSend(final String message, final boolean withEnter) {
	final boolean isEmpty = message == null || message.equals("");
	if (!isEmpty) {
	    view.clearInputText();
	    currentChat.onCurrentUserSend(message);
	}
	if (!withEnter) {
	    view.focusInput();
	}
    }

    protected void onUserColorChanged(final String color) {
	for (final Chat chat : xmpp.getChatManager().getChats()) {
	    setChatColor(chat, color);
	}
	for (final Chat room : xmpp.getInstance(RoomManager.class).getChats()) {
	    setChatColor(room, color);
	}
	userChatOptions.setColor(color);
	onUserColorChanged.fire(color);
    }

    protected void onUserSubscriptionModeChanged(final SubscriptionMode subscriptionMode) {
	xmpp.getRosterManager().setSubscriptionMode(subscriptionMode);
	userChatOptions.setSubscriptionMode(subscriptionMode);
	onUserSubscriptionModeChanged.fire(subscriptionMode);
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
	    if (chatUI == null) {
		Log.warn("Message received in a inexistent chatUI");
	    } else {
		final String node = message.getFromURI().getNode();
		chatUI.addMessage(node != null ? node : message.getFromURI().toString(), body);
	    }
	}
    }

    void messageReceivedInRoom(final Chat chat, final Message message) {
	final RoomUI roomUI = (RoomUI) getChatUI(chat);
	final XmppURI fromURI = message.getFromURI();
	if (roomUI == null) {
	    Log.warn("Message received in a inexistent roomUI");
	} else {
	    if (fromURI.getResource() == null && fromURI.getNode().equals(chat.getOtherURI().getNode())) {
		// Info messsage from room
		roomUI.addInfoMessage(message.getBody());
	    } else {
		roomUI.addMessage(fromURI.getResource(), message.getBody());
	    }
	}
    }

    private void addStateListener(final Chat chat) {
	chat.onStateChanged(new Slot<com.calclab.emite.client.im.chat.Chat.Status>() {
	    public void onEvent(final com.calclab.emite.client.im.chat.Chat.Status parameter) {
		final ChatUI chatUI = getChatUI(chat);
		if (chatUI != null && chatUI.equals(currentChat)) {
		    updateViewWithChatStatus(chat, chatUI);
		}
	    }
	});
    }

    private void checkNoChats() {
	if (openedChats == 0) {
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
	xmpp.getSession().onStateChanged(new Slot<State>() {
	    public void onEvent(final State current) {
		switch (current) {
		case notAuthorized:
		    view.showAlert(i18n.t("Error in authentication. Wrong user jabber id or password."));
		    break;
		case loggedIn:
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

	// FIXME: This in StatusPresenter
	xmpp.getPresenceManager().onOwnPresenceChanged(new Slot<Presence>() {
	    public void onEvent(final Presence presence) {
		view.setOwnPresence(new OwnPresence(presence));
	    }
	});

	xmpp.getChatManager().onChatCreated(new Slot<Chat>() {
	    public void onEvent(final Chat chat) {
		final ChatUI chatUI = createChat(chat);
		dockChatUIifIsStartedByMe(chat, chatUI);
		new ChatListenerAdaptor(chat, new ChatListener() {
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
		addStateListener(chat);
	    }
	});

	xmpp.getChatManager().onChatClosed(new Slot<Chat>() {
	    public void onEvent(final Chat chat) {
		doAfterChatClosed(chat);
	    }
	});

	final RoomManager roomManager = xmpp.getInstance(RoomManager.class);

	roomManager.onChatCreated(new Slot<Chat>() {
	    public void onEvent(final Chat room) {
		final RoomUI roomUI = createRoom(room, currentUserJid.getNode());
		dockChatUIifIsStartedByMe(room, roomUI);

		new RoomListenerAdaptor(room, new RoomListener() {
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
		addStateListener(room);
	    }
	});

	roomManager.onChatClosed(new Slot<Chat>() {
	    public void onEvent(final Chat chat) {
		doAfterChatClosed(chat);
	    }
	});

	roomManager.onInvitationReceived(new Slot<RoomInvitation>() {
	    public void onEvent(final RoomInvitation parameter) {
		view.click();
		view.roomJoinConfirm(parameter.getInvitor(), parameter.getRoomURI(), parameter.getReason());
	    }
	});
    }

    private void doAfterChatClosed(final Chat chat) {
	final ChatUI chatUI = getChatUI(chat);
	if (chatUI != null) {
	    openedChats--;
	    if (openedChats == 0) {
		view.setInfoPanelVisible(true);
	    }
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
    }

    private void dockChatUI(final Chat chat, final ChatUI chatUI) {
	if (!chatUI.isDocked()) {
	    openedChats++;
	    chatUI.setDocked(true);
	    currentChat = chatUI;
	    view.addChat(chatUI);
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
	final ChatUIStartedByMe chatUIData = chat.getData(ChatUIStartedByMe.class);
	return chatUIData != null && chatUIData.isStartedByMe();
    }

    private void logIfChatAlreadyOpened(final ChatUI chatUIalreadyOpened) {
	if (chatUIalreadyOpened != null) {
	    Log.debug("This conversation is already opened");
	}
    }

    private void loginIfnecessary(final Show status, final String statusText) {
	switch (xmpp.getSession().getState()) {
	case disconnected:
	    xmpp.login(new XmppURI(currentUserJid.getNode(), currentUserJid.getHost(), userChatOptions.getResource()),
		    currentUserPasswd, status, statusText);
	    break;
	case authorized:
	case connecting:
	case ready:
	    presenceManager.setOwnPresence(statusText, status);
	    break;
	case error:
	    Log.error("Trying to set status and whe have a internal error");
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
	setInputEnabled(false);
	view.setOwnPresence(OFFLINE_OWN_PRESENCE);
	roster.clearRoster();
	view.clearBottomChatNotification();
    }

    private void resetWhenNoChats() {
	currentChat = null;
	view.setCloseAllOptionEnabled(false);
	view.clearInputText();
	setInputEnabled(false);
	view.clearBottomChatNotification();
    }

    private void setChatColor(final Chat chat, final String color) {
	final ChatUI chatUI = getChatUI(chat);
	if (chatUI != null) {
	    chatUI.setUserColor(currentUserJid.getNode(), color);
	}
    }

    private void setInputEnabled(final boolean enabled) {
	view.setSendEnabled(enabled);
	view.setInputEditable(enabled);
	view.setEmoticonButtonEnabled(enabled);
	view.focusInput();
    }

    private void setUnavailableRosterItemVisibility() {
	// during init
	roster.showUnavailableRosterItems(this.getUserChatOptions().isUnavailableRosterItemsVisible());
	view.setShowUnavailableItemsButtonPressed(this.getUserChatOptions().isUnavailableRosterItemsVisible());
    }

    private void updateViewWithChatStatus(final Chat chat, final ChatUI chatUI) {
	switch (chat.getState()) {
	case locked:
	    // Log.info("Chat locked:" + chat.getID());
	    setInputEnabled(false);
	    chatUI.clearSavedChatNotification();
	    break;
	case ready:
	    // Log.info("Chat unlocked:" + chat.getID());
	    setInputEnabled(true);
	    break;
	}
    }

}
