package com.calclab.emite.client.im.chat;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public interface Chat {

    public abstract void addListener(final ChatListener listener);

    public abstract XmppURI getOtherURI();

    public abstract String getID();

    public abstract void send(final String body);

}