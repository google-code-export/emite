package com.calclab.examplechat.client.chatuiplugin.pairchat;

import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.examplechat.client.chatuiplugin.abstractchat.AbstractChatUser;

public class PairChatUser extends AbstractChatUser {

    private final Presence presence;

    // public PairChatUser(final String iconUrl, final String jid, final String
    // alias, final String color) {
    // super(iconUrl, jid, alias, color);
    // this.presence = new Presence();
    // }

    public PairChatUser(final String iconUrl, final String jid, final String alias, final String color,
            final Presence presence) {
        super(iconUrl, jid, alias, color);
        this.presence = presence;
    }

    public Presence getPresence() {
        return presence;
    }

}
