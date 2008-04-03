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
    public String getJid() {
        return node + "@" + host;
    }
}
