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

import com.calclab.emite.client.core.bosh.BoshManager;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.signal.Listener;
import com.calclab.emite.client.core.signal.Signal;
import com.calclab.emite.client.xmpp.session.SessionManager.Events;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public class Session {

    public static enum State {
	authorized, loggedIn, connecting, disconnected, error, notAuthorized, ready,
	/** USE ready!!! * */
	@Deprecated
	connected
    }

    private final SessionListenerCollection listeners;
    private State state;
    private final Emite emite;
    private XmppURI userURI;
    private final Signal<State> onStateChanged;
    private final Signal<Presence> onPresence;
    private final Signal<Message> onMessage;

    public Session(final BoshManager manager, final Emite emite) {
	this.emite = emite;
	state = State.disconnected;
	this.listeners = new SessionListenerCollection();
	this.onStateChanged = new Signal<State>();
	this.onPresence = new Signal<Presence>();
	this.onMessage = new Signal<Message>();

	manager.onStanza(new Listener<IPacket>() {
	    public void onEvent(final IPacket stanza) {
		final String name = stanza.getName();
		if (name.equals("message")) {
		    onMessage.fire(new Message(stanza));
		} else if (name.equals("presence")) {
		    onPresence.fire(new Presence(stanza));
		}
	    }
	});
    }

    /**
     * @deprecated
     * @see onStateChanged
     * @param listener
     */
    @Deprecated
    public void addListener(final SessionListener listener) {
	listeners.add(listener);
    }

    public XmppURI getCurrentUser() {
	return userURI;
    }

    public State getState() {
	return state;
    }

    public void login(final XmppURI uri, final String password) {
	if (state == State.disconnected) {
	    setState(State.connecting);
	    emite.publish(Events.login(uri, password));
	    emite.publish(BoshManager.Events.start(uri.getHost()));
	    userURI = uri;
	}
    }

    public void logout() {
	if (state != State.disconnected) {
	    emite.publish(BoshManager.Events.stop);
	    emite.publish(SessionManager.Events.onLoggedOut);
	    userURI = null;
	}
    }

    public void onMessage(final Listener<Message> listener) {
	onMessage.add(listener);
    }

    public void onPresence(final Listener<Presence> listener) {
	onPresence.add(listener);
    }

    public void onStateChanged(final Listener<State> listener) {
	onStateChanged.add(listener);
    }

    void setState(final State newState) {
	final State oldState = this.state;
	this.state = newState;
	listeners.onStateChanged(oldState, newState);
	onStateChanged.fire(state);
    }

}
