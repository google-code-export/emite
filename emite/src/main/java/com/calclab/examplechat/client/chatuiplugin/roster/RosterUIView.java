package com.calclab.examplechat.client.chatuiplugin.roster;

import org.ourproject.kune.platf.client.View;

import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;
import com.calclab.examplechat.client.chatuiplugin.users.UserGridMenuItemList;

public interface RosterUIView extends View {

    void removeRosterItem(PairChatUser pairChatUser);

    void confirmSusbscriptionRequest(Presence presence);

    void addRosterItem(PairChatUser user, UserGridMenuItemList menuItemList);

    void updateRosterItem(PairChatUser user, UserGridMenuItemList menuItemList);

    void clearRoster();

}
