package com.calclab.examplechat.client.chatuiplugin.utils;

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
