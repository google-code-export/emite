package com.calclab.emite.client.xmpp.session;

import java.util.ArrayList;

import com.calclab.emite.client.components.Globals;
import com.calclab.emite.client.core.bosh.BoshManager;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.packet.Event;

public class Session {

    public static class Events {
	public static final Event loggedIn = new Event("session:login");
	public static final Event loggedOut = new Event("session:logout");
    }

    public static enum State {
	authorized, connected, connecting, disconnected, error
    }

    private final Dispatcher dispatcher;
    private final Globals globals;
    private final ArrayList<SessionListener> listeners;
    private State state;

    public Session(final Dispatcher dispatcher, final Globals globals) {
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
	dispatcher.publish(BoshManager.Events.start);
    }

    public void logout() {
	dispatcher.publish(Events.loggedOut);
    }

    public void setState(final State newState) {
	final State oldState = this.state;
	this.state = newState;
	for (final SessionListener listener : listeners) {
	    listener.onStateChanged(oldState, newState);
	}
    }
}
