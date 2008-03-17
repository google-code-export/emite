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

	public void addListener(final ConnectionListener listener) {
		listeners.add(listener);
	}

	public void login(final String userName, final String userPassword) {
		engine.setGlobal(Engine.USER, userName);
		engine.setGlobal(Engine.PASSWORD, userPassword);
		engine.publish(new Event("connection:connecting"));
		engine.start();
	}

	public void logout() {
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
