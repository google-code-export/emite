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
package com.calclab.emite.client.xmpp.resource;

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.bosh.EmiteComponent;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.Event;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.xmpp.sasl.SASLManager;
import com.calclab.emite.client.xmpp.session.SessionManager;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public class ResourceBindingManager extends EmiteComponent {
    public static class Events {
	public static final Event binded = new Event("resource:binded");
    }

    private String resource;

    public ResourceBindingManager(final Emite emite) {
	super(emite);

    }

    @Override
    public void attach() {
	when(SessionManager.Events.logIn, new PacketListener() {

	    public void handle(final IPacket received) {
		resource = XmppURI.parse(received.getAttribute("uri")).getResource();
	    }
	});
	when(SASLManager.Events.authorized, new PacketListener() {
	    public void handle(final IPacket received) {
		final IQ iq = new IQ(IQ.Type.set);
		iq.add("bind", "urn:ietf:params:xml:ns:xmpp-bind").add("resource", null).addText(resource);

		emite.send("bind", iq, new PacketListener() {
		    public void handle(final IPacket received) {
			final String jid = received.getFirstChild("bind").getFirstChild("jid").getText();
			emite.publish(Events.binded.Params("uri", jid));
		    }
		});
	    }
	});
    }
}
