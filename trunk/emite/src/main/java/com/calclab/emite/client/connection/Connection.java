package com.calclab.emite.client.connection;

import java.util.ArrayList;

import com.calclab.emite.client.Engine;
import com.calclab.emite.client.packet.Event;

public class Connection {
	private final Engine engine;
	private final ArrayList<ConnectionListener> listeners;

	Connection(final Engine engine) {
		this.engine = engine;
		this.listeners = new ArrayList<ConnectionListener>();
	}

	public void start() {
		engine.publish(new Event("connection:connecting"));
		engine.start();
	}

	public void stop() {
		engine.stop();
		engine.publish(new Event("connection:disconnected"));
	}

	final void fireOnConnected() {
		for (final ConnectionListener listener : listeners) {
			listener.onConnected();
		}

	}

	final void fireOnConnecting() {
		for (final ConnectionListener listener : listeners) {
			listener.onConnecting();
		}
	}

	final void fireOnDisconnected() {
		for (final ConnectionListener listener : listeners) {
			listener.onDisconnected();
		}

	}
}
