package com.calclab.emite.client.im.chat;

import java.util.ArrayList;

import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;

class ChatDefault implements Chat {
    private final String from;
    private final boolean hasThread;
    private String id;
    private final ArrayList<ChatListener> listeners;
    private final ChatManagerDefault manager;
    private XmppURI other;
    private final String thread;
    private String to;

    public ChatDefault(final XmppURI other, final XmppURI myself, final String thread, final ChatManagerDefault manager) {
        this.other = other;
        this.thread = thread;
        this.from = myself.toString();
        this.to = other.toString();
        this.manager = manager;
        this.listeners = new ArrayList<ChatListener>();
        this.hasThread = !(thread == null || thread.length() == 0);
        this.id = createID(other, thread);
    }

    public void addListener(final ChatListener listener) {
        listeners.add(listener);
    }

    public String getID() {
        return id;
    }

    public XmppURI getOtherURI() {
        return other;
    }

    public void send(final String body) {
        final Message message = new Message(from, to, body);
        manager.sendMessage(message);
        for (final ChatListener listener : listeners) {
            listener.onMessageSent(this, message);
        }
    }

    public void setOtherURI(final XmppURI otherURI) {
        other = otherURI;
        to = other.toString();
        id = createID(other, thread);
    }

    @Override
    public String toString() {
        return id;
    }

    private String createID(final XmppURI other, final String thread) {
        return "chat: " + other.toString() + (hasThread ? "-" + thread : "");
    }

    String getThread() {
        return thread;
    }

    void process(final Message message) {
        for (final ChatListener listener : listeners) {
            listener.onMessageReceived(this, message);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (other == null ? 0 : other.hashCode());
        result = prime * result + (thread == null ? 0 : thread.hashCode());
        return result;
    }

}
