package com.calclab.emiteuiplugin.client.users;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public interface UserGridListener {
    void onDoubleClick(XmppURI userJid);
}
