package com.calclab.emite.client.xmpp.session;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public interface SessionLifecycle {
    public void logIn(XmppURI uri);

    public void logOut();
}
