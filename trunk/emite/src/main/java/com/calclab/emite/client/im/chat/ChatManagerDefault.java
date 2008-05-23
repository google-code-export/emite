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

import static com.calclab.emite.client.core.dispatcher.matcher.Matchers.when;
import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.xmpp.session.SessionComponent;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.Message.Type;

public class ChatManagerDefault extends SessionComponent implements ChatManager {
    protected final HashSet<Chat> chats;
    protected final ArrayList<ChatManagerListener> listeners;

    public ChatManagerDefault(final Emite emite) {
        super(emite);
        this.listeners = new ArrayList<ChatManagerListener>();
        this.chats = new HashSet<Chat>();
        install();
    }

    public void addListener(final ChatManagerListener listener) {
        listeners.add(listener);
    }

    public void close(final Chat chat) {
        chats.remove(chat);
        fireChatClosed(chat);
    }

    public Collection<? extends Chat> getChats() {
        return chats;
    }

    @Override
    public void logOut() {
        super.logOut();
        final ArrayList<Chat> toBeRemoved = new ArrayList<Chat>();
        toBeRemoved.addAll(chats);
        for (final Chat chat : toBeRemoved) {
            close(chat);
        }
    }

    public Chat openChat(final XmppURI to) {
        Chat chat = findChat(to, null);
        if (chat == null) {
            final String theThread = String.valueOf(Math.random() * 1000000);
            chat = new ChatDefault(to, userURI, theThread, emite);
            addChat(chat);
        }
        return chat;
    }

    public void setUserURI(final String uri) {
        this.userURI = uri(uri);
    }

    protected void eventMessage(final Message message) {
        final Type type = message.getType();
        switch (type) {
        case chat:
        case normal:
            onChatMessageReceived(message);
            break;
        case error:
            Log.warn("Error message received: " + message.toString());
        }
    }

    protected void fireChatClosed(final Chat chat) {
        for (final ChatManagerListener listener : listeners) {
            listener.onChatClosed(chat);
        }
    }

    protected void fireChatCreated(final Chat chat) {
        for (final ChatManagerListener listener : listeners) {
            listener.onChatCreated(chat);
        }
    }

    private Chat addChat(final Chat chat) {
        chats.add(chat);
        fireChatCreated(chat);
        return chat;
    }

    /**
     * We choose a chat by the thread, if no thread specified we look for the
     * same XmppURI, and if no resource specified we look for the same
     * node@domain
     * 
     * @param from
     * @param thread
     * @return
     */

    private Chat findChat(final XmppURI from, final String thread) {
        Chat selected = null;

        for (final Chat chat : chats) {
            if (thread != null) {
                if (thread.equals(chat.getThread())) {
                    return chat;
                }
            } else {
                final XmppURI chatTargetURI = chat.getOtherURI();
                if (from.hasResource() && from.equals(chatTargetURI)) {
                    selected = chat;
                } else if (from.equalsNoResource(chatTargetURI)) {
                    selected = chat;
                }
            }
        }

        return selected;
    }

    private void install() {
        emite.subscribe(when(new Packet("message", null)), new PacketListener() {
            public void handle(final IPacket received) {
                eventMessage(new Message(received));
            }
        });
    }

    private void onChatMessageReceived(final Message message) {
        final XmppURI from = message.getFromURI();
        final String thread = message.getThread();

        Chat chat = findChat(from, thread);
        if (chat == null) {
            final ChatDefault chat1 = new ChatDefault(from, userURI, thread, emite);
            chat = addChat(chat1);
        }
        ((ChatDefault) chat).fireMessageReceived(message);
    }

}
