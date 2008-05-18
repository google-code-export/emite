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

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractPacket implements IPacket {

    public int getAttributeAsInt(final String name) {
	final String value = getAttribute(name);
	return Integer.parseInt(value);
    }

    public List<? extends IPacket> getChildren(final PacketFilter filter) {
	final List<IPacket> list = new ArrayList<IPacket>();
	for (final IPacket child : getChildren()) {
	    if (filter.isValid(child)) {
		list.add(child);
	    }
	}
	return list;
    }

    public List<? extends IPacket> getChildren(final String name) {
	return getChildren(new PacketFilter() {
	    public boolean isValid(final IPacket packet) {
		return packet.getName().equals(name);
	    }
	});
    }

    public IPacket getFirstChild(final PacketFilter filter) {
	for (final IPacket child : getChildren()) {
	    if (filter.isValid(child)) {
		return child;
	    }
	}
	return NoPacket.INSTANCE;
    }

    public IPacket getFirstChild(final String childName) {
	return getFirstChild(new PacketFilter() {
	    public boolean isValid(final IPacket packet) {
		return childName.equals(packet.getName());
	    }
	});
    }

    public boolean hasAttribute(final String name) {
	return getAttribute(name) != null;
    }

    public boolean hasAttribute(final String name, final String value) {
	return value.equals(getAttribute(name));
    }

    public boolean hasChild(final String name) {
	return getFirstChild(name) != NoPacket.INSTANCE;
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
	setText(text);
	return this;
    }

}
