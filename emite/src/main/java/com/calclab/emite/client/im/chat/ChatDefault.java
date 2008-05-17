/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.client.im.chat;

import java.util.ArrayList;

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;

/**
 * 
 * About Chat ids: Other sender Uri plus thread identifies a chat (associated
 * with a chat panel in the UI). If no thread is specified, we join all messages
 * in one chat panel.
 * 
 */
class ChatDefault implements Chat {
    protected final XmppURI from;
    protected final XmppURI other;
    protected final String thread;
    private final String id;
    private final ArrayList<ChatListener> listeners;
    private final Emite emite;

    public ChatDefault(final XmppURI other, final XmppURI myself, final String thread, final Emite emite) {
        this.other = other;
        this.thread = thread;
        this.emite = emite;
        this.from = myself;
        this.listeners = new ArrayList<ChatListener>();
        this.id = generateChatID();
    }

    public void addListener(final ChatListener listener) {
        listeners.add(listener);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        final ChatDefault other = (ChatDefault) obj;
        return id.equals(other.id);
    }

    // FIXME: Dani revise this (ChatState)
    public XmppURI getFromURI() {
        return from;
    }

    public String getID() {
        return id;
    }

    public XmppURI getOtherURI() {
        return other;
    }

    public String getThread() {
        return thread;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (other == null ? 0 : other.hashCode());
        result = prime * result + (thread == null ? 0 : thread.hashCode());
        return result;
    }

    public void send(final String body) {
        final Message message = new Message(from, other, body).Thread(thread);
        emite.send(message);
        fireMessageSent(message);
    }

    @Override
    public String toString() {
        return id;
    }

    protected void fireMessageSent(final Message message) {
        for (final ChatListener listener : listeners) {
            listener.onMessageSent(this, message);
        }
    }

    void fireMessageReceived(final Message message) {
        for (final ChatListener listener : listeners) {
            listener.onMessageReceived(this, message);
        }
    }

    private String generateChatID() {
        return "chat: " + other.toString() + "-" + thread;
    }

}
