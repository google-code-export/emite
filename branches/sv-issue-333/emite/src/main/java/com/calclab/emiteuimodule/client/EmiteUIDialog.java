/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emiteuimodule.client;

import java.util.Collection;
import java.util.Date;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.core.client.bosh.BoshSettings;
import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.roster.Roster;
import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.emite.xep.avatar.client.AvatarManager;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.emiteuimodule.client.dialog.MultiChatPresenter;
import com.calclab.emiteuimodule.client.params.AvatarProvider;
import com.calclab.emiteuimodule.client.params.MultiChatCreationParam;
import com.calclab.emiteuimodule.client.room.RoomUIManager;
import com.calclab.emiteuimodule.client.status.OwnPresence;
import com.calclab.emiteuimodule.client.status.StatusUI;
import com.calclab.emiteuimodule.client.status.OwnPresence.OwnStatus;
import com.calclab.suco.client.events.Listener;
import com.google.gwt.user.client.Window;

public class EmiteUIDialog {
    private static final String EMITE_DEF_TITLE = "Emite Chat";
    private MultiChatPresenter multiChatDialog;
    private final EmiteUIFactory factory;
    private final StatusUI statusUI;
    private final Session session;
    private final ChatManager chatManager;
    private final RoomManager roomManager;
    private final AvatarManager avatarManager;
    private final Connection connection;
    private final RoomUIManager roomUIManager;
    private final Roster roster;

    public EmiteUIDialog(final Connection connection, final Session session, final ChatManager chatManager,
	    final EmiteUIFactory factory, final RoomManager roomManager, final AvatarManager avatarManager,
	    final StatusUI statusUI, final RoomUIManager roomUIManager, final Roster roster) {
	this.connection = connection;
	this.session = session;
	this.chatManager = chatManager;
	this.factory = factory;
	this.roomManager = roomManager;
	this.avatarManager = avatarManager;
	this.statusUI = statusUI;
	this.roomUIManager = roomUIManager;
	this.roster = roster;
    }

    public void addBuddie(final XmppURI buddieJid, final String name, final String... groups) {
	roster.addItem(buddieJid, name, groups);
    }

    public Chat chat(final XmppURI otherUserURI) {
	if (session.isLoggedIn()) {
	    return chatManager.open(otherUserURI);
	} else {
	    Log.error("To start a chat you need to be 'online'.");
	    return null;
	}
    }

    public void closeAllChats(final boolean withConfirmation) {
	checkIfDialogIsStarted();
	multiChatDialog.closeAllChats(withConfirmation);
    }

    public void collapse() {
	checkIfDialogIsStarted();
	multiChatDialog.collapse();
    }

    public void destroy() {
	checkIfDialogIsStarted();
	multiChatDialog.destroy();
    }

    public void expand() {
	checkIfDialogIsStarted();
	multiChatDialog.expand();
    }

    public Session getSession() {
	return session;
    }

    public void hide() {
	checkIfDialogIsStarted();
	multiChatDialog.hide();
    }

    public boolean isDialogNotStarted() {
	return multiChatDialog == null;
    }

    public boolean isLoggedIn() {
	return session.isLoggedIn();
    }

    public boolean isVisible() {
	checkIfDialogIsStarted();
	return multiChatDialog.isVisible();
    }

    public Chat joinRoom(final XmppURI roomURI) {
	if (session.isLoggedIn()) {
	    return roomManager.open(roomURI);
	} else {
	    Log.error("To join a chatroom you need to be 'online'.");
	    return null;
	}
    }

    public void onChatAttended(final Listener<String> listener) {
	checkIfDialogIsStarted();
	multiChatDialog.onChatAttended(listener);
    }

    public void onChatUnattendedWithActivity(final Listener<String> listener) {
	checkIfDialogIsStarted();
	multiChatDialog.onChatUnattendedWithActivity(listener);
    }

    public void onRosterChanged(final Listener<Collection<RosterItem>> listener) {
	// For external use: only a summarize RosterChanged event
	roster.onRosterRetrieved(listener);
	final Listener<RosterItem> changeListener = new Listener<RosterItem>() {
	    public void onEvent(final RosterItem parameter) {
		listener.onEvent(roster.getItems());
	    }
	};
	roster.onItemAdded(changeListener);
	roster.onItemChanged(changeListener);
	roster.onItemRemoved(changeListener);
    }

    public void onShowUnavailableRosterItemsChanged(final Listener<Boolean> listener) {
	checkIfDialogIsStarted();
	multiChatDialog.onShowUnavailableRosterItemsChanged(listener);
    }

    public void onUserColorChanged(final Listener<String> listener) {
	checkIfDialogIsStarted();
	statusUI.onUserColorChanged(listener);
    }

    public void onUserSubscriptionModeChanged(final Listener<SubscriptionMode> listener) {
	checkIfDialogIsStarted();
	statusUI.onUserSubscriptionModeChanged(listener);
    }

    public void refreshUserInfo(final UserChatOptions userChatOptions) {
	checkIfDialogIsStarted();
	multiChatDialog.setUserChatOptions(userChatOptions);
	statusUI.setCurrentUserChatOptions(userChatOptions);
    }

    public void setEnableStatusUI(final boolean enable) {
	checkIfDialogIsStarted();
	statusUI.setEnable(enable);
    }

    public void setOwnPresence(final OwnStatus status) {
	checkIfDialogIsStarted();
	statusUI.setOwnPresence(new OwnPresence(status));
    }

    public void setOwnVCardAvatar(final String photoBinary) {
	avatarManager.setVCardAvatar(photoBinary);
    }

    public void show() {
	checkIfDialogIsStarted();
	multiChatDialog.show();
    }

    public void show(final OwnStatus status) {
	show();
	setOwnPresence(status);
    }

    public void start(final String userJid, final String userPasswd, final String httpBase, final String host,
	    final String roomHost) {
	start(new UserChatOptions(userJid, userPasswd, ("emiteui-" + new Date().getTime()), "blue",
		SubscriptionMode.autoAcceptAll, true), httpBase, host, roomHost);
    }

    public void start(final UserChatOptions userChatOptions, final String httpBase, final String host,
	    final String roomHost) {
	// We define, default AvatarProvider and MultiChaListener for simple
	// facade
	start(userChatOptions, httpBase, host, roomHost, new AvatarProvider() {
	    public String getAvatarURL(final XmppURI userURI) {
		return "images/person-def.gif";
	    }
	}, EMITE_DEF_TITLE);
	final String initialWindowTitle = Window.getTitle();
	onChatAttended(new Listener<String>() {
	    public void onEvent(final String parameter) {
		Window.setTitle(initialWindowTitle);
	    }
	});
	onChatUnattendedWithActivity(new Listener<String>() {
	    public void onEvent(final String chatTitle) {
		Window.setTitle("(* " + chatTitle + ") " + initialWindowTitle);
	    }
	});
    }

    public void start(final UserChatOptions userChatOptions, final String httpBase, final String host,
	    final String roomHost, final AvatarProvider avatarProvider, final String emiteDialogTitle) {
	connection.setSettings(new BoshSettings(httpBase, host));
	statusUI.setCurrentUserChatOptions(userChatOptions);
	roomUIManager.setRoomHostDefault(roomHost);
	multiChatDialog = createChatDialog(new MultiChatCreationParam(emiteDialogTitle, roomHost, avatarProvider,
		userChatOptions));
	ImagesHelper.preFetchImages();
    }

    protected void checkIfDialogIsStarted() {
	if (isDialogNotStarted()) {
	    new RuntimeException("Emite UI dialog is not created (use 'start' method before)");
	}
    }

    private MultiChatPresenter createChatDialog(final MultiChatCreationParam param) {
	final MultiChatPresenter dialog = factory.createMultiChat(param);
	return dialog;
    }

}
