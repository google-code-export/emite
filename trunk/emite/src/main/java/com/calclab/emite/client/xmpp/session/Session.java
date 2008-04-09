/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.client.xmpp.session;

import java.util.ArrayList;

import com.calclab.emite.client.components.Globals;
import com.calclab.emite.client.core.bosh.BoshManager;
import com.calclab.emite.client.core.dispatcher.Dispatcher;

public class Session {

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

    public boolean isLoggedIn() {
	return state == State.connected;
    }

    public boolean isLoggedOut() {
	return state == State.disconnected;
    }

    public void login(final SessionOptions sessionOptions) {
	globals.setUserName(sessionOptions.getUserName());
	globals.setPassword(sessionOptions.getPassword());
	setState(State.connecting);
	dispatcher.publish(BoshManager.Events.start);
    }

    public void logout() {
	dispatcher.publish(BoshManager.Events.stop);
	dispatcher.publish(SessionManager.Events.loggedOut);
    }

    public void setState(final State newState) {
	final State oldState = this.state;
	this.state = newState;
	for (final SessionListener listener : listeners) {
	    listener.onStateChanged(oldState, newState);
	}
    }
}
