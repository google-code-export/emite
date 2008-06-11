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

import static com.calclab.emite.client.core.dispatcher.matcher.Matchers.when;

import com.calclab.emite.client.core.bosh.BoshManager;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.Event;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.xmpp.resource.ResourceBindingManager;
import com.calclab.emite.client.xmpp.sasl.AuthorizationTicket;
import com.calclab.emite.client.xmpp.sasl.SASLManager;
import com.calclab.emite.client.xmpp.session.Session.State;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.signal.Slot;

public class SessionManager {
    public static class Events {
	public static final Event onLoggedOut = new Event("session:on:logout");
	public static final Event onLoggedIn = new Event("session:on:login");
	public static final Event ready = new Event("session:ready");

	public static Event loggedIn(final String uri) {
	    return SessionManager.Events.onLoggedIn.Params("uri", uri);
	}

    }

    public static class Signals {
	public static void onLoggedOut(final Dispatcher dispatcher, final PacketListener listener) {
	    dispatcher.subscribe(when(Events.onLoggedOut), listener);
	}
    }

    private final Session session;
    private final Emite emite;
    private XmppURI userURI;
    private final SASLManager saslManager;
    private AuthorizationTicket authorizationTicket;
    private final ResourceBindingManager bindingManager;

    public SessionManager(final Session session, final Emite emite, final SASLManager saslManager,
	    final ResourceBindingManager bindingManager) {
	this.session = session;
	this.emite = emite;
	this.saslManager = saslManager;
	this.bindingManager = bindingManager;
	this.authorizationTicket = null;
	install();
    }

    private void install() {
	session.onLogin(new Slot<AuthorizationTicket>() {
	    public void onEvent(final AuthorizationTicket parameter) {
		authorizationTicket = parameter;
	    }
	});

	saslManager.onAuthorized(new Slot<AuthorizationTicket>() {
	    public void onEvent(final AuthorizationTicket ticket) {
		if (ticket.getState() == AuthorizationTicket.State.succeed) {
		    session.setState(Session.State.authorized);
		    emite.publish(BoshManager.Events.onRestartStream);
		    bindingManager.bindResource(ticket.uri.getResource());
		} else {
		    session.setState(State.notAuthorized);
		    emite.publish(Dispatcher.Events.error("not-authorized", ""));
		}
	    }

	});

	emite.subscribe(when(new Packet("stream:features")), new PacketListener() {
	    public void handle(final IPacket received) {
		if (received.hasChild("mechanisms")) {
		    saslManager.sendAuthorizationRequest(authorizationTicket);
		    authorizationTicket = null;
		}
	    }
	});

	emite.subscribe(when(SessionManager.Events.onLoggedOut), new PacketListener() {
	    public void handle(final IPacket received) {
		emite.publish(BoshManager.Events.stop);
		session.setState(Session.State.disconnected);
	    }
	});

	emite.subscribe(when(SessionManager.Events.ready), new PacketListener() {
	    public void handle(final IPacket received) {
		session.setState(State.ready);
	    }
	});

	emite.subscribe(when(Dispatcher.Events.onError), new PacketListener() {
	    public void handle(final IPacket received) {
		session.setState(Session.State.error);
		session.setState(Session.State.disconnected);
	    }

	});

	bindingManager.onBinded(new Slot<XmppURI>() {
	    public void onEvent(final XmppURI uri) {
		sendSessionRequest(uri);
	    }

	});

    }

    private void sendSessionRequest(final XmppURI uri) {
	userURI = uri;
	final IQ iq = new IQ(IQ.Type.set, userURI, userURI.getHostURI());
	iq.Includes("session", "urn:ietf:params:xml:ns:xmpp-session");

	emite.sendIQ("session", iq, new PacketListener() {
	    public void handle(final IPacket received) {
		if (IQ.isSuccess(received)) {
		    session.setState(Session.State.loggedIn);
		    emite.publish(Events.loggedIn(uri.toString()));
		    emite.publish(Events.ready);
		}
	    }
	});

    }

}
