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
package com.calclab.emiteui.client.emiteuiplugin.roster;

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
import com.calclab.emiteui.client.emiteuiplugin.AbstractPresenter;
import com.calclab.emiteui.client.emiteuiplugin.EmiteUiPlugin;
import com.calclab.emiteui.client.emiteuiplugin.users.ChatUserUI;
import com.calclab.emiteui.client.emiteuiplugin.users.UserGridMenuItem;
import com.calclab.emiteui.client.emiteuiplugin.users.UserGridMenuItemList;
import com.calclab.emiteui.client.emiteuiplugin.utils.XmppJID;

public class RosterUIPresenter implements RosterUI, AbstractPresenter {

    private RosterUIView view;
    private final Xmpp xmpp;
    private final HashMap<XmppJID, ChatUserUI> rosterMap;
    private final I18nTranslationService i18n;
    private final PresenceManager presenceManager;
    private final Roster roster;

    public RosterUIPresenter(final Xmpp xmpp, final I18nTranslationService i18n) {
        this.xmpp = xmpp;
        this.i18n = i18n;
        rosterMap = new HashMap<XmppJID, ChatUserUI>();
        presenceManager = xmpp.getPresenceManager();
        roster = xmpp.getRoster();
    }

    public void clearRoster() {
        rosterMap.clear();
        view.clearRoster();
    }

    public void doAction(final String eventName, final Object param) {
        if (eventName.equals(EmiteUiPlugin.ON_CANCEL_SUBSCRITOR)) {
            final XmppURI userURI = (XmppURI) param;
            presenceManager.cancelSubscriptor(userURI);
            // view.removeRosterItem(getUserByJid(userURI.getJid()));
            // rosterMap.remove(userURI.getJid());
        } else if (eventName.equals(EmiteUiPlugin.ON_REQUEST_REMOVE_ROSTERITEM)) {
            final XmppURI userURI = (XmppURI) param;
            xmpp.getRosterManager().requestRemoveItem(userURI.toString());
        }
        DefaultDispatcher.getInstance().fire(eventName, param);
    }

    public ChatUserUI getUserByJid(final XmppJID jid) {
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

    private UserGridMenuItemList createMenuItemList(final RosterItem item) {
        Type statusType;
        final UserGridMenuItemList itemList = new UserGridMenuItemList();
        final Presence presence = item.getPresence();
        final Subscription subscription = item.getSubscription();
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
                itemList.addItem(createStartChatMenuItem(item));
                if (presence.getShow() != null) {
                    switch (presence.getShow()) {
                    case available:
                    case chat:
                    case dnd:
                    case xa:
                    case away:
                        itemList.addItem(createUnsubscribeBuddyMenuItem(item));
                        break;
                    default:
                        // FIXME: vicente, nunca llegará uno que no esté
                        // definido en el enum Show ;) para eso son los enums
                        Log.debug("Status unknown, show: " + presence.getShow());
                    }

                } else {
                    /*
                     * 2.2.2.1. Show
                     * 
                     * If no <show/> element is provided, the entity is assumed
                     * to be online and available.
                     * 
                     */
                    itemList.addItem(createUnsubscribeBuddyMenuItem(item));
                }
                break;
            case unavailable:
                itemList.addItem(createUnsubscribeBuddyMenuItem(item));
                break;
            case subscribed:
                itemList.addItem(createStartChatMenuItem(item));
                itemList.addItem(createUnsubscribeBuddyMenuItem(item));
                break;
            case unsubscribed:
                itemList.addItem(createStartChatMenuItem(item));
                itemList.addItem(createSubscribeBuddyMenuItem(item));
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
                itemList.addItem(createStartChatMenuItem(item));
            }
            break;
        case from:
            itemList.addItem(createStartChatMenuItem(item));
        case none:
            itemList.addItem(createSubscribeBuddyMenuItem(item));
            break;
        default:
            Log.error("Code bug, subscription: " + subscription);
        }
        itemList.addItem(createRemoveBuddyMenuItem(item));
        return itemList;
    }

    private UserGridMenuItem<XmppURI> createRemoveBuddyMenuItem(final RosterItem item) {
        return new UserGridMenuItem<XmppURI>("cancel-icon", i18n.t("Remove this buddy"),
                EmiteUiPlugin.ON_REQUEST_REMOVE_ROSTERITEM, item.getXmppURI());
    }

    private UserGridMenuItem<XmppURI> createStartChatMenuItem(final RosterItem item) {
        return new UserGridMenuItem<XmppURI>("newchat-icon", i18n.t("Start a chat with this buddy"),
                EmiteUiPlugin.ON_PAIR_CHAT_START, item.getXmppURI());
    }

    private UserGridMenuItem<XmppURI> createSubscribeBuddyMenuItem(final RosterItem item) {
        return new UserGridMenuItem<XmppURI>("add-icon", i18n.t("Request to see when this buddy is connected or not"),
                EmiteUiPlugin.ON_REQUEST_SUBSCRIBE, item.getXmppURI());
    }

    private UserGridMenuItem<XmppURI> createUnsubscribeBuddyMenuItem(final RosterItem item) {
        return new UserGridMenuItem<XmppURI>("del-icon", i18n.t("Stop to see when this buddy is connected or not"),
                EmiteUiPlugin.ON_CANCEL_SUBSCRITOR, item.getXmppURI());
    }

    private void createXmppListeners() {
        roster.addListener(new RosterListener() {
            public void onItemPresenceChanged(final RosterItem item) {
                final ChatUserUI user = rosterMap.get(new XmppJID(item.getXmppURI()));
                if (user == null) {
                    Log.error("Trying to update a user is not in roster: " + item.getXmppURI() + " ----> Roster: "
                            + rosterMap);
                } else {
                    logRosterItem("Updating", item);
                    view.updateRosterItem(user, createMenuItemList(item));
                }
            }

            public void onRosterChanged(final Collection<RosterItem> roster) {
                for (final RosterItem item : roster) {
                    logRosterItem("Adding", item);
                    final ChatUserUI user = new ChatUserUI("images/person-def.gif", item, "black");
                    rosterMap.put(user.getJid(), user);
                    view.addRosterItem(user, createMenuItemList(item));
                }
            }

            private void logRosterItem(final String operation, final RosterItem item) {
                final String name = item.getName();
                final Presence presence = item.getPresence();
                Log.info(operation + " roster item: " + item.getXmppURI() + " name: " + name + " subsc: "
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
                // FIXME: Try to update roster
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
                // FIXME: Try to update roster
                // Wrong: view.removeRosterItem(rosterMap.get(new
                // XmppJID(presence.getFromURI())));
            }
        });
    }

    private void logPresence(final Presence presence) {
        Log.info("PRESENCE: type: " + presence.getType() + " from: " + presence.getFrom() + " show: "
                + presence.getShow() + " status: " + presence.getStatus() + " text: " + presence.getText());
    }
}
