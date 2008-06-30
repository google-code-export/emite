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

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.core.bosh.BoshManager;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.xmpp.sasl.AuthorizationTransaction;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.signal.Signal;
import com.calclab.suco.client.signal.Slot;

public class Session {

    public static enum State {
	authorized, loggedIn, connecting, disconnected, error, notAuthorized, ready,
    }

    private State state;
    private final Emite emite;
    private XmppURI userURI;
    private final Signal<State> onStateChanged;
    private final Signal<Presence> onPresence;
    private final Signal<Message> onMessage;
    private final Signal<AuthorizationTransaction> onLogin;
    private final SessionScope scope;

    public Session(final BoshManager manager, final Emite emite, final SessionScope scope) {
	this.emite = emite;
	this.scope = scope;
	state = State.disconnected;

	this.onStateChanged = new Signal<State>("onStateChanged");
	this.onPresence = new Signal<Presence>("onPresence");
	this.onMessage = new Signal<Message>("onMessage");
	this.onLogin = new Signal<AuthorizationTransaction>("onLogin");

	manager.onStanza(new Slot<IPacket>() {
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

    public XmppURI getCurrentUser() {
	return userURI;
    }

    public State getState() {
	return state;
    }

    public void login(final XmppURI uri, final String password) {
	if (state == State.disconnected) {
	    setState(State.connecting);
	    scope.createAll();
	    final AuthorizationTransaction transaction = new AuthorizationTransaction(uri, password);
	    Log.debug("Sending auth transaction: " + transaction);
	    onLogin.fire(transaction);
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

    public void onLogin(final Slot<AuthorizationTransaction> listener) {
	onLogin.add(listener);
    }

    public void onMessage(final Slot<Message> listener) {
	onMessage.add(listener);
    }

    public void onPresence(final Slot<Presence> listener) {
	onPresence.add(listener);
    }

    public void onStateChanged(final Slot<State> listener) {
	onStateChanged.add(listener);
    }

    void setState(final State newState) {
	this.state = newState;
	onStateChanged.fire(state);
    }

}
