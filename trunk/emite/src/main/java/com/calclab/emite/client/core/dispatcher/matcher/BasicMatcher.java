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

import com.calclab.emite.client.core.packet.Packet;

public class BasicMatcher implements Matcher {
	private final String attName;
	private final String attValue;
	private final String name;

	public BasicMatcher(final String name) {
		this(name, null, null);
	}

	public BasicMatcher(final String name, final String attName,
			final String attValue) {
		this.name = name;
		this.attName = attName;
		this.attValue = attValue;
	}

	public String getElementName() {
		return name;
	}

	public boolean matches(final Packet stanza) {
		final boolean isCorrectName = stanza.getName().equals(name);
		if (isCorrectName) {
			if (attName != null) {
				return attValue.equals(stanza.getAttribute(attName));
			} else {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		final String name = attName == null ? "null" : attName;
		final String value = attValue == null ? "null" : attValue;
		return "[Matcher: " + name + " (" + name + "," + value + ")]";
	}
}
