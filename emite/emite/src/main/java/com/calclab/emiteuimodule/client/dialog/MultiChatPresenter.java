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

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;

import java.util.Collection;
import java.util.HashSet;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.chat.ChatManager;
import com.calclab.emite.client.im.roster.RosterManager;
import com.calclab.emite.client.xep.avatar.AvatarManager;
import com.calclab.emite.client.xep.chatstate.ChatStateManager;
import com.calclab.emite.client.xep.chatstate.StateManager;
import com.calclab.emite.client.xep.muc.Occupant;
import com.calclab.emite.client.xep.muc.Room;
import com.calclab.emite.client.xep.muc.RoomManager;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emiteuimodule.client.EmiteUIFactory;
import com.calclab.emiteuimodule.client.UserChatOptions;
import com.calclab.emiteuimodule.client.chat.ChatNotification;
import com.calclab.emiteuimodule.client.chat.ChatUI;
import com.calclab.emiteuimodule.client.chat.ChatUIStartedByMe;
import com.calclab.emiteuimodule.client.params.MultiChatCreationParam;
import com.calclab.emiteuimodule.client.room.RoomUI;
import com.calclab.emiteuimodule.client.roster.RosterUIPresenter;
import com.calclab.emiteuimodule.client.sound.SoundManager;
import com.calclab.emiteuimodule.client.status.StatusUI;
import com.calclab.suco.client.provider.Provider;
import com.calclab.suco.client.signal.Signal;
import com.calclab.suco.client.signal.Slot;
import com.calclab.suco.client.signal.Slot2;

public class MultiChatPresenter {

    private ChatUI currentChat;
    private final EmiteUIFactory factory;
    private final I18nTranslationService i18n;
    private UserChatOptions userChatOptions;
    private MultiChatPanel view;
    private final Xmpp xmpp;
    private final String roomHost;
    private final RosterUIPresenter roster;
    private int openedChats;
    private final Signal<String> onChatAttended;
    private final Signal<String> onChatUnattendedWithActivity;
    private final Signal<Boolean> onShowUnavailableRosterItemsChanged;
    private final ChatManager chatManager;
    private final RoomManager roomManager;
    private final StateManager stateManager;
    private final RosterManager rosterManager;
    private final StatusUI statusUI;
    private final Provider<SoundManager> soundManagerProvider;

    public MultiChatPresenter(final Xmpp xmpp, final I18nTranslationService i18n, final EmiteUIFactory factory,
	    final MultiChatCreationParam param, final RosterUIPresenter roster, final StatusUI statusUI,
	    final Provider<SoundManager> soundManagerProvider) {
	this.xmpp = xmpp;
	this.i18n = i18n;
	this.factory = factory;
	this.roster = roster;
	this.statusUI = statusUI;
	this.soundManagerProvider = soundManagerProvider;
	setUserChatOptions(param.getUserChatOptions());
	roomHost = param.getRoomHost();
	chatManager = xmpp.getChatManager();
	roomManager = xmpp.getInstance(RoomManager.class);
	rosterManager = xmpp.getRosterManager();
	stateManager = xmpp.getInstance(StateManager.class);
	openedChats = 0;
	onChatAttended = new Signal<String>("onChatAttended");
	onChatUnattendedWithActivity = new Signal<String>("onChatUnattendedWithActivity");
	onShowUnavailableRosterItemsChanged = new Signal<Boolean>("onShowUnavailableRosterItemsChanged");
	roster.onOpenChat(new Slot<XmppURI>() {
	    public void onEvent(final XmppURI userURI) {
		joinChat(userURI);
	    }
	});
    }

    public void addRosterItem(final String name, final String jid) {
	Log.info("Adding " + name + "(" + jid + ") to your roster.");
	rosterManager.requestAddItem(uri(jid), name, null);
    }

    public void center() {
	view.center();
    }

    public void closeAllChats(final boolean withConfirmation) {
	if (withConfirmation) {
	    statusUI.confirmCloseAll();
	} else {
	    onCloseAllConfirmed();
	}
    }

    public void collapse() {
	view.collapse();
    }

    public ChatUI createChat(final Chat chat) {
	final ChatUI chatUIalreadyOpened = getChatUI(chat);
	logIfChatAlreadyOpened(chatUIalreadyOpened);
	final ChatStateManager chatStateManager = stateManager.getChatState(chat);
	final ChatUI chatUI = chatUIalreadyOpened == null ? factory.createChatUI(chat.getOtherURI(), userChatOptions
		.getUserJid().getNode(), userChatOptions.getColor(), chatStateManager) : chatUIalreadyOpened;
	if (chatUIalreadyOpened == null) {
	    addCommonChatSignals(chat, chatUI);
	    chatUI.onClose(new Slot<ChatUI>() {
		public void onEvent(final ChatUI parameter) {
		    chatManager.close(chat);
		}
	    });
	    chatUI.onUserDrop(new Slot<XmppURI>() {
		public void onEvent(final XmppURI userURI) {
		    if (!chat.getOtherURI().equals(userURI)) {
			joinChat(userURI);
		    }
		}
	    });
	    chat.setData(ChatUI.class, chatUI);
	}
	return chatUI;
    }

    public RoomUI createRoom(final Chat chat, final String userAlias) {
	final ChatUI chatUIalreadyOpened = getChatUI(chat);
	logIfChatAlreadyOpened(chatUIalreadyOpened);
	// FIXME userCO.getUserJid ... etc
	final RoomUI roomUI = (RoomUI) (chatUIalreadyOpened == null ? factory.createRoomUI(chat.getOtherURI(),
		userChatOptions.getUserJid().getNode(), userChatOptions.getColor(), i18n) : chatUIalreadyOpened);
	if (chatUIalreadyOpened == null) {
	    addCommonChatSignals(chat, roomUI);
	    roomUI.onClose(new Slot<ChatUI>() {
		public void onEvent(final ChatUI parameter) {
		    roomManager.close(chat);
		}
	    });
	    roomUI.onUserDrop(new Slot<XmppURI>() {
		public void onEvent(final XmppURI userURI) {
		    roomUI.askInvitation(userURI);
		}
	    });
	    roomUI.onInviteUserRequested(new Slot2<XmppURI, String>() {
		public void onEvent(final XmppURI userJid, final String reasonText) {
		    ((Room) chat).sendInvitationTo(userJid.toString(), reasonText);
		    view.setBottomInfoMessage(i18n.t("Invitation sended"));
		}
	    });
	    roomUI.onModifySubjectRequested(new Slot<String>() {
		public void onEvent(final String newSubject) {
		    ((Room) chat).setSubject(newSubject);
		}
	    });
	    chat.setData(ChatUI.class, roomUI);
	}
	return roomUI;
    }

    public void destroy() {
	view.destroy();
    }

    public void expand() {
	view.expand();
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
	statusUI.onAfterLogin(new Slot<StatusUI>() {
	    public void onEvent(final StatusUI parameter) {
		doAfterLogin();
	    }
	});
	statusUI.onAfterLogout(new Slot<StatusUI>() {
	    public void onEvent(final StatusUI parameter) {
		resetAfterLogout();
	    }
	});
	statusUI.onCloseAllConfirmed(new Slot<StatusUI>() {
	    public void onEvent(final StatusUI parameter) {
		onCloseAllConfirmed();
	    }
	});
    }

    public boolean isVisible() {
	return view.isVisible();
    }

    public void joinChat(final XmppURI userURI) {
	final Chat chat = chatManager.openChat(userURI, ChatUIStartedByMe.class, new ChatUIStartedByMe(true));
	final ChatUI chatUI = getChatUI(chat);
	if (chatUI != null && !chatUI.isDocked()) {
	    // Bug 94
	    Log.debug("Already opened chat with no body, but not docked");
	    dockChatUI(chat, chatUI);
	}
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

    public void onUserDropped(final XmppURI userURI) {
	if (currentChat != null) {
	    currentChat.onUserDrop(userURI);
	} else {
	    joinChat(userURI);
	}
    }

    public void setUserChatOptions(final UserChatOptions userChatOptions) {
	this.userChatOptions = userChatOptions;
    }

    public void setVCardAvatar(final String photoBinary) {
	xmpp.getInstance(AvatarManager.class).setVCardAvatar(photoBinary);
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
	chatsToClose.addAll(chatManager.getChats());
	chatsToClose.addAll(roomManager.getChats());
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

    void closeChatUI(final ChatUI chatUI) {
	chatUI.onClose();
    }

    UserChatOptions getUserChatOptions() {
	return userChatOptions;
    }

    void messageReceived(final Chat chat, final Message message) {
	final String body = message.getBody();
	final XmppURI fromURI = message.getFrom();
	// FIXME: remove this?
	if (body != null) {
	    final ChatUI chatUI = getChatUI(chat);
	    if (chatUI == null) {
		Log.warn("Message received in an inexistent chatUI");
	    } else {
		chatUI.addMessage(fromURI, body);
	    }
	}
    }

    void messageReceivedInRoom(final Chat chat, final Message message) {
	final RoomUI roomUI = (RoomUI) getChatUI(chat);
	final XmppURI fromURI = message.getFrom();
	if (roomUI == null) {
	    Log.warn("Message received in an inexistent roomUI");
	} else {
	    if (fromURI.getResource() == null && fromURI.getNode().equals(chat.getOtherURI().getNode())) {
		// Info messsage from room
		roomUI.addInfoMessage(message.getBody());
	    } else {
		roomUI.addMessage(fromURI, message.getBody());
	    }
	}
    }

    private void addCommonChatSignals(final Chat chat, final ChatUI chatUI) {
	chatUI.onActivate(new Slot<ChatUI>() {
	    public void onEvent(final ChatUI parameter) {
		view.setInputText(chatUI.getSavedInput());
		currentChat = chatUI;
		updateViewWithChatStatus(chat, chatUI);
		view.setBottomChatNotification(chatUI.getSavedChatNotification());
	    }
	});
	chatUI.onChatNotificationClear(new Slot<ChatUI>() {
	    public void onEvent(final ChatUI parameter) {
		if (chatUI.equals(currentChat)) {
		    view.clearBottomChatNotification();
		}
	    }
	});
	chatUI.onCurrentUserSend(new Slot<String>() {
	    public void onEvent(final String message) {
		chat.send(new Message(message));
	    }
	});
	chatUI.onDeactivate(new Slot<ChatUI>() {
	    public void onEvent(final ChatUI parameter) {
		chatUI.saveInput(view.getInputText());
	    }
	});
	chatUI.onHighLight(new Slot<ChatUI>() {
	    public void onEvent(final ChatUI parameter) {
		view.highLight();
		soundManagerProvider.get().click();
		onChatUnattendedWithActivity.fire(chatUI.getChatTitle());
	    }
	});
	chatUI.onNewChatNotification(new Slot<ChatNotification>() {
	    public void onEvent(final ChatNotification chatNotification) {
		if (chatUI.equals(currentChat)) {
		    view.setBottomChatNotification(chatNotification);
		}
	    }
	});
	chatUI.onUnHighLight(new Slot<ChatUI>() {
	    public void onEvent(final ChatUI parameter) {
		view.unHighLight();
		onChatAttended.fire(chatUI.getChatTitle());
	    }
	});
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
	    statusUI.setCloseAllOptionEnabled(true);
	    setInputEnabled(true);
	    view.setInfoPanelVisible(false);
	}
    }

    private void createXmppListeners() {

	chatManager.onChatCreated(new Slot<Chat>() {
	    public void onEvent(final Chat chat) {
		final ChatUI chatUI = createChat(chat);
		dockChatUIifIsStartedByMe(chat, chatUI);

		chat.onMessageReceived(new Slot<Message>() {
		    public void onEvent(final Message message) {
			if (message.getBody() != null) {
			    dockChatUI(chat, chatUI);
			    messageReceived(chat, message);
			}
		    }
		});

		chat.onMessageSent(new Slot<Message>() {
		    public void onEvent(final Message message) {
			messageReceived(chat, message);
		    }
		});

		addStateListener(chat);
	    }
	});

	chatManager.onChatClosed(new Slot<Chat>() {
	    public void onEvent(final Chat chat) {
		doAfterChatClosed(chat);
	    }
	});

	roomManager.onChatCreated(new Slot<Chat>() {
	    public void onEvent(final Chat chat) {
		final Room room = (Room) chat;
		final RoomUI roomUI = createRoom(room, userChatOptions.getUserJid().getNode());
		dockChatUIifIsStartedByMe(room, roomUI);

		room.onMessageReceived(new Slot<Message>() {
		    public void onEvent(final Message message) {
			if (message.getBody() != null) {
			    dockChatUI(room, roomUI);
			    messageReceivedInRoom(room, message);
			}
		    }
		});

		room.onOccupantModified(new Slot<Occupant>() {
		    public void onEvent(final Occupant occupant) {
			Log.info("Room occupant changed (" + occupant.getUri() + ")");
			roomUI.onOccupantModified(occupant);
		    }
		});

		room.onOccupantsChanged(new Slot<Collection<Occupant>>() {
		    public void onEvent(final Collection<Occupant> occupants) {
			roomUI.onOccupantsChanged(occupants);
		    }
		});

		room.onSubjectChanged(new Slot2<Occupant, String>() {
		    public void onEvent(final Occupant occupant, final String newSubject) {
			roomUI.setSubject(newSubject);
			if (occupant != null) {
			    roomUI.addInfoMessage(i18n.t("[%s] has changed the subject to: ", occupant.getNick())
				    + newSubject);
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
	view.setTitleConectedAs(userChatOptions.getUserJid());
	view.setAddRosterItemButtonVisible(true);
	view.setShowUnavailableItemsButtonVisible(true);
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

    private void resetAfterLogout() {
	view.setOfflineTitle();
	view.setAddRosterItemButtonVisible(false);
	view.setShowUnavailableItemsButtonVisible(false);
	view.setRosterVisible(false);
	view.setOfflineInfo();
	setInputEnabled(false);
	roster.clearRoster();
	view.clearBottomChatNotification();
    }

    private void resetWhenNoChats() {
	currentChat = null;
	statusUI.setCloseAllOptionEnabled(false);
	view.clearInputText();
	setInputEnabled(false);
	view.clearBottomChatNotification();
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
