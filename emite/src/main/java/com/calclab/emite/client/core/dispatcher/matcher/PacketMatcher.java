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
package com.calclab.emite.client.core.dispatcher.matcher;

import java.util.HashMap;

import com.calclab.emite.client.core.packet.Packet;

public class PacketMatcher implements Matcher {
	private final HashMap<String, String> attributes;
	private final String name;

	public PacketMatcher(final Packet packet) {
		this(packet.getName(), packet.getAttributes());
	}

	public PacketMatcher(final String packetName) {
		this(packetName, null);
	}

	public PacketMatcher(final String name, final HashMap<String, String> attributes) {
		this.name = name;
		this.attributes = attributes;
	}

	public String getElementName() {
		return name;
	}

	public boolean matches(final Packet stanza) {
		if (attributes == null) {
			return true;
		}
		for (final String name : attributes.keySet()) {
			final String value = stanza.getAttribute(name);
			final String expected = attributes.get(name);
			// Log.debug("MATCHER " + name + " - expecting: " + expected + " |
			// value: " + value);
			if (!expected.equals(value)) {
				return false;
			}
		}
		return true;
	}

}
