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
package com.calclab.emiteuimodule.client.status;

import org.ourproject.kune.platf.client.View;
import org.ourproject.kune.platf.client.i18n.I18nTranslationService;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.presence.PresenceManager;
import com.calclab.emite.im.client.roster.Roster;
import com.calclab.emite.xep.muc.client.RoomManager;
import com.calclab.emiteuimodule.client.SubscriptionMode;
import com.calclab.emiteuimodule.client.UserChatOptions;
import com.calclab.emiteuimodule.client.chat.ChatUI;
import com.calclab.emiteuimodule.client.status.OwnPresence.OwnStatus;
import com.calclab.emiteuimodule.client.subscription.SubscriptionUI;
import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Listener;
import com.calclab.suco.client.ioc.Provider;

public class StatusUIPresenter implements StatusUI {

    private static final OwnPresence OFFLINE_OWN_PRESENCE = new OwnPresence(OwnStatus.offline);

    private StatusUIView view;
    private final PresenceManager presenceManager;
    private UserChatOptions userChatOptions;
    private final Session session;
    private final I18nTranslationService i18n;
    private final Event<StatusUI> onAfterLogin;
    private final Event<StatusUI> onAfterLogout;
    private final Event<StatusUI> onCloseAllConfirmed;
    private final Event<String> onUserColorChanged;
    private final Event<SubscriptionMode> onUserSubscriptionModeChanged;
    private final Provider<ChatManager> chatManagerProvider;
    private final Provider<RoomManager> roomManagerProvider;
    private final SubscriptionUI subscriber;

    public StatusUIPresenter(final Session session, final PresenceManager presenceManager, final Roster roster,
	    final Provider<ChatManager> chatManager, final Provider<RoomManager> roomManager,
	    final SubscriptionUI subscriber, final I18nTranslationService i18n) {
	this.session = session;
	this.presenceManager = presenceManager;
	this.subscriber = subscriber;
	this.chatManagerProvider = chatManager;
	this.roomManagerProvider = roomManager;
	this.i18n = i18n;
	this.onAfterLogin = new Event<StatusUI>("onAfterLogin");
	this.onAfterLogout = new Event<StatusUI>("onAfterLogout");
	this.onCloseAllConfirmed = new Event<StatusUI>("onCloseAllConfirmed");
	onUserColorChanged = new Event<String>("onUserColorChanged");
	onUserSubscriptionModeChanged = new Event<SubscriptionMode>("onUserSubscriptionModeChanged");
    }

    public void addButtonItem(final View item) {
	view.addButtonItem(item);
    }

    public void addChatMenuItem(final View item) {
	view.addChatMenuItem(item);
    }

    public void addOptionsSubMenuItem(final View item) {
	view.addOptionsSubMenuItem(item);
    }

    public void addToolbarItem(final View item) {
	view.addToolbarItem(item);
    }

    public void confirmCloseAll() {
	view.confirmCloseAll();
    }

    public void createListeners() {
	presenceManager.onOwnPresenceChanged(new Listener<Presence>() {
	    public void onEvent(final Presence presence) {
		view.setOwnPresence(new OwnPresence(presence));
	    }
	});
	session.onStateChanged(new Listener<Session.State>() {
	    public void onEvent(final Session.State current) {
		switch (current) {
		case notAuthorized:
		    view.showAlert(i18n.t("Error in authentication. Wrong user jabber id or password."));
		    break;
		case loggedIn:
		    view.setLoadingVisible(false);
		    onAfterLogin();
		    break;
		case connecting:
		    onConnecting();
		    break;
		case disconnected:
		    onAfterLogout();
		    break;
		}
	    }
	});
    }

    public View getView() {
	return view;
    }

    public void init(final StatusUIView view) {
	this.view = view;
	createListeners();
    }

    public void onAfterLogin(final Listener<StatusUI> listener) {
	onAfterLogin.add(listener);
    }

    public void onAfterLogout(final Listener<StatusUI> listener) {
	onAfterLogout.add(listener);
    }

    public void onCloseAllConfirmed() {
	onCloseAllConfirmed.fire(this);
    }

    public void onCloseAllConfirmed(final Listener<StatusUI> listener) {
	onCloseAllConfirmed.add(listener);
    }

    public void onUserColorChanged(final Listener<String> listener) {
	onUserColorChanged.add(listener);
    }

    public void onUserSubscriptionModeChanged(final Listener<SubscriptionMode> listener) {
	onUserSubscriptionModeChanged.add(listener);
    }

    public void setCloseAllOptionEnabled(final boolean enabled) {
	view.setCloseAllOptionEnabled(enabled);
    }

    public void setCurrentUserChatOptions(final UserChatOptions userChatOptions) {
	this.userChatOptions = userChatOptions;
	final SubscriptionMode subscriptionMode = userChatOptions.getSubscriptionMode();
	subscriber.setSubscriptionMode(subscriptionMode);
	view.setSubscriptionMode(subscriptionMode);
    }

    public void setEnable(final boolean enable) {
	view.setEnable(enable);
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
	    session.logout();
	    break;
	}
	view.setOwnPresence(ownPresence);
    }

    protected void onUserColorChanged(final String color) {
	assert userChatOptions != null;
	for (final Chat chat : chatManagerProvider.get().getChats()) {
	    setChatColor(chat, color);
	}
	for (final Chat room : roomManagerProvider.get().getChats()) {
	    setChatColor(room, color);
	}
	userChatOptions.setColor(color);
	onUserColorChanged.fire(color);
    }

    void onUserSubscriptionModeChanged(final SubscriptionMode subscriptionMode) {
	assert userChatOptions != null;
	subscriber.setSubscriptionMode(subscriptionMode);
	userChatOptions.setSubscriptionMode(subscriptionMode);
	onUserSubscriptionModeChanged.fire(subscriptionMode);
    }

    private void loginIfnecessary(final Show show, final String statusText) {
	assert userChatOptions != null;
	final XmppURI userJid = userChatOptions.getUserJid();
	switch (session.getState()) {
	case disconnected:
	    session.login(XmppURI.uri(userJid.getNode(), userJid.getHost(), userChatOptions.getResource()),
		    userChatOptions.getUserPassword());
	    break;
	case error:
	    view.setLoadingVisible(false);
	    Log.error("Trying to set status and whe have a internal error");
	    break;
	}
	presenceManager.setOwnPresence(Presence.build(statusText, show));
    }

    private void onAfterLogin() {
	view.setLoadingVisible(false);
	onAfterLogin.fire(this);
    }

    private void onAfterLogout() {
	view.setLoadingVisible(false);
	view.setOwnPresence(OFFLINE_OWN_PRESENCE);
	onAfterLogout.fire(this);
    }

    private void onConnecting() {
	view.setLoadingVisible(true);
    }

    private void setChatColor(final Chat chat, final String color) {
	final ChatUI chatUI = chat.getData(ChatUI.class);
	if (chatUI != null) {
	    chatUI.setCurrentUserColor(color);
	}
    }

}
