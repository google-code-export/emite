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

import java.util.Date;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.core.bosh.Connection;
import com.calclab.emite.client.core.bosh.StreamSettings;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.xmpp.resource.ResourceBindingManager;
import com.calclab.emite.client.xmpp.sasl.AuthorizationTransaction;
import com.calclab.emite.client.xmpp.sasl.SASLManager;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.signal.Slot;

public class XmppSession extends AbstractSession {
    private State state;
    private XmppURI userURI;
    private final SessionScope scope;
    private final Connection connection;
    private AuthorizationTransaction transaction;
    private final IQManager iqManager;

    public XmppSession(final Connection connection, final SessionScope scope, final SASLManager saslManager,
	    final ResourceBindingManager bindingManager) {
	this.connection = connection;
	this.scope = scope;
	state = State.disconnected;
	this.iqManager = new IQManager();

	connection.onStanzaReceived(new Slot<IPacket>() {
	    public void onEvent(final IPacket stanza) {
		final String name = stanza.getName();
		if (name.equals("message")) {
		    onMessage.fire(new Message(stanza));
		} else if (name.equals("presence")) {
		    onPresence.fire(new Presence(stanza));
		} else if (name.equals("iq")) {
		    iqManager.handle(stanza);
		} else if ("stream:features".equals(name) && stanza.hasChild("mechanisms")) {
		    if (transaction != null) {
			saslManager.sendAuthorizationRequest(transaction);
			transaction = null;
		    }
		}

	    }
	});

	connection.onError(new Slot<String>() {
	    public void onEvent(final String msg) {
		Log.debug("ERROR: " + msg);
		setState(State.error);
		disconnect();
	    }
	});

	saslManager.onAuthorized(new Slot<AuthorizationTransaction>() {
	    public void onEvent(final AuthorizationTransaction ticket) {
		if (ticket.getState() == AuthorizationTransaction.State.succeed) {
		    setState(Session.State.authorized);
		    connection.restartStream();
		    bindingManager.bindResource(ticket.uri.getResource());
		} else {
		    setState(Session.State.notAuthorized);
		    disconnect();
		}
	    }

	});

	bindingManager.onBinded(new Slot<XmppURI>() {
	    public void onEvent(final XmppURI uri) {
		userURI = uri;
		final IQ iq = new IQ(IQ.Type.set, userURI, userURI.getHostURI());
		iq.Includes("session", "urn:ietf:params:xml:ns:xmpp-session");

		sendIQ("session", iq, new Slot<IPacket>() {
		    public void onEvent(final IPacket received) {
			if (IQ.isSuccess(received)) {
			    setLoggedIn(uri);
			}
		    }
		});
	    }

	});

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.calclab.emite.client.xmpp.session.Session#getCurrentUser()
     */
    public XmppURI getCurrentUser() {
	return userURI;
    }

    public Session.State getState() {
	return state;
    }

    public boolean isLoggedIn() {
	return userURI != null;
    }

    public void login(XmppURI uri, final String password) {
	if (state == Session.State.disconnected) {
	    setState(Session.State.connecting);
	    connection.connect();
	    scope.createAll();
	    if (!uri.hasResource()) {
		uri = new XmppURI(uri.getNode(), uri.getHost(), "" + new Date().getTime());
	    }
	    transaction = new AuthorizationTransaction(uri, password);
	    Log.debug("Sending auth transaction: " + transaction);
	}
    }

    public void logout() {
	if (state != State.disconnected && userURI != null) {
	    onLoggedOut.fire(userURI);
	    userURI = null;
	    connection.disconnect();
	    setState(State.disconnected);
	}
    }

    public StreamSettings pause() {
	return state == State.ready ? connection.pause() : null;
    }

    public void resume(final XmppURI userURI, final StreamSettings settings) {
	this.userURI = userURI;
	connection.resume(settings);
	setState(State.ready);
    }

    public void send(final IPacket packet) {
	connection.send(packet);
    }

    public void sendIQ(final String category, final IQ iq, final Slot<IPacket> slot) {
	final String id = iqManager.register(category, slot);
	iq.setAttribute("id", id);
	send(iq);
    }

    public void setLoggedIn(final XmppURI userURI) {
	this.userURI = userURI;
	setState(Session.State.loggedIn);
	onLoggedIn.fire(userURI);
	setState(Session.State.ready);
    }

    void setState(final Session.State newState) {
	this.state = newState;
	onStateChanged.fire(state);
    }

    private void disconnect() {
	connection.disconnect();
	setState(Session.State.disconnected);
    }

}
