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
package com.calclab.emite.client.xmpp.stanzas;

import com.calclab.emite.client.core.packet.IPacket;

public class IQ extends BasicStanza {
    public static enum Type {
	error, get, result, set
    }

    private static final String NAME = "iq";

    public static boolean isSuccess(final IPacket received) {
	return received.hasAttribute(TYPE, "result");
    }

    public IQ(final IPacket stanza) {
	super(stanza);
    }

    public IQ(final Type type) {
	super(NAME, "jabber:client");
	if (type != null) {
	    setType(type.toString());
	}
    }

    public IQ(final Type type, final XmppURI from, final String to) {
	this(type);
	setFrom(from.toString());
	setTo(to);
    }

    public IPacket Includes(final String name, final String xmlns) {
	add(name, xmlns);
	return this;
    }

    public IPacket setQuery(final String namespace) {
	return add("query", namespace);
    }

    public IQ To(final XmppURI toURI) {
	setTo(toURI.toString());
	return this;
    }

    public IQ WithQuery(final String queryNamespace, final IPacket child) {
	final IPacket query = add("query", queryNamespace);
	if (child != null) {
	    query.addChild(child);
	}
	return this;
    }
}
