package com.calclab.examplechat.client.chatuiplugin.roster;

import org.ourproject.kune.platf.client.View;

import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;

public interface RosterUI {

    PairChatUser getUserByJid(String jid);

    View getView();

    void clearRoster();

}
