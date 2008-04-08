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

/**
 * 
 * http://www.xmpp.org/drafts/attic/draft-saintandre-xmpp-uri-00.html
 * 
 * <code>XMPP- = ["xmpp:"] node "@" host[ "/" resource]</code>
 * 
 */
public class XmppURI {
    private static final String PREFIX = "xmpp:";
    private static final int PREFIX_LENGTH = PREFIX.length();

    public static XmppURI parse(final String xmppUri) {
        final String xmppNoPrefix = xmppUri.startsWith(PREFIX) ? xmppUri.substring(PREFIX_LENGTH) : xmppUri;

        final int atIndex = xmppNoPrefix.indexOf('@') + 1;
        if (atIndex <= 0) {
            throw new RuntimeException("The @ is mandatory");
        }
        final String node = xmppNoPrefix.substring(0, atIndex - 1);
        if (node.length() == 0) {
            throw new RuntimeException("The node is mandatory");
        }

        final int barIndex = xmppNoPrefix.indexOf('/', atIndex);
        final String host = barIndex > 0 ? xmppNoPrefix.substring(atIndex, barIndex) : xmppNoPrefix.substring(atIndex);
        if (host.length() == 0) {
            throw new RuntimeException("The domain is required");
        }

        final String resource = barIndex > 0 ? xmppNoPrefix.substring(barIndex + 1) : null;

        return new XmppURI(node, host, resource);
    }

    private final String host;
    private final String node;
    private final String representation;
    private final String resource;

    /**
     * 
     * @param jid
     * @param resource
     *                <code> resource      = *( unreserved / escaped )
                reserved      = ";" / "/" / "?" / ":" / "@" / "&" / "=" / "+" /
                "$" / "," / "[" / "]" </code>
     * 
     */
    public XmppURI(final String node, final String host, final String resource) {
        this.node = node;
        this.host = host;
        this.resource = resource;
        this.representation = node + "@" + host + (resource != null ? "/" + resource : "");
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        return representation.equals(((XmppURI) obj).representation);
    }

    public boolean equalsNoResource(final XmppURI other) {
        return host.equals(other.host) && node.equals(other.node);
    }

    public String getHost() {
        return host;
    }

    public String getNode() {
        return node;
    }

    public String getResource() {
        return resource;
    }

    @Override
    public int hashCode() {
        return representation.hashCode();
    }

    public boolean hasResource() {
        return resource != null;
    }

    @Override
    public String toString() {
        return representation;
    }

    // I need a way to get only the jid to use in HashMaps
    public String getJID() {
        return node + "@" + host;
    }
}
