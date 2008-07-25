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
import com.calclab.emite.client.core.bosh3.Bosh3Connection;
import com.calclab.emite.client.core.bosh3.Bosh3Settings;
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
    private final Bosh3Connection connection;
    private AuthorizationTransaction transaction;
    private final IQManager iqManager;

    public XmppSession(final Bosh3Connection connection, final SessionScope scope, final SASLManager saslManager,
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
		    if (!iqManager.handle(stanza)) {
			onIQ.fire(new IQ(stanza));
		    }
		} else if ("stream:features".equals(name) && stanza.hasChild("mechanisms")) {
		    if (transaction != null) {
			saslManager.sendAuthorizationRequest(transaction);
			transaction = null;
		    }
		}

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
		    connection.disconnect();
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

    /*
     * (non-Javadoc)
     * 
     * @see com.calclab.emite.client.xmpp.session.Session#getState()
     */
    public Session.State getState() {
	return state;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.calclab.emite.client.xmpp.session.Session#isLoggedIn()
     */
    public boolean isLoggedIn() {
	return userURI != null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.calclab.emite.client.xmpp.session.Session#login(com.calclab.emite
     * .client.xmpp.stanzas.XmppURI, java.lang.String,
     * com.calclab.emite.client.core.bosh3.Bosh3Settings)
     */
    public void login(final XmppURI uri, final String password, final Bosh3Settings settings) {
	if (state == Session.State.disconnected) {
	    setState(Session.State.connecting);
	    connection.connect(settings);
	    scope.createAll();
	    transaction = new AuthorizationTransaction(uri, password);
	    Log.debug("Sending auth transaction: " + transaction);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.calclab.emite.client.xmpp.session.Session#logout()
     */
    public void logout() {
	if (state != Session.State.disconnected) {
	    connection.disconnect();
	    final XmppURI lastUser = userURI;
	    userURI = null;
	    onLoggedOut.fire(lastUser);
	}
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

}
