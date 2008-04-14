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
import com.calclab.emite.client.core.bosh.EmiteComponent;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.Event;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public class SessionManager extends EmiteComponent {
    public static class Events {
	public static final Event authorized = new Event("sasl:authorized");
	public static final Event binded = new Event("resource:binded");
	/** ATTRIBUTES: uri */
	public static final Event loggedIn = new Event("session:on:login");
	public static final Event loggedOut = new Event("session:on:logout");
	/** ATTIBUTES: uri, password */
	public static final Event logIn = new Event("session:do:login");
    }

    private Session session;

    public SessionManager(final Emite emite) {
	super(emite);
    }

    @Override
    public void attach() {

	when(Events.authorized, new PacketListener() {
	    public void handle(final IPacket received) {
		emite.publish(BoshManager.Events.restart);
	    }
	});

	when(Events.authorized, new PacketListener() {
	    public void handle(final IPacket received) {
		session.setState(Session.State.authorized);
	    }
	});

	when(SessionManager.Events.loggedOut, new PacketListener() {
	    public void handle(final IPacket received) {
		emite.publish(BoshManager.Events.stop);
		session.setState(Session.State.disconnected);
	    }
	});

	when(BoshManager.Events.onError, new PacketListener() {
	    public void handle(final IPacket received) {
		session.setState(Session.State.error);
		session.setState(Session.State.disconnected);
	    }
	});

	when(Events.binded, new PacketListener() {
	    public void handle(final IPacket received) {
		final XmppURI userURI = XmppURI.parse(received.getAttribute("uri"));
		final IQ iq = new IQ(IQ.Type.set, userURI, userURI.getHost());
		iq.Include("session", "urn:ietf:params:xml:ns:xmpp-session");

		emite.send("session", iq, new PacketListener() {
		    public void handle(final IPacket received) {
			session.setState(Session.State.connected);
			emite.publish(SessionManager.Events.loggedIn.Params("uri", userURI.toString()));
		    }
		});
	    }

	});
    }

    public void doLogin(final XmppURI uri, final String password) {
	emite.publish(SessionManager.Events.logIn.Params("uri", uri.toString()).With("password", password));
	emite.publish(BoshManager.Events.start.Params("domain", uri.getHost()));
    }

    public void doLogout() {
	emite.publish(BoshManager.Events.stop);
	emite.publish(SessionManager.Events.loggedOut);
    }

    public void setSession(final Session session) {
	this.session = session;
    }
}
