package com.calclab.examplechat.client.chatuiplugin.roster;

import org.ourproject.kune.platf.client.View;

import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;

public interface RosterUIView extends View {

    void removeRosterItem(PairChatUser pairChatUser);

    void confirmSusbscriptionRequest(Presence presence);

    void addRosterItem(PairChatUser user);

    void updateRosterItem(PairChatUser user);

    void clearRoster();

}
