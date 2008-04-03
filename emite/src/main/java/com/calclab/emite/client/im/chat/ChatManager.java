package com.calclab.emite.client.im.chat;

import java.util.Collection;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public interface ChatManager {

    public abstract void addListener(final ChatManagerListener listener);

    public abstract Collection<ChatDefault> getChats();

    public abstract Chat newChat(final XmppURI xmppURI);

}