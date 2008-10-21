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

    }

    public void close(final Conversation conversation) {
	conversations.remove(conversation);
	((AbstractConversation) conversation).setState(State.locked);
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
	Conversation conversation = findChat(toURI);
	if (conversation == null) {
	    conversation = createChat(toURI, extraType, extraData);
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
	    final XmppURI from = message.getFrom();

	    Conversation conversation = findChat(from);
	    if (conversation == null) {
		conversation = createChat(from, null, null);
	    }
	    break;
	}
    }

    private <T> Conversation createChat(final XmppURI toURI, final Class<T> extraType, final T extraData) {
	final Chat chat = new Chat(session, toURI, null);
	if (extraType != null) {
	    chat.setData(extraType, extraData);
	}
	conversations.add(chat);
	onChatCreated.fire(chat);
	chat.setState(Conversation.State.ready);
	return chat;
    }

    /**
     * Find a chat using the given uri.
     */

    private Conversation findChat(final XmppURI uri) {
	for (final Conversation conversation : conversations) {
	    final XmppURI chatTargetURI = conversation.getURI();
	    if (uri.equals(chatTargetURI)) {
		return conversation;
	    }
	}
	return null;
    }

}
