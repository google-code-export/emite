package com.calclab.emite.client.im.chat;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public interface Chat {

    public void addListener(final ChatListener listener);

    public String getID();

    public XmppURI getOtherURI();

    public String getThread();

    public void send(final String body);

}