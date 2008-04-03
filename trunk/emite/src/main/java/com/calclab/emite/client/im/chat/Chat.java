package com.calclab.emite.client.im.chat;

import java.util.ArrayList;

import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public class Chat {
    private final String from;
    private final boolean hasThread;
    private final String id;
    private final ArrayList<ChatListener> listeners;
    private final ChatManager manager;
    private final String to;

    public Chat(final XmppURI other, final XmppURI myself, final String thread, final ChatManager manager) {
	this.from = myself.toString();
	this.to = other.toString();
	this.manager = manager;
	this.listeners = new ArrayList<ChatListener>();
	this.hasThread = !(thread == null || thread.length() == 0);
	this.id = other.toString() + (hasThread ? thread : "");
    }

    public void addListener(final ChatListener listener) {
	listeners.add(listener);
    }

    public String getID() {
	return id;
    }

    public void send(final String body) {
	final Message message = new Message(from, to, body);
	manager.sendMessage(message);
	for (final ChatListener listener : listeners) {
	    listener.onMessageSent(this, message);
	}
    }

    void process(final Message message) {
	for (final ChatListener listener : listeners) {
	    listener.onMessageReceived(this, message);
	}
    }

}
