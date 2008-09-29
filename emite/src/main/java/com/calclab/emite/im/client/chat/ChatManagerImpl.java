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
package com.calclab.emite.im.client.chat;

import java.util.Collection;
import java.util.HashSet;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.Message.Type;
import com.calclab.emite.im.client.chat.Conversation.State;
import com.calclab.suco.client.listener.Event;
import com.calclab.suco.client.listener.Listener;

/**
 * Default ChatManager implementation. Use ChatManager interface instead
 * 
 * @author dani
 * 
 */
public class ChatManagerImpl implements ChatManager {
    protected final HashSet<Conversation> conversations;
    protected final Event<Conversation> onChatCreated;
    protected Event<Conversation> onChatClosed;
    protected final Session session;
    private XmppURI lastLoggedInUser;

    public ChatManagerImpl(final Session session) {
	this.session = session;
	this.onChatCreated = new Event<Conversation>("chatManager:onChatCreated");
	this.onChatClosed = new Event<Conversation>("chatManager:onChatClosed");
	this.conversations = new HashSet<Conversation>();

	session.onMessage(new Listener<Message>() {
	    public void onEvent(final Message message) {
		eventMessage(message);
	    }
	});

	session.onStateChanged(new Listener<Session.State>() {
	    public void onEvent(final Session.State state) {
		if (state == Session.State.loggedIn) {
		    unlockChatsIfSameResource(session.getCurrentUser());
		} else if (state == Session.State.disconnected) {
		    lockAllChats();
		}
	    }
	});

    }

    public void close(final Conversation conversation) {
	conversations.remove(conversation);
	((AbstractChat) conversation).setState(State.locked);
	onChatClosed.fire(conversation);
    }

    public Collection<? extends Conversation> getChats() {
	return conversations;
    }

    public void onChatClosed(final Listener<Conversation> listener) {
	onChatClosed.add(listener);
    }

    public void onChatCreated(final Listener<Conversation> listener) {
	onChatCreated.add(listener);
    }

    public <T> Conversation openChat(final XmppURI toURI, final Class<T> extraType, final T extraData) {
	Conversation conversation = findChat(toURI, null);
	if (conversation == null) {
	    final String thread = String.valueOf(Math.random() * 1000000);
	    conversation = createChat(toURI, thread, extraType, extraData);
	} else {
	    conversation.setData(extraType, extraData);
	}
	return conversation;
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

    private <T> Conversation createChat(final XmppURI toURI, final String thread, final Class<T> extraType, final T extraData) {
	final ChatImpl chat = new ChatImpl(session, toURI, thread);
	if (extraType != null) {
	    chat.setData(extraType, extraData);
	}
	conversations.add(chat);
	onChatCreated.fire(chat);
	chat.setState(Conversation.State.ready);
	return chat;
    }

    /**
     * We choose a chat by the thread, if no thread specified we look for the
     * same XmppURI, and if no resource specified we look for the same node@domain
     * 
     * @param from
     * @param thread
     * @return
     */

    private Conversation findChat(final XmppURI from, final String thread) {
	Conversation selected = null;

	for (final Conversation conversation : conversations) {
	    if (thread != null) {
		if (thread.equals(conversation.getThread())) {
		    return conversation;
		}
	    } else {
		final XmppURI chatTargetURI = conversation.getOtherURI();
		if (from.hasResource() && from.equals(chatTargetURI)) {
		    selected = conversation;
		} else if (from.equalsNoResource(chatTargetURI)) {
		    selected = conversation;
		}
	    }
	}

	return selected;
    }

    private void lockAllChats() {
	for (final Conversation conversation : conversations) {
	    ((AbstractChat) conversation).setState(State.locked);
	}
    }

    private void onChatMessageReceived(final Message message) {
	final XmppURI from = message.getFrom();
	final String thread = message.getThread();

	Conversation conversation = findChat(from, thread);
	if (conversation == null) {
	    conversation = createChat(from, thread, null, null);
	}
	conversation.receive(message);
    }

    private void unlockChatsIfSameResource(final XmppURI uri) {
	if (uri.equalsNoResource(lastLoggedInUser)) {
	    for (final Conversation conversation : conversations) {
		((AbstractChat) conversation).setState(State.ready);
	    }
	}
	this.lastLoggedInUser = uri;
    }

}
