package com.calclab.emiteuiplugin.client.params;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public interface AvatarProvider {
    String getAvatarURL(XmppURI userURI);
}
