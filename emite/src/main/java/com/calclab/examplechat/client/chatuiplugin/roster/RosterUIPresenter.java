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
package com.calclab.examplechat.client.chatuiplugin.roster;

import java.util.Collection;
import java.util.HashMap;

import org.ourproject.kune.platf.client.View;
import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.AbstractXmpp;
import com.calclab.emite.client.im.presence.PresenceListener;
import com.calclab.emite.client.im.presence.PresenceManager;
import com.calclab.emite.client.im.roster.Roster;
import com.calclab.emite.client.im.roster.RosterItem;
import com.calclab.emite.client.im.roster.RosterListener;
import com.calclab.emite.client.im.roster.RosterItem.Subscription;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.Presence.Type;
import com.calclab.examplechat.client.chatuiplugin.AbstractPresenter;
import com.calclab.examplechat.client.chatuiplugin.EmiteUiPlugin;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;
import com.calclab.examplechat.client.chatuiplugin.users.UserGridMenuItem;
import com.calclab.examplechat.client.chatuiplugin.users.UserGridMenuItemList;

public class RosterUIPresenter extends AbstractPresenter implements RosterUI {

    private RosterUIView view;
    private final AbstractXmpp xmpp;
    private final HashMap<String, PairChatUser> rosterMap;
    private final I18nTranslationService i18n;
    private final PresenceManager presenceManager;
    private final Roster roster;

    public RosterUIPresenter(final AbstractXmpp xmpp, final I18nTranslationService i18n) {
        this.xmpp = xmpp;
        this.i18n = i18n;
        rosterMap = new HashMap<String, PairChatUser>();
        presenceManager = xmpp.getPresenceManager();
        roster = xmpp.getRoster();
    }

    public void init(final RosterUIView view) {
        this.view = view;
        createXmppListeners();
    }

    public PairChatUser getUserByJid(final String jid) {
        return rosterMap.get(jid);
    }

    public View getView() {
        return view;
    }

    public void clearRoster() {
        rosterMap.clear();
        view.clearRoster();
    }

    public void onPresenceAccepted(final Presence presence) {
        presenceManager.acceptSubscription(presence);
    }

    public void onPresenceNotAccepted(final Presence presence) {
        presenceManager.denySubscription(presence);
    }

    private void createXmppListeners() {
        roster.addListener(new RosterListener() {
            public void onItemPresenceChanged(final RosterItem item) {
                PairChatUser user = rosterMap.get(item.getXmppURI().getJID());
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
                    PairChatUser user = new PairChatUser("images/person-def.gif", item);
                    rosterMap.put(user.getJid(), user);
                    view.addRosterItem(user, createMenuItemList(item));
                }
            }

            private void logRosterItem(final String operation, final RosterItem item) {
                final String name = item.getName();
                Presence presence = item.getPresence();
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
                // PairChatUser user =
                // rosterMap.get(presence.getFromURI().getJID());
                // if (user != null) {
                // RosterItem rosterItem = roster.findItemByURI(user.getUri());
                // if (rosterItem != null) {
                // view.addRosterItem(user, createMenuItemList(rosterItem));
                // } else {
                // Log.error("Rootitem, not found of Presence received");
                // }
                // } else {
                // Log.error("User not found of Presence received");
                // }
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
                default:
                    Log.info("Manual accept/reject");
                    view.confirmSusbscriptionRequest(presence);
                    break;
                }
            }

            public void onUnsubscriptionReceived(final Presence presence) {
                Log.info("UNSUBS RECEIVED");
                view.removeRosterItem(rosterMap.get(presence.getFromURI().getJID()));
            }
        });
    }

    private UserGridMenuItemList createMenuItemList(final RosterItem item) {
        Type statusType;
        UserGridMenuItemList itemList = new UserGridMenuItemList();
        Presence presence = item.getPresence();
        Subscription subscription = item.getSubscription();
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
                        itemList.addItem(createRemoveBuddyMenuItem(item));
                        break;
                    default:
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
                    itemList.addItem(createRemoveBuddyMenuItem(item));
                }
                break;
            case unavailable:
                itemList.addItem(createRemoveBuddyMenuItem(item));
                break;
            case unsubscribed:
            case subscribed:
                itemList.addItem(createStartChatMenuItem(item));
                itemList.addItem(createRemoveBuddyMenuItem(item));
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
        case none:
            itemList.addItem(createStartChatMenuItem(item));
            break;
        default:
            Log.error("Programatic error, subscription: " + subscription);
        }
        return itemList;
    }

    private UserGridMenuItem<XmppURI> createRemoveBuddyMenuItem(final RosterItem item) {
        return new UserGridMenuItem<XmppURI>("del-icon", i18n.t("Remove from my buddies list"),
                EmiteUiPlugin.ON_CANCEL_SUBSCRITOR, item.getXmppURI());
    }

    private UserGridMenuItem<XmppURI> createStartChatMenuItem(final RosterItem item) {
        return new UserGridMenuItem<XmppURI>("newchat-icon", i18n.t("Start a chat with this buddie"),
                EmiteUiPlugin.ON_PAIR_CHAT_START, item.getXmppURI());
    }

    @Override
    public void doAction(final String eventName, final Object param) {
        if (eventName.equals(EmiteUiPlugin.ON_CANCEL_SUBSCRITOR)) {
            XmppURI userURI = (XmppURI) param;
            presenceManager.cancelSubscriptor(userURI);
            // view.removeRosterItem(getUserByJid(userURI.getJid()));
            // rosterMap.remove(userURI.getJid());
        } else {

        }
        super.doAction(eventName, param);
    }

    private void logPresence(final Presence presence) {
        Log.info("PRESENCE: type: " + presence.getType() + " from: " + presence.getFrom() + " show: "
                + presence.getShow() + " status: " + presence.getStatus() + " text: " + presence.getText());
    }
}
