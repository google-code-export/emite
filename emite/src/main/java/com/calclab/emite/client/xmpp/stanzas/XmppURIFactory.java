package com.calclab.emite.client.xmpp.stanzas;

import java.util.HashMap;

public class XmppURIFactory {
    private static final String PREFIX = "xmpp:";
    private static final int PREFIX_LENGTH = PREFIX.length();
    private final HashMap<String, XmppURI> cache = new HashMap<String, XmppURI>();

    public XmppURI parse(final String xmppUri) {
	if (xmppUri == null) {
	    return null;
	}

	final String uri = xmppUri.startsWith(PREFIX) ? xmppUri.substring(PREFIX_LENGTH).toLowerCase() : xmppUri
		.toLowerCase();
	final XmppURI cached = cache.get(uri);
	if (cached != null) {
	    return cached;
	}

	String node = null;
	String domain = null;
	String resource = null;

	final int atIndex = uri.indexOf('@') + 1;
	if (atIndex > 0) {
	    node = uri.substring(0, atIndex - 1);
	    if (node.length() == 0) {
		throw new RuntimeException("a uri with @ should have node");
	    }
	}

	final int barIndex = uri.indexOf('/', atIndex);
	if (atIndex == barIndex) {
	    throw new RuntimeException("bad syntax!");
	}
	if (barIndex > 0) {
	    domain = uri.substring(atIndex, barIndex);
	    resource = uri.substring(barIndex + 1);
	} else {
	    domain = uri.substring(atIndex);
	}
	if (domain.length() == 0) {
	    throw new RuntimeException("The domain is required");
	}

	return cache(new XmppURI(node, domain, resource));
    }

    private XmppURI cache(final XmppURI xmppURI) {
	cache.put(xmppURI.toString(), xmppURI);
	return xmppURI;
    }

}
