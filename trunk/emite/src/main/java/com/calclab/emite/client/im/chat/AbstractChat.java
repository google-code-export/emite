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

import java.util.HashMap;

import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.signal.Signal;
import com.calclab.suco.client.signal.Slot;

public abstract class AbstractChat implements Chat {

    protected final ChatListenerCollection listeners;
    protected final XmppURI other;
    protected Status status;
    protected final Signal<Status> onStateChanged;
    protected final Signal<Message> onBeforeReceive;
    protected final Session session;
    private final HashMap<Class<?>, Object> data;
    private final Signal<Message> onMessageSent;
    private final Signal<Message> onMessageReceived;
    private final Signal<Message> onBeforeSend;

    public AbstractChat(final Session session, final XmppURI other) {
	this.session = session;
	this.other = other;
	this.listeners = new ChatListenerCollection();
	this.data = new HashMap<Class<?>, Object>();
	this.status = Chat.Status.locked;
	this.onStateChanged = new Signal<Status>("onStateChanged");
	this.onMessageSent = new Signal<Message>("onMessageSent");
	this.onMessageReceived = new Signal<Message>("onMessageReceived");
	this.onBeforeSend = new Signal<Message>("Chat:onBeforeSend");
	this.onBeforeReceive = new Signal<Message>("Chat:onBeforeReceive");
    }

    public void addListener(final ChatListener listener) {
	listeners.add(listener);
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

    public Status getState() {
	return status;
    }

    public void onBeforeReceive(final Slot<Message> slot) {
	onBeforeReceive.add(slot);
    }

    public void onBeforeSend(final Slot<Message> slot) {
	onBeforeSend.add(slot);
    }

    public void onMessageReceived(final Slot<Message> slot) {
	onMessageReceived.add(slot);
    }

    public void onMessageSent(final Slot<Message> slot) {
	onMessageSent.add(slot);
    }

    public void onStateChanged(final Slot<Status> listener) {
	onStateChanged.add(listener);
    }

    public void receive(final Message message) {
	onBeforeReceive.fire(message);
	onMessageReceived.fire(message);
	listeners.onMessageReceived(this, message);
    }

    public void send(final Message message) {
	if (status == Status.ready) {
	    message.setFrom(session.getCurrentUser());
	    message.setTo(other);
	    onBeforeSend.fire(message);
	    session.send(message);
	    onMessageSent.fire(message);
	    listeners.onMessageSent(this, message);
	}
    }

    @SuppressWarnings("unchecked")
    public <T> T setData(final Class<T> type, final T value) {
	return (T) data.put(type, value);
    }

    protected void setStatus(final Status status) {
	this.status = status;
	onStateChanged.fire(status);
    }
}
