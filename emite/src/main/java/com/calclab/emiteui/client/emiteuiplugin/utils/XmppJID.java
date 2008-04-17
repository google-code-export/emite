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
package com.calclab.emiteui.client.emiteuiplugin.utils;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;

/**
 * 
 * http://www.xmpp.org/drafts/attic/draft-saintandre-xmpp-uri-00.html
 * 
 * <code>jid  = node "@" host</code>
 * 
 */
public class XmppJID {

    private final String host;
    private final String node;
    private final String representation;

    public XmppJID(final String node, final String host) {
        this.node = node;
        this.host = host;
        this.representation = node + "@" + host;
    }

    public XmppJID(final String jid) {
        this(XmppURI.parse(jid));
    }

    public XmppJID(final XmppURI xmppURI) {
        this(xmppURI.getNode(), xmppURI.getHost());
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        return representation.equals(((XmppJID) obj).representation);
    }

    public String getHost() {
        return host;
    }

    public String getNode() {
        return node;
    }

    @Override
    public int hashCode() {
        return representation.hashCode();
    }

    @Override
    public String toString() {
        return representation;
    }
}
