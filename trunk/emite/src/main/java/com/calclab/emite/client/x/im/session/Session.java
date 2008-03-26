package com.calclab.emite.client.x.im.session;

import java.util.ArrayList;

import com.calclab.emite.client.Globals;
import com.calclab.emite.client.bosh.Connection;
import com.calclab.emite.client.dispatcher.Dispatcher;
import com.calclab.emite.client.packet.Event;

public class Session {

	public static class Events {
		public static final Event login = new Event("session:login");
	}

	public static enum State {
		authorized, connected, connecting, disconnected
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

	/**
	 * EXPERIMENTAL: every listener receives the current state
	 * 
	 * @param listener
	 */
	public void addListener(final SessionListener listener) {
		listeners.add(listener);
		listener.onStateChanged(state, state);
	}

	public State getState() {
		return state;
	}

	public void login(final SessionOptions sessionOptions) {
		globals.setUserName(sessionOptions.getUserName());
		globals.setPassword(sessionOptions.getPassword());
		setState(State.connecting);
		dispatcher.publish(Connection.Events.start);
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
