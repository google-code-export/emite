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

import com.calclab.emite.client.components.Globals;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.bosh.EmiteComponent;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.Event;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.xmpp.sasl.SASLManager;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public class ResourceBindingManager extends EmiteComponent {
	public static class Events {
		public static final Event binded = new Event("resource:binded");
	}

	private final Globals globals;

	public ResourceBindingManager(final Emite emite, final Globals globals) {
		super(emite);
		this.globals = globals;

	}

	@Override
	public void attach() {
		when(SASLManager.Events.authorized, new PacketListener() {
			public void handle(final Packet received) {
				final IQ iq = new IQ("bindRequest", IQ.Type.set);
				iq.add("bind", "urn:ietf:params:xml:ns:xmpp-bind").add("resource", null).addText(
						globals.getResourceName());

				emite.send(iq);
			}
		});
		when(new IQ("bindRequest", IQ.Type.result, null), new PacketListener() {
			public void handle(final Packet iq) {
				final String jid = iq.getFirstChild("bind").getFirstChild("jid").getText();
				final XmppURI uri = XmppURI.parse(jid);
				globals.setOwnURI(uri);
				emite.publish(Events.binded);
			}
		});
	}
}
