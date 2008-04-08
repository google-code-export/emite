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
                    Log.error("Trying to update a user non in roster");
                } else {
                    logPresence("Updating", item);
                    view.updateRosterItem(user, createMenuItemList(item));
                }
            }

            public void onRosterChanged(final Collection<RosterItem> roster) {
                for (final RosterItem item : roster) {
                    logPresence("Adding", item);
                    PairChatUser user = new PairChatUser("images/person-def.gif", item);
                    rosterMap.put(user.getUri().getJID(), user);
                    view.addRosterItem(user, createMenuItemList(item));
                }
            }

            private void logPresence(final String operation, final RosterItem item) {
                final String name = item.getName();
                Presence presence = item.getPresence();
                Log.info(operation + " roster item: " + item.getXmppURI() + " name: " + name + " subsc: "
                        + item.getSubscription() + " show: " + (presence == null ? null : presence.getShow()));
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
                itemList.addItem(createStartChatMenuItem(item));
                switch (subscription) {
                case both:
                case to:
                    switch (statusType) {
                    case available:
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
                    case unavailable:
                        break;
                    default:
                        Log.error("Programatic error, show: " + presence.getShow());
                    }
                    break;
                case from:
                case none:
                    break;
                default:
                    Log.error("Programatic error, subscription: " + subscription);
                }
                return itemList;
            }

            private UserGridMenuItem<XmppURI> createRemoveBuddyMenuItem(final RosterItem item) {
                return new UserGridMenuItem<XmppURI>("chat-icon", i18n.t("Remove from my buddies list"),
                        EmiteUiPlugin.ON_CANCEL_SUBSCRITOR, item.getXmppURI());
            }

            private UserGridMenuItem<XmppURI> createStartChatMenuItem(final RosterItem item) {
                return new UserGridMenuItem<XmppURI>("chat-icon", i18n.t("Start a chat with this buddie"),
                        EmiteUiPlugin.ON_PAIR_CHAT_START, item.getXmppURI());
            }
        });

        presenceManager.addListener(new PresenceListener() {
            public void onPresenceReceived(final Presence presence) {
                Log.info("PRESENCE: " + presence.getFromURI());
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
}
