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

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.events.Listener;

/**
 * Create and manage chat conversations.
 * 
 * There are one implementation for one-to-one conversations (ChatManagerImpl)
 * and many-to-many conversations (RoomManagerImpl)
 */
public interface ChatManager {

    /**
     * Close the given conversation. If a conversation is closed, a new
     * onChatCreated event will be throw when opened
     * 
     * @param conversation
     */
    public void close(Conversation conversation);

    public Collection<? extends Conversation> getChats();

    public void onChatClosed(Listener<Conversation> listener);

    public void onChatCreated(Listener<Conversation> listener);

    /**
     * Same as openChat(uri, null, null);
     * 
     * @see openChat
     */
    public Conversation openChat(XmppURI uri);

    // FIXME: no está nada claro...
    /**
     * Get the Conversation associatted to the given uri. If the conversation
     * does not exist, it creates a new one and send the onChatCreated event to
     * the listeners.
     * 
     * 
     * @param <T>
     * @param uri
     * @param dataType
     * @param dataValue
     * @return
     */
    @Deprecated
    public <T> Conversation openChat(final XmppURI uri, Class<T> dataType, T dataValue);

}
