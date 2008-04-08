package com.calclab.examplechat.client.chatuiplugin.roster;

import java.util.Collection;
import java.util.HashMap;

import org.ourproject.kune.platf.client.View;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.AbstractXmpp;
import com.calclab.emite.client.im.presence.PresenceListener;
import com.calclab.emite.client.im.roster.RosterItem;
import com.calclab.emite.client.im.roster.RosterListener;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.examplechat.client.chatuiplugin.AbstractPresenter;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;

public class RosterUIPresenter extends AbstractPresenter implements RosterUI {

    private RosterUIView view;
    private final AbstractXmpp xmpp;
    private final HashMap<String, PairChatUser> rosterMap;

    public RosterUIPresenter(final AbstractXmpp xmpp) {
        this.xmpp = xmpp;
        rosterMap = new HashMap<String, PairChatUser>();
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
        xmpp.getPresenceManager().acceptSubscription(presence);
    }

    public void onPresenceNotAccepted(final Presence presence) {
        xmpp.getPresenceManager().denySubscription(presence);
    }

    private void createXmppListeners() {
        xmpp.getRoster().addListener(new RosterListener() {
            public void onItemPresenceChanged(final RosterItem item) {
                PairChatUser user = rosterMap.get(item.getXmppURI().getJid());
                if (user == null) {
                    Log.error("Trying to update a user non in roster");
                } else {
                    view.updateRosterItem(user);
                }
            }

            public void onRosterInitialized(final Collection<RosterItem> roster) {
                for (final RosterItem item : roster) {
                    final String name = item.getName();
                    Log.info("Rooster, adding: " + item.getXmppURI() + " name: " + name + " subsc: "
                            + item.getSubscription());
                    PairChatUser user = new PairChatUser("images/person-def.gif", item);
                    rosterMap.put(user.getUri().getJid(), user);
                    view.addRosterItem(user);
                }
            }
        });

        xmpp.getPresenceManager().addListener(new PresenceListener() {
            public void onPresenceReceived(final Presence presence) {
                Log.debug("PRESENCE: " + presence.getFromURI());
            }

            public void onSubscriptionRequest(final Presence presence) {
                Log.debug("SUBSCRIPTION REQUEST: " + presence);
                view.confirmSusbscriptionRequest(presence);
            }

            public void onUnsubscriptionReceived(final Presence presence) {
                view.removeRosterItem(rosterMap.get(presence.getFromURI().getJid()));
            }
        });
    }
}
