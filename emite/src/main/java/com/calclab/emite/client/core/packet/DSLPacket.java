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
package com.calclab.emite.client.core.packet;

public abstract class DSLPacket implements IPacket {

    public int getAttributeAsInt(final String name) {
	final String value = getAttribute(name);
	return Integer.parseInt(value);
    }

    public boolean hasAttribute(final String name) {
	return getAttribute(name) != null;
    }

    public boolean hasAttribute(final String name, final String value) {
	return value.equals(getAttribute(name));
    }

    public IPacket With(final IPacket child) {
	addChild(child);
	return this;
    }

    public IPacket With(final String name, final long value) {
	return With(name, String.valueOf(value));
    }

    public IPacket With(final String name, final String value) {
	setAttribute(name, value);
	return this;
    }

    public IPacket WithText(final String text) {
	addText(text);
	return this;
    }
}
