package com.calclab.emite.client.im.chat;

import java.util.HashMap;

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.signal.Listener;
import com.calclab.emite.client.core.signal.Signal;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public abstract class AbstractChat implements Chat {

    protected final MessageInterceptorCollection interceptors;
    protected final ChatListenerCollection listeners;
    protected final Emite emite;
    protected final XmppURI from;
    protected final XmppURI other;
    protected Status status;
    protected final Signal<Status> onStateChanged;
    private final HashMap<Class<?>, Object> data;

    public AbstractChat(final XmppURI from, final XmppURI other, final Emite emite) {
	this.emite = emite;
	this.from = from;
	this.other = other;
	this.interceptors = new MessageInterceptorCollection();
	this.listeners = new ChatListenerCollection();
	this.data = new HashMap<Class<?>, Object>();
	this.status = Chat.Status.locked;
	this.onStateChanged = new Signal<Status>();
    }

    public void addListener(final ChatListener listener) {
	listeners.add(listener);
    }

    public void addMessageInterceptor(final MessageInterceptor messageInterceptor) {
	interceptors.add(messageInterceptor);
    }

    @SuppressWarnings("unchecked")
    public <T> T getData(final Class<T> type) {
	return (T) data.get(type);
    }

    public XmppURI getFromURI() {
	return from;
    }

    public XmppURI getOtherURI() {
	return other;
    }

    public Status getState() {
	return status;
    }

    public void onStateChanged(final Listener<Status> listener) {
	onStateChanged.add(listener);
    }

    public void receive(final Message message) {
	interceptors.onBeforeReceive(message);
	listeners.onMessageReceived(this, message);
    }

    public void send(final Message message) {
	message.setFrom(from);
	message.setTo(other);
	interceptors.onBeforeSend(message);
	emite.send(message);
	listeners.onMessageSent(this, message);
    }

    @Deprecated
    public void send(final String body) {
	final Message message = new Message().Body(body);
	send(message);
    }

    @SuppressWarnings("unchecked")
    public <T> T setData(final Class<T> type, final T value) {
	return (T) data.put(type, value);
    }

    protected void setState(final Status status) {
	this.status = status;
	onStateChanged.fire(status);
    }
}
