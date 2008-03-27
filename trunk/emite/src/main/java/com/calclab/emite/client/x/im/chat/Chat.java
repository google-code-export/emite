package com.calclab.emite.client.x.im.chat;

import java.util.ArrayList;

import com.calclab.emite.client.bosh.Connection;
import com.calclab.emite.client.dispatcher.Dispatcher;
import com.calclab.emite.client.packet.Event;
import com.calclab.emite.client.packet.stanza.Message;

public class Chat {


	private final Dispatcher dispatcher;
	private final ArrayList<MessageListener> listeners;

	public Chat(final Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		this.listeners = new ArrayList<MessageListener>();
	}

	public void addListener(final MessageListener listener) {
		listeners.add(listener);

	}

	public void onReceived(final Message message) {
		for (final MessageListener listener : listeners) {
			listener.onReceived(message);
		}
	}

	public void send(final String to, final String msg) {
		final Message message = new Message(to, msg);
		dispatcher.publish(new Event(Connection.Events.send).With(message));
	}
}
