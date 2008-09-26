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
import com.calclab.suco.client.listener.Listener;

/**
 * <p>
 * The ChatManager is the object that allows you to chat with other people and
 * mantain different conversations.
 * </p>
 * 
 * 
 */
public interface ChatManager {

    public void close(Chat chat);

    public Collection<? extends Chat> getChats();

    public void onChatClosed(Listener<Chat> listener);

    public void onChatCreated(Listener<Chat> listener);

    public <T> Chat openChat(final XmppURI xmppURI, Class<T> dataType, T dataValue);

}