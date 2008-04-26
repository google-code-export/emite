package com.calclab.emiteuiplugin.client.users;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public interface UserGridDropListener {
    void onDrop(XmppURI userURI);
}
