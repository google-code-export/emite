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
package com.calclab.emiteuiplugin.client.roster;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.ourproject.kune.platf.client.View;
import org.ourproject.kune.platf.client.dispatch.DefaultDispatcher;
import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.core.packet.TextUtils;
import com.calclab.emite.client.im.chat.ChatManager;
import com.calclab.emite.client.im.presence.PresenceListener;
import com.calclab.emite.client.im.presence.PresenceManager;
import com.calclab.emite.client.im.roster.Roster;
import com.calclab.emite.client.im.roster.RosterItem;
import com.calclab.emite.client.im.roster.RosterListener;
import com.calclab.emite.client.im.roster.RosterManager;
import com.calclab.emite.client.im.roster.RosterManagerListener;
import com.calclab.emite.client.im.roster.RosterItem.Subscription;
import com.calclab.emite.client.im.roster.RosterManager.SubscriptionMode;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.client.xmpp.stanzas.Presence.Type;
import com.calclab.emiteuiplugin.client.AbstractPresenter;
import com.calclab.emiteuiplugin.client.EmiteEvents;
import com.calclab.emiteuiplugin.client.params.AvatarProvider;
import com.calclab.emiteuiplugin.client.users.ChatUserUI;
import com.calclab.emiteuiplugin.client.users.UserGridMenuItem;
import com.calclab.emiteuiplugin.client.users.UserGridMenuItemList;
import com.calclab.emiteuiplugin.client.utils.ChatUIUtils;

public class RosterUIPresenter implements RosterUI, AbstractPresenter {

    public static interface RosterPresenceListener {
	void onOpenChat(XmppURI userURI);

	void onRequestRemoveItem(XmppURI userURI);

	void onRequestSubscribe(XmppURI userURI);

	void onRequestUnsubscribe(XmppURI userURI);

    }
    public static final String ON_CANCEL_SUBSCRITOR = "emiteuiplugin.oncancelsubscriptor";
    public static final String ON_REQUEST_REMOVE_ROSTERITEM = "emiteuiplugin.onrequestremoveitem";
    public static final String ON_REQUEST_SUBSCRIBE = "emiteuiplugin.onrequestsubscribeitem";

    public static final String ON_REQUEST_UNSUBSCRIBE = "emiteuiplugin.onrequestunsubscribeitem";

    private RosterUIView view;
    private final HashMap<XmppURI, ChatUserUI> rosterMap;
    private final I18nTranslationService i18n;
    private final PresenceManager presenceManager;
    private final Roster roster;
    private final RosterManager rosterManager;
    private final AvatarProvider avatarProvider;
    private final ChatManager chatManager;
    private boolean showUnavailableItems;
    private RosterPresenceListener listener;

    public RosterUIPresenter(final Xmpp xmpp, final I18nTranslationService i18n, final AvatarProvider avatarProvider) {
	this.i18n = i18n;
	this.avatarProvider = avatarProvider;
	rosterMap = new HashMap<XmppURI, ChatUserUI>();
	presenceManager = xmpp.getPresenceManager();
	rosterManager = xmpp.getRosterManager();
	roster = xmpp.getRoster();
	chatManager = xmpp.getChatManager();
	showUnavailableItems = false;
    }

    public void clearRoster() {
	rosterMap.clear();
	view.clearRoster();
    }

    public void doAction(final String eventName, final Object param) {
	if (eventName.equals(ON_CANCEL_SUBSCRITOR)) {
	    final XmppURI userURI = (XmppURI) param;
	    rosterManager.cancelSubscriptor(userURI);
	} else if (eventName.equals(ON_REQUEST_UNSUBSCRIBE)) {
	    final XmppURI userURI = (XmppURI) param;
	    rosterManager.requestUnsubscribe(userURI);
	} else if (eventName.equals(ON_REQUEST_REMOVE_ROSTERITEM)) {
	    final XmppURI userURI = (XmppURI) param;
	    rosterManager.requestRemoveItem(userURI);
	} else if (eventName.equals(ON_REQUEST_SUBSCRIBE)) {
	    final XmppURI userURI = (XmppURI) param;
	    rosterManager.requestSubscribe(userURI);
	}
	DefaultDispatcher.getInstance().fire(eventName, param);
    }

    public ChatUserUI getUserByJid(final XmppURI jid) {
	return rosterMap.get(jid);
    }

    public View getView() {
	return view;
    }

    public void init(final RosterUIView view) {
	this.view = view;
	createXmppListeners();
    }

    public void onPresenceAccepted(final Presence presence) {
	rosterManager.acceptSubscription(presence);
    }

    public void onPresenceNotAccepted(final Presence presence) {
	rosterManager.denySubscription(presence);
    }

    public void openChat(final XmppURI userJid) {
	chatManager.openChat(userJid);
    }

    public void setListener(final RosterPresenceListener listener) {
	this.listener = listener;
    }

    public void showUnavailableRosterItems(final boolean show) {
	showUnavailableItems = show;
	for (final Iterator<XmppURI> iterator = rosterMap.keySet().iterator(); iterator.hasNext();) {
	    final XmppURI jid = iterator.next();
	    final RosterItem item = roster.findItemByJID(jid);
	    final ChatUserUI user = rosterMap.get(jid);
	    if (item == null) {
		Log.error("Trying to update a ui roster item not in roster");
	    } else {
		refreshRosterItemInView(item, user, show);
	    }
	}
    }

    String formatRosterItemStatusText(final Presence presence, final Subscription subscription) {
	String statusText = "";
	if (presence != null) {
	    statusText = presence.getStatus();
	}
	if (statusText == null || statusText.equals("null")) {
	    // FIXME: Dani we are receiving "null" as String
	    statusText = "";
	}
	if (presence != null) {
	    final Show show = presence.getShow();
	    if (statusText.equals("")) {
		statusText += ChatUIUtils.getShowText(i18n, show);
	    }
	}
	// Only for test:
	// if (subscription != null) {
	// switch (subscription) {
	// case both:
	// case from:
	// case none:
	// case to:
	// statusText += " (" + subscription.toString() + ")";
	// }
	// }
	return statusText.equals("") ? " " : TextUtils.escape(statusText);
    }

    ChatIconDescriptor getPresenceIcon(final Presence presence) {
	switch (presence.getType()) {
	case available:
	    switch (presence.getShow()) {
	    case chat:
	    case away:
	    case dnd:
	    case xa:
		return ChatIconDescriptor.valueOf(presence.getShow().toString());
	    case notSpecified:
	    case unknown:
		return ChatIconDescriptor.available;
	    }
	case unavailable:
	    switch (presence.getShow()) {
	    case away:
	    case notSpecified:
		return ChatIconDescriptor.offline;
	    case unknown:
		return ChatIconDescriptor.unknown;
	    }
	}
	return ChatIconDescriptor.unknown;
    }

    void refreshRosterItemInView(final RosterItem item, final ChatUserUI user, final boolean showUnavailable) {
	final boolean mustShow = isAvailable(item) || showUnavailable;
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

    private UserGridMenuItem<XmppURI> createCancelSubscriptorBuddyMenuItem(final XmppURI userURI) {
	return new UserGridMenuItem<XmppURI>("del-icon",
		i18n.t("Stop to show when I'm connected or not to this buddy"), ON_CANCEL_SUBSCRITOR, userURI);
    }

    private UserGridMenuItemList createMenuItemList(final RosterItem item) {
	return createMenuItemList(item.getJID(), item.getPresence(), item.getSubscription());
    }

    private UserGridMenuItemList createMenuItemList(final XmppURI userURI, final Presence presence,
	    final Subscription subscription) {
	Type statusType;
	final UserGridMenuItemList itemList = new UserGridMenuItemList();
	if (presence == null) {
	    statusType = Presence.Type.unavailable;
	} else {
	    statusType = presence.getType();
	}
	switch (subscription) {
	case both:
	case to:
	    switch (statusType) {
	    case available:
		itemList.addItem(createStartChatMenuItem(userURI));
		if (presence.getShow() != null) {
		    switch (presence.getShow()) {
		    case chat:
		    case dnd:
		    case xa:
		    case away:
			itemList.addItem(createUnsubscribeBuddyMenuItem(userURI));
			break;
		    }

		} else {
		    /*
		     * 2.2.2.1. Show
		     * 
		     * If no <show/> element is provided, the entity is assumed
		     * to be online and available.
		     * 
		     */
		    itemList.addItem(createUnsubscribeBuddyMenuItem(userURI));
		}
		break;
	    case unavailable:
		itemList.addItem(createUnsubscribeBuddyMenuItem(userURI));
		break;
	    case subscribed:
		itemList.addItem(createStartChatMenuItem(userURI));
		itemList.addItem(createUnsubscribeBuddyMenuItem(userURI));
		break;
	    case unsubscribed:
		itemList.addItem(createStartChatMenuItem(userURI));
		itemList.addItem(createSubscribeBuddyMenuItem(userURI));
		break;
	    default:
		/**
		 * 2.2.1. Types of Presence
		 * 
		 * The 'type' attribute of a presence stanza is OPTIONAL. A
		 * presence stanza that does not possess a 'type' attribute is
		 * used to signal to the server that the sender is online and
		 * available for communication. If included, the 'type'
		 * attribute specifies a lack of availability, a request to
		 * manage a subscription to another entity's presence, a request
		 * for another entity's current presence, or an error related to
		 * a previously-sent presence stanza.
		 */
		itemList.addItem(createStartChatMenuItem(userURI));
	    }
	    break;
	case from:
	    itemList.addItem(createStartChatMenuItem(userURI));
	case none:
	    itemList.addItem(createSubscribeBuddyMenuItem(userURI));
	    break;
	default:
	    Log.error("Code bug, subscription: " + subscription);
	}
	switch (subscription) {
	case both:
	case from:
	    itemList.addItem(createCancelSubscriptorBuddyMenuItem(userURI));
	    break;
	}
	itemList.addItem(createRemoveBuddyMenuItem(userURI));
	return itemList;
    }

    private UserGridMenuItem<XmppURI> createRemoveBuddyMenuItem(final XmppURI userURI) {
	// listener.onRequestRemoveItem(userURI);
	return new UserGridMenuItem<XmppURI>("cancel-icon", i18n.t("Remove this buddy"), ON_REQUEST_REMOVE_ROSTERITEM,
		userURI);
    }

    private UserGridMenuItem<XmppURI> createStartChatMenuItem(final XmppURI userURI) {
	// listener.onOpenChat(userURI);
	// return new UserGridMenuItem<XmppURI>("newchat-icon", i18n.t("Start a
	// chat with this buddy"),
	// EmiteEvents.CHATOPEN, userURI);
	return null;
    }

    private UserGridMenuItem<XmppURI> createSubscribeBuddyMenuItem(final XmppURI userURI) {
	// listener.onRequestSubscribe(userURI);
	return new UserGridMenuItem<XmppURI>("add-icon", i18n.t("Request to see when this buddy is connected or not"),
		ON_REQUEST_SUBSCRIBE, userURI);
    }

    private UserGridMenuItem<XmppURI> createUnsubscribeBuddyMenuItem(final XmppURI userURI) {
	// listener.onRequestUnsubscribe(userURI);
	return new UserGridMenuItem<XmppURI>("del-icon", i18n.t("Stop to see when this buddy is connected or not"),
		ON_REQUEST_UNSUBSCRIBE, userURI);
    }

    private void createXmppListeners() {
	roster.addListener(new RosterListener() {
	    public void onItemChanged(final RosterItem item) {
		final ChatUserUI user = rosterMap.get(item.getJID());
		if (user == null) {
		    Log.error("Trying to update a user is not in roster: " + item.getJID() + " ----> Roster: "
			    + rosterMap);
		} else {
		    logRosterItem("Updating", item);
		    user.setStatusIcon(getPresenceIcon(item.getPresence()));
		    user.setStatusText(formatRosterItemStatusText(item.getPresence(), item.getSubscription()));
		    refreshRosterItemInView(item, user, showUnavailableItems);
		}
	    }

	    public void onRosterChanged(final Collection<RosterItem> roster) {
		rosterMap.clear();
		view.clearRoster();
		for (final RosterItem item : roster) {
		    logRosterItem("Adding", item);
		    final ChatUserUI user = new ChatUserUI(avatarProvider.getAvatarURL(item.getJID()), item, "black");
		    user.setStatusIcon(getPresenceIcon(item.getPresence()));
		    if (showUnavailableItems || isAvailable(item)) {
			user.setVisible(true);
			view.addRosterItem(user, createMenuItemList(item));
		    } else {
			user.setVisible(false);
		    }
		    rosterMap.put(user.getURI(), user);
		}
		// For external use:
		DefaultDispatcher.getInstance().fire(EmiteEvents.ON_ROSTER_CHANGED, roster);
	    }

	    private void logRosterItem(final String operation, final RosterItem item) {
		final String name = item.getName();
		final Presence presence = item.getPresence();
		Log.info(operation + " roster item: " + item.getJID() + ", name: " + name + ", subsc: "
			+ item.getSubscription());
		if (presence != null) {
		    logPresence(presence, "procesed after RosterChanged or RosterItemChanged");
		} else {
		    Log.info("with null presence");
		}
	    }
	});

	rosterManager.addListener(new RosterManagerListener() {
	    public void onSubscriptionRequest(final Presence presence, final SubscriptionMode currentMode) {
		switch (currentMode) {
		case autoAcceptAll:
		    Log.info("Accepting because we are auto accepting");
		    break;
		case autoRejectAll:
		    Log.info("Rejecting because we are auto rejecting");
		    break;
		default:
		    Log.info("Manual accept/reject");
		    // FIXME: Highlight here to advice the user this pending
		    // request...
		    view.confirmSusbscriptionRequest(presence);
		    break;
		}
	    }

	    public void onUnsubscribedReceived(final Presence presence, final SubscriptionMode currentMode) {
		Log.info("UNSUBS RECEIVED");
		view.showMessageAboutUnsuscription(presence);
	    }
	});

	presenceManager.addListener(new PresenceListener() {
	    public void onPresenceReceived(final Presence presence) {
		logPresence(presence, "not processed in RosterUIPresenter presence listener but logged");
	    }
	});

    }

    private boolean isAvailable(final RosterItem item) {
	final Presence presence = item.getPresence();
	switch (presence.getType()) {
	case available:
	    switch (presence.getShow()) {
	    case chat:
	    case away:
	    case dnd:
	    case xa:
	    case notSpecified:
		return true;
	    case unknown:
		return false;
	    }
	case unavailable:
	    switch (presence.getShow()) {
	    case away:
	    case notSpecified:
		return false;
	    case unknown:
		return false;
	    }
	}
	return false;
    }

    private void logPresence(final Presence presence, final String subTitle) {
	Log.info("PRESENCE: type: " + presence.getType() + ", from: " + presence.getFrom() + ", show: "
		+ presence.getShow() + ", status: " + presence.getStatus() + " (" + subTitle + ")");
    }
}
