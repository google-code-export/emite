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

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;

import java.util.Collection;
import java.util.HashSet;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.Conversation;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.roster.Roster;
import com.calclab.emite.xep.avatar.client.AvatarManager;
import com.calclab.emite.xep.chatstate.client.ChatStateManager;
import com.calclab.emite.xep.chatstate.client.StateManager;
import com.calclab.emite.xep.muc.client.Occupant;
import com.calclab.emite.xep.muc.client.Room;
import com.calclab.emite.xep.muc.client.RoomManager;
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
import com.calclab.suco.client.ioc.Provider;
import com.calclab.suco.client.listener.Event;
import com.calclab.suco.client.listener.Listener;
import com.calclab.suco.client.listener.Listener2;

public class MultiChatPresenter {

    private ChatUI currentChat;
    private final EmiteUIFactory factory;
    private final I18nTranslationService i18n;
    private UserChatOptions userChatOptions;
    private MultiChatPanel view;
    private final String roomHost;
    private final RosterUIPresenter rosterPresenter;
    private int openedChats;
    private final Event<String> onChatAttended;
    private final Event<String> onChatUnattendedWithActivity;
    private final Event<Boolean> onShowUnavailableRosterItemsChanged;
    private final ChatManager chatManager;
    private final RoomManager roomManager;
    private final StateManager stateManager;
    private final StatusUI statusUI;
    private final Provider<SoundManager> soundManagerProvider;
    private final AvatarManager avatarManager;
    private final Roster roster;

    public MultiChatPresenter(final ChatManager chatManager, final Roster roster, final I18nTranslationService i18n,
	    final EmiteUIFactory factory, final MultiChatCreationParam param, final RosterUIPresenter rosterPresenter,
	    final StatusUI statusUI, final Provider<SoundManager> soundManagerProvider, final RoomManager roomManager,
	    final StateManager stateManager, final AvatarManager avatarManager) {
	this.roster = roster;
	this.i18n = i18n;
	this.factory = factory;
	this.rosterPresenter = rosterPresenter;
	this.statusUI = statusUI;
	this.soundManagerProvider = soundManagerProvider;
	this.roomManager = roomManager;
	this.stateManager = stateManager;
	this.avatarManager = avatarManager;
	setUserChatOptions(param.getUserChatOptions());
	roomHost = param.getRoomHost();
	this.chatManager = chatManager;
	openedChats = 0;
	onChatAttended = new Event<String>("onChatAttended");
	onChatUnattendedWithActivity = new Event<String>("onChatUnattendedWithActivity");
	onShowUnavailableRosterItemsChanged = new Event<Boolean>("onShowUnavailableRosterItemsChanged");
	rosterPresenter.onOpenChat(new Listener<XmppURI>() {
	    public void onEvent(final XmppURI userURI) {
		joinChat(userURI);
	    }
	});
    }

    public void addRosterItem(final String name, final String jid) {
	Log.info("Adding " + name + "(" + jid + ") to your roster.");
	roster.addItem(uri(jid), name);
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

    public ChatUI createChat(final Conversation conversation) {
	final ChatUI chatUIalreadyOpened = getChatUI(conversation);
	logIfChatAlreadyOpened(chatUIalreadyOpened);
	final ChatStateManager chatStateManager = stateManager.getChatState(conversation);
	final ChatUI chatUI = chatUIalreadyOpened == null ? factory.createChatUI(conversation.getURI(), userChatOptions
		.getUserJid().getNode(), userChatOptions.getColor(), chatStateManager) : chatUIalreadyOpened;
	if (chatUIalreadyOpened == null) {
	    addCommonChatEvents(conversation, chatUI);
	    chatUI.onClose(new Listener<ChatUI>() {
		public void onEvent(final ChatUI parameter) {
		    chatManager.close(conversation);
		}
	    });
	    chatUI.onUserDrop(new Listener<XmppURI>() {
		public void onEvent(final XmppURI userURI) {
		    if (!conversation.getURI().equals(userURI)) {
			joinChat(userURI);
		    }
		}
	    });
	    conversation.setData(ChatUI.class, chatUI);
	}
	return chatUI;
    }

    public RoomUI createRoom(final Conversation conversation, final String userAlias) {
	final ChatUI chatUIalreadyOpened = getChatUI(conversation);
	logIfChatAlreadyOpened(chatUIalreadyOpened);
	// FIXME userCO.getUserJid ... etc
	final RoomUI roomUI = (RoomUI) (chatUIalreadyOpened == null ? factory.createRoomUI(conversation.getURI(),
		userChatOptions.getUserJid().getNode(), userChatOptions.getColor(), i18n) : chatUIalreadyOpened);
	if (chatUIalreadyOpened == null) {
	    addCommonChatEvents(conversation, roomUI);
	    roomUI.onClose(new Listener<ChatUI>() {
		public void onEvent(final ChatUI parameter) {
		    roomManager.close(conversation);
		}
	    });
	    roomUI.onUserDrop(new Listener<XmppURI>() {
		public void onEvent(final XmppURI userURI) {
		    roomUI.askInvitation(userURI);
		}
	    });
	    roomUI.onInviteUserRequested(new Listener2<XmppURI, String>() {
		public void onEvent(final XmppURI userJid, final String reasonText) {
		    ((Room) conversation).sendInvitationTo(userJid.toString(), reasonText);
		    view.setBottomInfoMessage(i18n.t("Invitation sended"));
		}
	    });
	    roomUI.onModifySubjectRequested(new Listener<String>() {
		public void onEvent(final String newSubject) {
		    ((Room) conversation).setSubject(newSubject);
		}
	    });
	    conversation.setData(ChatUI.class, roomUI);
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
	statusUI.onAfterLogin(new Listener<StatusUI>() {
	    public void onEvent(final StatusUI parameter) {
		doAfterLogin();
	    }
	});
	statusUI.onAfterLogout(new Listener<StatusUI>() {
	    public void onEvent(final StatusUI parameter) {
		resetAfterLogout();
	    }
	});
	statusUI.onCloseAllConfirmed(new Listener<StatusUI>() {
	    public void onEvent(final StatusUI parameter) {
		onCloseAllConfirmed();
	    }
	});
    }

    public boolean isVisible() {
	return view.isVisible();
    }

    public void joinChat(final XmppURI userURI) {
	final Conversation conversation = chatManager.openChat(userURI, ChatUIStartedByMe.class, new ChatUIStartedByMe(
		true));
	final ChatUI chatUI = getChatUI(conversation);
	if (chatUI != null && !chatUI.isDocked()) {
	    // Bug 94
	    Log.debug("Already opened chat with no body, but not docked");
	    dockChatUI(conversation, chatUI);
	}
    }

    public void onChatAttended(final Listener<String> listener) {
	onChatAttended.add(listener);
    }

    public void onChatUnattendedWithActivity(final Listener<String> listener) {
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

    public void onShowUnavailableRosterItemsChanged(final Listener<Boolean> listener) {
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
	avatarManager.setVCardAvatar(photoBinary);
    }

    public void show() {
	view.show();
    }

    public void showUnavailableRosterItems(final boolean show) {
	rosterPresenter.showUnavailableRosterItems(show);
	onShowUnavailableRosterItemsChanged.fire(show);
	userChatOptions.setUnavailableRosterItemsVisible(show);
    }

    protected void onCloseAllConfirmed() {
	final Collection<Conversation> chatsToClose = new HashSet<Conversation>();
	chatsToClose.addAll(chatManager.getChats());
	chatsToClose.addAll(roomManager.getChats());
	Log.info("Trying to close " + chatsToClose.size() + " chats");
	for (final Conversation conversation : chatsToClose) {
	    final ChatUI chatUI = getChatUI(conversation);
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

    void messageReceived(final Conversation conversation, final Message message) {
	final String body = message.getBody();
	final XmppURI fromURI = message.getFrom();
	// FIXME: remove this?
	if (body != null) {
	    final ChatUI chatUI = getChatUI(conversation);
	    if (chatUI == null) {
		Log.warn("Message received in an inexistent chatUI");
	    } else {
		chatUI.addMessage(fromURI, body);
	    }
	}
    }

    void messageReceivedInRoom(final Conversation conversation, final Message message) {
	final RoomUI roomUI = (RoomUI) getChatUI(conversation);
	final XmppURI fromURI = message.getFrom();
	if (roomUI == null) {
	    Log.warn("Message received in an inexistent roomUI");
	} else {
	    if (fromURI.getResource() == null && fromURI.getNode().equals(conversation.getURI().getNode())) {
		// Info messsage from room
		roomUI.addInfoMessage(message.getBody());
	    } else {
		roomUI.addMessage(fromURI, message.getBody());
	    }
	}
    }

    private void addCommonChatEvents(final Conversation conversation, final ChatUI chatUI) {
	chatUI.onActivate(new Listener<ChatUI>() {
	    public void onEvent(final ChatUI parameter) {
		view.setInputText(chatUI.getSavedInput());
		currentChat = chatUI;
		updateViewWithChatStatus(conversation, chatUI);
		view.setBottomChatNotification(chatUI.getSavedChatNotification());
	    }
	});
	chatUI.onChatNotificationClear(new Listener<ChatUI>() {
	    public void onEvent(final ChatUI parameter) {
		if (chatUI.equals(currentChat)) {
		    view.clearBottomChatNotification();
		}
	    }
	});
	chatUI.onCurrentUserSend(new Listener<String>() {
	    public void onEvent(final String message) {
		conversation.send(new Message(message));
	    }
	});
	chatUI.onDeactivate(new Listener<ChatUI>() {
	    public void onEvent(final ChatUI parameter) {
		chatUI.saveInput(view.getInputText());
	    }
	});
	chatUI.onHighLight(new Listener<ChatUI>() {
	    public void onEvent(final ChatUI parameter) {
		view.highLight();
		soundManagerProvider.get().click();
		onChatUnattendedWithActivity.fire(chatUI.getChatTitle());
	    }
	});
	chatUI.onNewChatNotification(new Listener<ChatNotification>() {
	    public void onEvent(final ChatNotification chatNotification) {
		if (chatUI.equals(currentChat)) {
		    view.setBottomChatNotification(chatNotification);
		}
	    }
	});
	chatUI.onUnHighLight(new Listener<ChatUI>() {
	    public void onEvent(final ChatUI parameter) {
		view.unHighLight();
		onChatAttended.fire(chatUI.getChatTitle());
	    }
	});
    }

    private void addStateListener(final Conversation conversation) {
	conversation.onStateChanged(new Listener<com.calclab.emite.im.client.chat.Conversation.State>() {
	    public void onEvent(final com.calclab.emite.im.client.chat.Conversation.State parameter) {
		final ChatUI chatUI = getChatUI(conversation);
		if (chatUI != null && chatUI.equals(currentChat)) {
		    updateViewWithChatStatus(conversation, chatUI);
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

	chatManager.onChatCreated(new Listener<Conversation>() {
	    public void onEvent(final Conversation conversation) {
		final ChatUI chatUI = createChat(conversation);
		dockChatUIifIsStartedByMe(conversation, chatUI);

		conversation.onMessageReceived(new Listener<Message>() {
		    public void onEvent(final Message message) {
			if (message.getBody() != null) {
			    dockChatUI(conversation, chatUI);
			    messageReceived(conversation, message);
			}
		    }
		});

		conversation.onMessageSent(new Listener<Message>() {
		    public void onEvent(final Message message) {
			messageReceived(conversation, message);
		    }
		});

		addStateListener(conversation);
	    }
	});

	chatManager.onChatClosed(new Listener<Conversation>() {
	    public void onEvent(final Conversation conversation) {
		doAfterChatClosed(conversation);
	    }
	});

	roomManager.onChatCreated(new Listener<Conversation>() {
	    public void onEvent(final Conversation conversation) {
		final Room room = (Room) conversation;
		final RoomUI roomUI = createRoom(room, userChatOptions.getUserJid().getNode());
		dockChatUIifIsStartedByMe(room, roomUI);

		room.onMessageReceived(new Listener<Message>() {
		    public void onEvent(final Message message) {
			if (message.getBody() != null) {
			    dockChatUI(room, roomUI);
			    messageReceivedInRoom(room, message);
			}
		    }
		});

		room.onOccupantModified(new Listener<Occupant>() {
		    public void onEvent(final Occupant occupant) {
			Log.info("Room occupant changed (" + occupant.getURI() + ")");
			roomUI.onOccupantModified(occupant);
		    }
		});

		room.onOccupantsChanged(new Listener<Collection<Occupant>>() {
		    public void onEvent(final Collection<Occupant> occupants) {
			roomUI.onOccupantsChanged(occupants);
		    }
		});

		room.onSubjectChanged(new Listener2<Occupant, String>() {
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

	roomManager.onChatClosed(new Listener<Conversation>() {
	    public void onEvent(final Conversation conversation) {
		doAfterChatClosed(conversation);
	    }
	});
    }

    private void doAfterChatClosed(final Conversation conversation) {
	final ChatUI chatUI = getChatUI(conversation);
	if (chatUI != null) {
	    openedChats--;
	    if (openedChats == 0) {
		view.setInfoPanelVisible(true);
	    }
	    chatUI.destroy();
	    conversation.setData(ChatUI.class, null);
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

    private void dockChatUI(final Conversation conversation, final ChatUI chatUI) {
	if (!chatUI.isDocked()) {
	    openedChats++;
	    chatUI.setDocked(true);
	    currentChat = chatUI;
	    view.addChat(chatUI);
	    checkThereAreChats();
	    if (!isChatStartedByMe(conversation)) {
		chatUI.highLightChatTitle();
	    }
	}
    }

    private void dockChatUIifIsStartedByMe(final Conversation conversation, final ChatUI chatUI) {
	if (isChatStartedByMe(conversation)) {
	    dockChatUI(conversation, chatUI);
	}
    }

    private ChatUI getChatUI(final Conversation conversation) {
	return conversation.getData(ChatUI.class);
    }

    private boolean isChatStartedByMe(final Conversation conversation) {
	final ChatUIStartedByMe chatUIData = conversation.getData(ChatUIStartedByMe.class);
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
	rosterPresenter.clearRoster();
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
	rosterPresenter.showUnavailableRosterItems(this.getUserChatOptions().isUnavailableRosterItemsVisible());
	view.setShowUnavailableItemsButtonPressed(this.getUserChatOptions().isUnavailableRosterItemsVisible());
    }

    private void updateViewWithChatStatus(final Conversation conversation, final ChatUI chatUI) {
	switch (conversation.getState()) {
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
