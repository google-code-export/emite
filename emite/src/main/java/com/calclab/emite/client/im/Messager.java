package com.calclab.emite.client.im;

import java.util.ArrayList;

import com.calclab.emite.client.Engine;
import com.calclab.emite.client.packet.stanza.Message;

public class Messager {
	private final Engine engine;
	private final ArrayList<MessageListener> listeners;

	public Messager(final Engine engine) {
		this.engine = engine;
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
		engine.send(message);
	}

}
