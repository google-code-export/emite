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

import com.calclab.emite.client.components.Globals;
import com.calclab.emite.client.core.bosh.BoshManager;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.bosh.EmiteComponent;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.Event;
import com.calclab.emite.client.core.packet.APacket;
import com.calclab.emite.client.xmpp.resource.ResourceBindingManager;
import com.calclab.emite.client.xmpp.sasl.SASLManager;
import com.calclab.emite.client.xmpp.stanzas.IQ;

public class SessionManager extends EmiteComponent {
    public static class Events {
	public static final Event loggedIn = new Event("session:on:login");
	public static final Event loggedOut = new Event("session:on:logout");
    }

    private final Globals globals;
    private final Session session;

    public SessionManager(final Emite emite, final Globals globals, final Session session) {
	super(emite);
	this.globals = globals;
	this.session = session;

    }

    @Override
    public void attach() {

	when(SASLManager.Events.authorized, new PacketListener() {
	    public void handle(final APacket received) {
		emite.publish(BoshManager.Events.restart);
	    }
	});

	when(SASLManager.Events.authorized, new PacketListener() {
	    public void handle(final APacket received) {
		session.setState(Session.State.authorized);
	    }
	});

	when(SessionManager.Events.loggedOut, new PacketListener() {
	    public void handle(final APacket received) {
		emite.publish(BoshManager.Events.stop);
		session.setState(Session.State.disconnected);
	    }
	});

	when(BoshManager.Events.onError, new PacketListener() {
	    public void handle(final APacket received) {
		session.setState(Session.State.error);
		session.setState(Session.State.disconnected);
	    }
	});

	when(ResourceBindingManager.Events.binded, new PacketListener() {
	    public void handle(final APacket received) {
		final IQ iq = new IQ("requestSession", IQ.Type.set).From(globals.getOwnURI()).To(globals.getDomain());
		iq.Include("session", "urn:ietf:params:xml:ns:xmpp-session");
		emite.send(iq);
	    }

	});
	when(new IQ("requestSession", IQ.Type.result, null), new PacketListener() {
	    public void handle(final APacket received) {
		session.setState(Session.State.connected);
		emite.publish(SessionManager.Events.loggedIn);
	    }
	});

    }
}
