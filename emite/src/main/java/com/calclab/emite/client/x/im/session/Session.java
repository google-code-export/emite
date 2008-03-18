package com.calclab.emite.client.x.im.session;

import java.util.ArrayList;

import com.calclab.emite.client.IDispatcher;
import com.calclab.emite.client.IGlobals;
import com.calclab.emite.client.packet.Event;

public class Session {

	public static class Events {
		public static final Event login = new Event("session:login");
	}

	public static enum State {
		connected, connecting, disconnected
	}

	private final IDispatcher dispatcher;
	private final IGlobals globals;
	private final ArrayList<SessionListener> listeners;
	private State state;

	public Session(final IGlobals globals, final IDispatcher dispatcher) {
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

	public void setState(final State state) {
		final State oldState = state;
		this.state = state;
		for (final SessionListener listener : listeners) {
			listener.onStateChanged(oldState, state);
		}
	}
}
