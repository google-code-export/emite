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
package com.calclab.emiteuimodule.client.roster;

import java.util.Collection;
import java.util.HashMap;

import org.ourproject.kune.platf.client.i18n.I18nTranslationService;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.core.client.packet.TextUtils;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.im.client.roster.Roster;
import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.emite.im.client.roster.SubscriptionManager;
import com.calclab.emite.im.client.roster.SubscriptionState;
import com.calclab.emiteuimodule.client.params.AvatarProvider;
import com.calclab.emiteuimodule.client.users.ChatUserUI;
import com.calclab.emiteuimodule.client.users.UserGridMenuItem;
import com.calclab.emiteuimodule.client.users.UserGridMenuItemList;
import com.calclab.emiteuimodule.client.users.UserGridMenuItem.UserGridMenuItemListener;
import com.calclab.emiteuimodule.client.utils.ChatIconDescriptor;
import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Listener;

public class RosterUIPresenter {

    private RosterUIView view;
    private final HashMap<XmppURI, ChatUserUI> rosterMap;
    private final I18nTranslationService i18n;
    private final AvatarProvider avatarProvider;
    private boolean showUnavailableItems;
    private final Event<XmppURI> onOpenChat;
    private final Roster roster;
    private final SubscriptionManager subscriptionManager;

    public RosterUIPresenter(final Roster roster, final SubscriptionManager subscriptionManager,
	    final I18nTranslationService i18n, final AvatarProvider avatarProvider) {
	this.roster = roster;
	this.subscriptionManager = subscriptionManager;
	this.i18n = i18n;
	this.avatarProvider = avatarProvider;
	rosterMap = new HashMap<XmppURI, ChatUserUI>();
	showUnavailableItems = false;
	this.onOpenChat = new Event<XmppURI>("onOpenChat");
    }

    public void clearRoster() {
	rosterMap.clear();
	view.clearRoster();
    }

    public String getDefaultStatusText(final boolean isAvailable, final Show show) {
	String textLabel = "";
	switch (show) {
	case chat:
	    textLabel = i18n.t("Available to Chat");
	    break;
	case away:
	case xa:
	    textLabel = i18n.t("Away");
	    break;
	case dnd:
	    textLabel = i18n.t("Don't disturb");
	    break;
	case notSpecified:
	case unknown:
	    if (isAvailable) {
		textLabel = i18n.t("Online");
	    } else {
		textLabel = i18n.t("Offline");
	    }
	}
	return textLabel;
    }

    public ChatUserUI getUserByJid(final XmppURI jid) {
	return rosterMap.get(jid);
    }

    public RosterUIView getView() {
	return view;
    }

    public void init(final RosterUIView view) {
	this.view = view;
	createXmppListeners();
    }

    public void onOpenChat(final Listener<XmppURI> listener) {
	onOpenChat.add(listener);
    }

    public void openChat(final XmppURI userURI) {
	onOpenChat.fire(userURI);
    }

    public void showUnavailableRosterItems(final boolean show) {
	showUnavailableItems = show;
	for (final XmppURI jid : rosterMap.keySet()) {
	    final RosterItem item = roster.getItemByJID(jid);
	    final ChatUserUI user = rosterMap.get(jid);
	    if (item == null) {
		Log.error("Trying to update a ui roster item not in roster");
	    } else {
		refreshRosterItemInView(item, user, show);
	    }
	}
    }

    String formatRosterItemStatusText(final RosterItem item) {
	String itemStatus = item.getStatus();
	String statusText = itemStatus == null || itemStatus.equals("null") ? "" : itemStatus;
	if (statusText.equals("")) {
	    // Don't have status, ok use default status
	    statusText += getDefaultStatusText(item.isAvailable(), item.getShow());
	}
	SubscriptionState subscriptionState = item.getSubscriptionState();
	if (subscriptionState != null) {
	    switch (subscriptionState) {
	    case none:
		statusText += " " + i18n.t("(you cannot see your buddy status, your buddy neither)");
		break;
	    case to:
		statusText += " " + i18n.t("(your buddy cannot see your status)");
		break;
	    case from:
		statusText += " " + i18n.t("(you cannot see your buddy status)");
		break;
	    case both:
		break;
	    }
	}
	return statusText.equals("") ? " " : TextUtils.escape(statusText);
    }

    ChatIconDescriptor getPresenceIcon(final RosterItem item) {
	Show show = item.getShow();
	if (item.isAvailable()) {
	    switch (show) {
	    case away:
		return ChatIconDescriptor.away;
	    case chat:
	    case dnd:
	    case xa:
		return ChatIconDescriptor.valueOf(show.toString());
	    case notSpecified:
		return ChatIconDescriptor.available;
	    case unknown:
		return ChatIconDescriptor.unknown;
	    }
	} else {
	    return ChatIconDescriptor.offline;
	}
	return ChatIconDescriptor.unknown;
    }

    void refreshRosterItemInView(final RosterItem item, final ChatUserUI user, final boolean showUnavailable) {
	final boolean mustShow = item.isAvailable() || showUnavailable;
	if (mustShow) {
	    if (user.getVisible()) {
		view.updateRosterItem(user, createMenuItemList(item));
	    } else {
		view.addRosterItem(user, createMenuItemList(item));
	    }
	} else {
	    if (user.getVisible()) {
		view.removeRosterItem(user);
	    }
	}
	user.setVisible(mustShow);
    }

    private void addRosterItem(final RosterItem item) {
	logRosterItem("Adding", item);
	final ChatUserUI user = new ChatUserUI(avatarProvider.getAvatarURL(item.getJID()), item, "black");
	updateUserWithRosterItem(user, item);
	if (showUnavailableItems || item.isAvailable()) {
	    user.setVisible(true);
	    view.addRosterItem(user, createMenuItemList(item));
	} else {
	    user.setVisible(false);
	}
	rosterMap.put(user.getURI(), user);
    }

    private UserGridMenuItem<XmppURI> createCancelSubscriptorBuddyMenuItem(final XmppURI userURI) {
	return new UserGridMenuItem<XmppURI>("del-icon",
		i18n.t("Stop to show when I'm connected or not to this buddy"), new UserGridMenuItemListener() {
		    public void onAction() {
			subscriptionManager.cancelSubscription(userURI);
		    }
		});
    }

    private UserGridMenuItemList createMenuItemList(final RosterItem item) {
	return createMenuItemList(item.getJID(), item.getSubscriptionState());
    }

    private UserGridMenuItemList createMenuItemList(final XmppURI userURI, final SubscriptionState subscriptionState) {
	final UserGridMenuItemList itemList = new UserGridMenuItemList();
	itemList.addItem(createStartChatMenuItem(userURI));
	switch (subscriptionState) {
	case to:
	    itemList.addItem(createUnsubscribeBuddyMenuItem(userURI));
	    break;
	case both:
	    itemList.addItem(createUnsubscribeBuddyMenuItem(userURI));
	    itemList.addItem(createCancelSubscriptorBuddyMenuItem(userURI));
	    break;
	case from:
	    itemList.addItem(createSubscribeBuddyMenuItem(userURI));
	    itemList.addItem(createCancelSubscriptorBuddyMenuItem(userURI));
	    break;
	case none:
	    itemList.addItem(createSubscribeBuddyMenuItem(userURI));
	    break;
	}
	itemList.addItem(createRemoveBuddyMenuItem(userURI));
	return itemList;
    }

    private UserGridMenuItem<XmppURI> createRemoveBuddyMenuItem(final XmppURI userURI) {
	return new UserGridMenuItem<XmppURI>("cancel-icon", i18n.t("Remove this buddy"),
		new UserGridMenuItemListener() {
		    public void onAction() {
			roster.removeItem(userURI);
		    }
		});
    }

    private UserGridMenuItem<XmppURI> createStartChatMenuItem(final XmppURI userURI) {
	return new UserGridMenuItem<XmppURI>("newchat-icon", i18n.t("Start a chat with this buddy"),
		new UserGridMenuItemListener() {
		    public void onAction() {
			openChat(userURI);
		    }
		});
    }

    private UserGridMenuItem<XmppURI> createSubscribeBuddyMenuItem(final XmppURI userURI) {
	return new UserGridMenuItem<XmppURI>("add-icon", i18n.t("Request to see when this buddy is connected or not"),
		new UserGridMenuItemListener() {
		    public void onAction() {
			subscriptionManager.requestSubscribe(userURI);
		    }
		});
    }

    private UserGridMenuItem<XmppURI> createUnsubscribeBuddyMenuItem(final XmppURI userURI) {
	return new UserGridMenuItem<XmppURI>("del-icon", i18n.t("Stop to see when this buddy is connected or not"),
		new UserGridMenuItemListener() {
		    public void onAction() {
			subscriptionManager.unsubscribe(userURI);
		    }
		});
    }

    private void createXmppListeners() {
	roster.onRosterRetrieved(new Listener<Collection<RosterItem>>() {
	    public void onEvent(final Collection<RosterItem> parameter) {
		refreshRoster(parameter);
	    }
	});

	roster.onItemChanged(new Listener<RosterItem>() {
	    public void onEvent(final RosterItem item) {
		final ChatUserUI user = rosterMap.get(item.getJID());
		if (user == null) {
		    Log.error("Trying to update a user is not in roster: " + item.getJID() + " ----> Roster: "
			    + rosterMap);
		} else {
		    logRosterItem("Updating", item);
		    updateUserWithRosterItem(user, item);
		    refreshRosterItemInView(item, user, showUnavailableItems);
		}
	    }
	});

	roster.onItemAdded(new Listener<RosterItem>() {
	    public void onEvent(final RosterItem item) {
		addRosterItem(item);
	    }
	});
	roster.onItemRemoved(new Listener<RosterItem>() {
	    public void onEvent(final RosterItem item) {
		final ChatUserUI user = rosterMap.get(item.getJID());
		if (user == null) {
		    Log.error("Trying to delete a user is not in roster: " + item.getJID() + " ----> Roster: "
			    + rosterMap);
		} else {
		    logRosterItem("Removing", item);
		    view.removeRosterItem(user);
		}
	    }
	});
    }

    private void logRosterItem(final String operation, final RosterItem item) {
	final String name = item.getName();
	Log.info(operation + " roster item: " + item.getJID() + ", name: " + name + ", subsc: "
		+ item.getSubscriptionState() + ", available: " + item.isAvailable() + ", show: " + item.getShow()
		+ ", status: " + item.getStatus());
    }

    private void refreshRoster(final Collection<RosterItem> rosterItems) {
	rosterMap.clear();
	view.clearRoster();
	for (final RosterItem item : rosterItems) {
	    addRosterItem(item);
	}
    }

    private void updateUserWithRosterItem(final ChatUserUI user, final RosterItem item) {
	user.setStatusIcon(getPresenceIcon(item));
	user.setStatusText(formatRosterItemStatusText(item));
    }

}
