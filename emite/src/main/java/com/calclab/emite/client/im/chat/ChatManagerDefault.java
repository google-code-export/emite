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

import java.util.Collection;
import java.util.HashSet;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.im.chat.Chat.Status;
import com.calclab.emite.client.xmpp.session.SessionComponent;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.Message.Type;
import com.calclab.modular.client.signal.Slot;
import com.calclab.modular.client.signal.Signal;

public class ChatManagerDefault extends SessionComponent implements ChatManager {
    protected final HashSet<Chat> chats;
    protected final ChatManagerListenerCollection listeners;
    protected final Signal<Chat> onChatCreated;
    protected Signal<Chat> onChatClosed;
    private XmppURI lastLoggedInUser;

    public ChatManagerDefault(final Emite emite) {
	super(emite);
	this.onChatCreated = new Signal<Chat>();
	this.onChatClosed = new Signal<Chat>();
	this.listeners = new ChatManagerListenerCollection();
	this.chats = new HashSet<Chat>();
	install();
    }

    /**
     * New signal system
     * 
     * @see onChatCreated
     */
    @Deprecated
    public void addListener(final ChatManagerListener listener) {
	listeners.add(listener);
    }

    public void close(final Chat chat) {
	chats.remove(chat);
	((AbstractChat) chat).setState(Status.locked);
	listeners.onChatClosed(chat);
	onChatClosed.fire(chat);
    }

    public Collection<? extends Chat> getChats() {
	return chats;
    }

    @Override
    public void logIn(final XmppURI uri) {
	super.logIn(uri);
	if (uri.equalsNoResource(lastLoggedInUser)) {
	    for (final Chat chat : chats) {
		((AbstractChat) chat).setState(Status.ready);
	    }
	}
	this.lastLoggedInUser = userURI;
    }

    @Override
    public void logOut() {
	super.logOut();
	for (final Chat chat : chats) {
	    ((AbstractChat) chat).setState(Status.locked);
	}
    }

    public void onChatClosed(final Slot<Chat> listener) {
	onChatClosed.add(listener);
    }

    public void onChatCreated(final Slot<Chat> listener) {
	onChatCreated.add(listener);
    }

    public <T> Chat openChat(final XmppURI toURI, final Class<T> extraType, final T extraData) {
	Chat chat = findChat(toURI, null);
	if (chat == null) {
	    final String thread = String.valueOf(Math.random() * 1000000);
	    chat = createChat(toURI, thread, extraType, extraData);
	}
	return chat;
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

    private <T> Chat createChat(final XmppURI toURI, final String thread, final Class<T> extraType, final T extraData) {
	final ChatDefault chat = new ChatDefault(userURI, toURI, thread, emite);
	if (extraType != null) {
	    chat.setData(extraType, extraData);
	}
	chats.add(chat);
	onChatCreated.fire(chat);
	listeners.onChatCreated(chat);
	chat.setState(Chat.Status.ready);
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
	    chat = createChat(from, thread, null, null);
	}
	chat.receive(message);
    }

}
