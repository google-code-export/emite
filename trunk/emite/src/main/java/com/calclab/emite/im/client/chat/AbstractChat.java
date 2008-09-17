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

import java.util.HashMap;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.listener.Event;
import com.calclab.suco.client.listener.Listener;

public abstract class AbstractChat implements Chat {

    protected final XmppURI other;
    protected State state;
    protected final Event<State> onStateChanged;
    protected final Event<Message> onBeforeReceive;
    protected final Session session;
    private final HashMap<Class<?>, Object> data;
    private final Event<Message> onMessageSent;
    private final Event<Message> onMessageReceived;
    private final Event<Message> onBeforeSend;

    public AbstractChat(final Session session, final XmppURI other) {
	this.session = session;
	this.other = other;
	this.data = new HashMap<Class<?>, Object>();
	this.state = Chat.State.locked;
	this.onStateChanged = new Event<State>("chat:onStateChanged");
	this.onMessageSent = new Event<Message>("chat:onMessageSent");
	this.onMessageReceived = new Event<Message>("chat:onMessageReceived");
	this.onBeforeSend = new Event<Message>("chat:onBeforeSend");
	this.onBeforeReceive = new Event<Message>("chat:onBeforeReceive");
    }

    @SuppressWarnings("unchecked")
    public <T> T getData(final Class<T> type) {
	return (T) data.get(type);
    }

    public XmppURI getFromURI() {
	return session.getCurrentUser();
    }

    public XmppURI getOtherURI() {
	return other;
    }

    public State getState() {
	return state;
    }

    public void onBeforeReceive(final Listener<Message> listener) {
	onBeforeReceive.add(listener);
    }

    public void onBeforeSend(final Listener<Message> listener) {
	onBeforeSend.add(listener);
    }

    public void onMessageReceived(final Listener<Message> listener) {
	onMessageReceived.add(listener);
    }

    public void onMessageSent(final Listener<Message> listener) {
	onMessageSent.add(listener);
    }

    public void onStateChanged(final Listener<State> listener) {
	onStateChanged.add(listener);
    }

    public void receive(final Message message) {
	onBeforeReceive.fire(message);
	onMessageReceived.fire(message);
    }

    public void send(final Message message) {
	if (state == State.ready) {
	    message.setFrom(session.getCurrentUser());
	    message.setTo(other);
	    onBeforeSend.fire(message);
	    session.send(message);
	    onMessageSent.fire(message);
	}
    }

    @SuppressWarnings("unchecked")
    public <T> T setData(final Class<T> type, final T value) {
	return (T) data.put(type, value);
    }

    protected void setState(final State state) {
	this.state = state;
	onStateChanged.fire(state);
    }
}
