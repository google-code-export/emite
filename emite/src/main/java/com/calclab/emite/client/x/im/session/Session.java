package com.calclab.emite.client.x.im.session;

import java.util.ArrayList;

import com.calclab.emite.client.Globals;
import com.calclab.emite.client.action.Dispatcher;
import com.calclab.emite.client.packet.Event;

public class Session {

	public static class Events {
		public static final Event login = new Event("session:login");
	}

	public static enum State {
		connected, connecting, disconnected
	}

	private final Dispatcher dispatcher;
	private final Globals globals;
	private final ArrayList<SessionListener> listeners;
	private State state;

	public Session(final Globals globals, final Dispatcher dispatcher) {
		this.globals = globals;
		this.dispatcher = dispatcher;
		state = State.disconnected;
		this.listeners = new ArrayList<SessionListener>();
	}

	public void addListener(final SessionListener listener) {
		listeners.add(listener);
	}

	public void login(final SessionOptions sessionOptions) {
		globals.setUserName(sessionOptions.getUserName());
		globals.setPassword(sessionOptions.getPassword());
		dispatcher.publish(Events.login);
	}

	public void logout() {

	}

	public void setState(final State newState) {
		final State oldState = this.state;
		this.state = newState;
		for (final SessionListener listener : listeners) {
			listener.onStateChanged(oldState, newState);
		}
	}
}
