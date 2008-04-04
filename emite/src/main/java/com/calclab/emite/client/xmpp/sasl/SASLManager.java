/**
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
package com.calclab.emite.client.xmpp.sasl;

import com.calclab.emite.client.components.Globals;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.bosh.EmiteComponent;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.BasicPacket;
import com.calclab.emite.client.core.packet.Event;
import com.calclab.emite.client.core.packet.Packet;

public class SASLManager extends EmiteComponent {
	public static class Events {
		public static final Event authorized = new Event("sasl:authorized");
	}

	private static final String SEP = new String(new char[] { 0 });
	private final Globals globals;

	public SASLManager(final Emite emite, final Globals globals) {
		super(emite);
		this.globals = globals;

	}

	@Override
	public void attach() {
		when(new BasicPacket("stream:features"), new PacketListener() {
			public void handle(final Packet received) {
				emite.send(createPlainAuthorization());
			}
		});

		when(new BasicPacket("success", "urn:ietf:params:xml:ns:xmpp-sasl"), new PacketListener() {
			public void handle(final Packet received) {
				emite.publish(Events.authorized);
			}
		});

	}

	protected String encode(final String domain, final String userName, final String password) {
		final String auth = userName + "@" + domain + SEP + userName + SEP + password;
		return Base64Coder.encodeString(auth);
	}

	private Packet createPlainAuthorization() {
		final Packet auth = new BasicPacket("auth", "urn:ietf:params:xml:ns:xmpp-sasl").With("mechanism", "PLAIN");
		final String encoded = encode(globals.getDomain(), globals.getUserName(), globals.getPassword());
		auth.addText(encoded);
		return auth;
	}
}
