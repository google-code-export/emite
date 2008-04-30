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

import com.calclab.emite.client.components.Installable;
import com.calclab.emite.client.core.bosh.BoshManager;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.Event;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.xmpp.session.Session.State;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.*;

public class SessionManager implements Installable {
    public static class Events {
	public static final Event onAuthorized = new Event("session:on:authorized");
	public static final Event onAuthorizationFailed = new Event("session:on:authorization-failed");
	public static final Event onBinded = new Event("session:on:binded");
	public static final Event onLoggedOut = new Event("session:on:logout");
	public static final Event onDoLogin = new Event("session:do:login");
	public static IPacket onDoAuthorization = new Event("session:do:authorization");
	public static final Event onLoggedIn = new Event("session:on:login");

	public static Event binded(final String jid) {
	    return SessionManager.Events.onBinded.Params("uri", jid);
	}

	public static Event loggedIn(final String uri) {
	    return SessionManager.Events.onLoggedIn.Params("uri", uri);
	}

	public static Event login(final XmppURI uri, final String password) {
	    final String strURI = uri != null ? uri.toString() : null;
	    return (Event) SessionManager.Events.onDoLogin.Params("uri", strURI).With("password", password);
	}
    }

    private Session session;
    private final Emite emite;
    private XmppURI userURI;

    public SessionManager(final Emite emite) {
	this.emite = emite;
    }

    public void doLogin(final XmppURI uri, final String password) {
	emite.publish(Events.login(uri, password));
	emite.publish(BoshManager.Events.start(uri.getHost()));
	userURI = uri;
    }

    public void doLogout() {
	emite.publish(BoshManager.Events.stop);
	emite.publish(SessionManager.Events.onLoggedOut);
	userURI = null;
    }

    public void install() {

	emite.subscribe(when(Events.onAuthorizationFailed), new PacketListener() {
	    public void handle(final IPacket received) {
		session.setState(State.notAuthorized);
		emite.publish(BoshManager.Events.error("not-authorized", ""));
	    }
	});

	emite.subscribe(when(new Packet("stream:features")), new PacketListener() {
	    public void handle(final IPacket received) {
		if (received.hasChild("mechanisms")) {
		    emite.publish(Events.onDoAuthorization);
		}
	    }
	});

	emite.subscribe(when(Events.onAuthorized), new PacketListener() {
	    public void handle(final IPacket received) {
		session.setState(Session.State.authorized);
		emite.publish(BoshManager.Events.onRestart);
	    }
	});

	emite.subscribe(when(SessionManager.Events.onLoggedOut), new PacketListener() {
	    public void handle(final IPacket received) {
		emite.publish(BoshManager.Events.stop);
		session.setState(Session.State.disconnected);
	    }
	});

	emite.subscribe(when(BoshManager.Events.onError), new PacketListener() {
	    public void handle(final IPacket received) {
		session.setState(Session.State.error);
		session.setState(Session.State.disconnected);
	    }

	});

	emite.subscribe(when(Events.onBinded), new PacketListener() {
	    public void handle(final IPacket received) {
		final String uri = received.getAttribute("uri");
		sendSessionRequest(uri);
	    }

	});
    }

    public void setSession(final Session session) {
	this.session = session;
    }

    private void sendSessionRequest(final String uri) {
	userURI = uri(uri);
	final IQ iq = new IQ(IQ.Type.set, userURI, userURI.getHostURI());
	iq.Includes("session", "urn:ietf:params:xml:ns:xmpp-session");

	emite.sendIQ("session", iq, new PacketListener() {
	    public void handle(final IPacket received) {
		if (IQ.isSuccess(received)) {
		    session.setState(Session.State.connected);
		    emite.publish(Events.loggedIn(uri));
		}
	    }
	});

    }

}
