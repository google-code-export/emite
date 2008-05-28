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

import com.calclab.emite.client.core.signal.Listener;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public interface Chat {

    /**
     * Possible chat states.
     * 
     */
    public static enum State {
	ready, locked
    }

    public void addListener(final ChatListener listener);

    public <T> T getData(Class<T> type);

    public XmppURI getFromURI();

    public String getID();

    public XmppURI getOtherURI();

    public State getState();

    public String getThread();

    public void onStateChanged(Listener<State> listener);

    /**
     * To make this chat receive a message
     * 
     * @param message
     *                the message
     */
    public void receive(Message message);

    /**
     * To make this chat send a message
     * 
     * @param message
     *                the message
     * @throws RuntimeException
     *                 if chat state != ready
     */
    public void send(Message message);

    /**
     * To make this chat send a message
     * 
     * @deprecated
     * @see use chat.send(new Message("body"));
     * @param body
     *                message body
     * @throws RuntimeException
     *                 if chat state != ready
     */
    @Deprecated
    public void send(final String body);

    public <T> T setData(Class<T> type, T data);

    /**
     * Allows to modify the messages before send or receive
     * 
     * @param messageInterceptor
     */
    void addMessageInterceptor(MessageInterceptor messageInterceptor);

}
