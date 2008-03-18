package com.calclab.emite.client.x.im;

import java.util.ArrayList;

import com.calclab.emite.client.bosh.Connection;
import com.calclab.emite.client.packet.stanza.Message;

public class Chat {
	private final Connection connection;
	private final ArrayList<MessageListener> listeners;

	public Chat(final Connection connection) {
		this.connection = connection;
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
		connection.send(message);
	}

}
