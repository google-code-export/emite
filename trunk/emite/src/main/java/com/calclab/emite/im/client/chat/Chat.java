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

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.listener.Listener;

/**
 * A conversation between two users
 * 
 * @author dani
 * 
 */
public interface Chat {

    /**
     * Possible chat states.
     * 
     */
    public static enum Status {
	ready, locked
    }

    public <T> T getData(Class<T> type);

    public XmppURI getFromURI();

    public String getID();

    public XmppURI getOtherURI();

    public Status getState();

    public String getThread();

    public void onBeforeReceive(Listener<Message> listener);

    /**
     * Allows to modify the message just before send it
     * 
     * @param messageInterceptor
     */
    public void onBeforeSend(Listener<Message> listener);

    /**
     * Allows to modify the message just before inform about the reception
     * 
     * @param messageInterceptor
     */
    public void onMessageReceived(Listener<Message> listener);

    public void onMessageSent(Listener<Message> listener);

    public void onStateChanged(Listener<Status> listener);

    /**
     * To make this chat receive a message
     * 
     * @param message
     *            the message
     */
    public void receive(Message message);

    /**
     * To make this chat send a message
     * 
     * @param message
     *            the message
     * @throws RuntimeException
     *             if chat state != ready
     */
    public void send(Message message);

    public <T> T setData(Class<T> type, T data);

}
