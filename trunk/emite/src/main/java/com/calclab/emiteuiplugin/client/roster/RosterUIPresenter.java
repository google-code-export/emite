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

import org.ourproject.kune.platf.client.View;
import org.ourproject.kune.platf.client.dispatch.DefaultDispatcher;
import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.im.presence.PresenceListener;
import com.calclab.emite.client.im.presence.PresenceManager;
import com.calclab.emite.client.im.roster.Roster;
import com.calclab.emite.client.im.roster.RosterItem;
import com.calclab.emite.client.im.roster.RosterListener;
import com.calclab.emite.client.im.roster.RosterItem.Subscription;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.Presence.Type;
import com.calclab.emiteuiplugin.client.AbstractPresenter;
import com.calclab.emiteuiplugin.client.EmiteUIPlugin;
import com.calclab.emiteuiplugin.client.users.ChatUserUI;
import com.calclab.emiteuiplugin.client.users.UserGridMenuItem;
import com.calclab.emiteuiplugin.client.users.UserGridMenuItemList;

public class RosterUIPresenter implements RosterUI, AbstractPresenter {

    public static final String ON_CANCEL_SUBSCRITOR = "emiteuiplugin.oncancelsubscriptor";
    public static final String ON_REQUEST_REMOVE_ROSTERITEM = "emiteuiplugin.onrequestremoveitem";
    public static final String ON_REQUEST_SUBSCRIBE = "emiteuiplugin.onrequestsubscribeitem";

    private RosterUIView view;
    private final Xmpp xmpp;
    private final HashMap<XmppURI, ChatUserUI> rosterMap;
    private final I18nTranslationService i18n;
    private final PresenceManager presenceManager;
    private final Roster roster;

    public RosterUIPresenter(final Xmpp xmpp, final I18nTranslationService i18n) {
        this.xmpp = xmpp;
        this.i18n = i18n;
        rosterMap = new HashMap<XmppURI, ChatUserUI>();
        presenceManager = xmpp.getPresenceManager();
        roster = xmpp.getRoster();
    }

    public void clearRoster() {
        rosterMap.clear();
        view.clearRoster();
    }

    public void doAction(final String eventName, final Object param) {
        if (eventName.equals(ON_CANCEL_SUBSCRITOR)) {
            final XmppURI userURI = (XmppURI) param;
            presenceManager.cancelSubscriptor(userURI);
            // view.removeRosterItem(getUserByJid(userURI.getJid()));
            // rosterMap.remove(userURI.getJid());
        } else if (eventName.equals(ON_REQUEST_REMOVE_ROSTERITEM)) {
            final XmppURI userURI = (XmppURI) param;
            xmpp.getRosterManager().requestRemoveItem(userURI);
        } else if (eventName.equals(ON_REQUEST_SUBSCRIBE)) {
            final XmppURI userURI = (XmppURI) param;
            xmpp.getPresenceManager().requestSubscribe(userURI);
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
        presenceManager.acceptSubscription(presence);
        // Auto requesting the subscription to contact, I think is useful
        presenceManager.requestSubscribe(presence.getFromURI());
    }

    public void onPresenceNotAccepted(final Presence presence) {
        presenceManager.denySubscription(presence);
    }

    String formatRosterItemStatusText(final Presence presence, final Subscription subscription) {
        String statusText = "";
        if (presence != null) {
            statusText = presence.getStatus();
        }
        if (statusText == null || statusText.equals("null")) {
            statusText = "";
        }
        if (subscription != null) {
            switch (subscription) {
            case both:
            case from:
            case none:
            case to:
                statusText += " (" + subscription.toString() + ")";
            }
        }
        return statusText.equals("") ? " " : statusText;
    }

    UserStatusIcon getPresenceIcon(final Presence presence) {
        switch (presence.getType()) {
        case available:
            switch (presence.getShow()) {
            case chat:
            case away:
            case dnd:
            case xa:
            case available:
                return UserStatusIcon.valueOf(presence.getShow().toString());
            case notSpecified:
                return UserStatusIcon.available;
            }
        case unavailable:
            switch (presence.getShow()) {
            case away:
            case notSpecified:
                return UserStatusIcon.offline;
            case unknown:
                return UserStatusIcon.unknown;
            }
        }
        return UserStatusIcon.unknown;
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
                    case available:
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
        itemList.addItem(createRemoveBuddyMenuItem(userURI));
        return itemList;
    }

    private UserGridMenuItem<XmppURI> createRemoveBuddyMenuItem(final XmppURI userURI) {
        return new UserGridMenuItem<XmppURI>("cancel-icon", i18n.t("Remove this buddy"), ON_REQUEST_REMOVE_ROSTERITEM,
                userURI);
    }

    private UserGridMenuItem<XmppURI> createStartChatMenuItem(final XmppURI userURI) {
        return new UserGridMenuItem<XmppURI>("newchat-icon", i18n.t("Start a chat with this buddy"),
                EmiteUIPlugin.CHATOPEN, userURI);
    }

    private UserGridMenuItem<XmppURI> createSubscribeBuddyMenuItem(final XmppURI userURI) {
        return new UserGridMenuItem<XmppURI>("add-icon", i18n.t("Request to see when this buddy is connected or not"),
                ON_REQUEST_SUBSCRIBE, userURI);
    }

    private UserGridMenuItem<XmppURI> createUnsubscribeBuddyMenuItem(final XmppURI userURI) {
        return new UserGridMenuItem<XmppURI>("del-icon", i18n.t("Stop to see when this buddy is connected or not"),
                ON_CANCEL_SUBSCRITOR, userURI);
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
                    view.updateRosterItem(user, createMenuItemList(item));
                }
            }

            public void onRosterChanged(final Collection<RosterItem> roster) {
                rosterMap.clear();
                view.clearRoster();
                for (final RosterItem item : roster) {
                    logRosterItem("Adding", item);
                    final ChatUserUI user = new ChatUserUI("images/person-def.gif", item, "black");
                    user.setStatusIcon(getPresenceIcon(item.getPresence()));
                    rosterMap.put(user.getURI(), user);
                    view.addRosterItem(user, createMenuItemList(item));
                }
                DefaultDispatcher.getInstance().fire(EmiteUIPlugin.ON_ROSTER_CHANGED, roster);
            }

            private void logRosterItem(final String operation, final RosterItem item) {
                final String name = item.getName();
                final Presence presence = item.getPresence();
                Log.info(operation + " roster item: " + item.getJID() + " name: " + name + " subsc: "
                        + item.getSubscription());
                if (presence != null) {
                    logPresence(presence);
                } else {
                    Log.info("with null presence");
                }
            }
        });

        presenceManager.addListener(new PresenceListener() {
            public void onPresenceReceived(final Presence presence) {
                logPresence(presence);
            }

            public void onSubscribedReceived(final Presence presence) {
                Log.info("SUBS RECEIVED");
                // RosterItem changed event fired?
            }

            public void onSubscriptionRequest(final Presence presence) {
                switch (xmpp.getRoster().getSubscriptionMode()) {
                case auto_accept_all:
                    Log.info("Accepting because we are auto accepting");
                    onPresenceAccepted(presence);
                    break;
                case auto_reject_all:
                    Log.info("Rejecting because we are auto rejecting");
                    onPresenceNotAccepted(presence);
                    break;
                default:
                    Log.info("Manual accept/reject");
                    view.confirmSusbscriptionRequest(presence);
                    break;
                }
            }

            public void onUnsubscribedReceived(final Presence presence) {
                Log.info("UNSUBS RECEIVED");
                // Inform about unsubscription?
                // RosterItem changed event fired?
            }
        });
    }

    private void logPresence(final Presence presence) {
        Log.info("PRESENCE: type: " + presence.getType() + " from: " + presence.getFrom() + " show: "
                + presence.getShow() + " status: " + presence.getStatus());
    }
}
