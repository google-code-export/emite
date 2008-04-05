package com.calclab.examplechat.client.chatuiplugin.params;

import com.calclab.emite.client.Xmpp;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;

public class MultiChatCreationParam {

    private final Xmpp xmpp;
    private final PairChatUser sessionUser;
    private final String userPassword;

    public MultiChatCreationParam(final Xmpp xmpp, final PairChatUser sessionUser, final String userPassword) {
        this.xmpp = xmpp;
        this.sessionUser = sessionUser;
        this.userPassword = userPassword;
    }

    public Xmpp getXmpp() {
        return xmpp;
    }

    public PairChatUser getSessionUser() {
        return sessionUser;
    }

    public String getUserPassword() {
        return userPassword;
    }

}
