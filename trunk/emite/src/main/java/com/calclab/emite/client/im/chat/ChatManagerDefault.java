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
import java.util.Collection;
import java.util.HashSet;

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.bosh.EmiteComponent;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.xmpp.session.SessionManager;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.Message.Type;

public class ChatManagerDefault extends EmiteComponent implements ChatManager {
    protected XmppURI userURI;
    private final ArrayList<ChatManagerListener> listeners;
    private final HashSet<ChatDefault> chats;

    public ChatManagerDefault(final Emite emite) {
	super(emite);
	this.listeners = new ArrayList<ChatManagerListener>();
	this.chats = new HashSet<ChatDefault>();
    }

    public void addListener(final ChatManagerListener listener) {
	listeners.add(listener);

    }

    @Override
    public void attach() {
	when(SessionManager.Events.loggedOut, new PacketListener() {
	    public void handle(final IPacket received) {
		onLoggedOut();
	    }
	});
	when(SessionManager.Events.loggedIn, new PacketListener() {
	    public void handle(final IPacket received) {
		setUserURI(received.getAttribute("uri"));
	    }
	});
	when(new Packet("message", null), new PacketListener() {
	    public void handle(final IPacket received) {
		onMessageReceived(new Message(received));
	    }
	});
    }

    public void close(final Chat chat) {
	chats.remove(chat);
	fireChatClosed(chat);
    }

    public Collection<ChatDefault> getChats() {
	return chats;
    }

    public void onLoggedOut() {
	final HashSet<ChatDefault> remove = chats;
	closeAll(remove);
    }

    public Chat openChat(final XmppURI to) {
	ChatDefault chat = findChat(to, null);
	if (chat == null) {
	    chat = createChat(to, null);
	}
	return chat;
    }

    public void setUserURI(final String uri) {
	this.userURI = XmppURI.parse(uri);
    }

    /**
     * TEMPLATE METHOD PATTERN no me gusta mucho, la verdad... existe demasiada
     * conexión entre ChatManager y RoomManager :(
     * 
     * @param remove
     */
    @Deprecated
    protected void closeAll(final Collection<? extends Chat> remove) {
	userURI = null;
	final ArrayList<Chat> toBeRemoved = new ArrayList<Chat>();
	toBeRemoved.addAll(remove);
	for (final Chat chat : toBeRemoved) {
	    close(chat);
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

    protected void onMessageReceived(final Message message) {
	final Type type = message.getType();
	switch (type) {
	case chat:
	case normal:
	    onChatMessageReceived(message);
	}
    }

    private ChatDefault createChat(final XmppURI from, final String thread) {
	final String theThread = thread != null ? thread : String.valueOf(Math.random() * 1000000);
	final ChatDefault chat = new ChatDefault(from, userURI, theThread, emite);
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
    private ChatDefault findChat(final XmppURI from, final String thread) {
	ChatDefault selected = null;

	for (final ChatDefault chat : chats) {
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

    private void onChatMessageReceived(final Message message) {
	final XmppURI from = message.getFromURI();
	final String thread = message.getThread();

	ChatDefault chat = findChat(from, thread);
	if (chat == null) {
	    chat = createChat(from, thread);
	}
	chat.fireMessageReceived(message);
    }

}
