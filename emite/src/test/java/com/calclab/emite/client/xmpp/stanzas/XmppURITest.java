package com.calclab.emite.client.xmpp.stanzas;

import static org.junit.Assert.*;
import org.junit.Test;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.*;

public class XmppURITest {

    @Test
    public void checkEqualsContract() {
	final XmppURI uri1 = uri("xmpp:test@example/res");
	final XmppURI uri2 = uri("xmpp:test@example/res");
	assertEquals(uri1, uri2);
	assertEquals(uri1.hashCode(), uri2.hashCode());
    }

    @Test
    public void checkUriFormat() {
	final XmppURI uri = uri("xmpp:test@example/res");
	assertEquals("test", uri.getNode());
	assertEquals("example", uri.getHost());
	assertEquals("res", uri.getResource());
    }

    @Test(expected = RuntimeException.class)
    public void checkUriFormatWithOnlyAtFails() {
	uri("xmpp:@");
    }

    @Test(expected = RuntimeException.class)
    public void checkUriFormatWithOnlyHostFails() {
	uri("xmpp:@example");
    }

    @Test(expected = RuntimeException.class)
    public void checkUriFormatWithOnlyNodeFails() {
	uri("xmpp:test@");
    }

    @Test(expected = RuntimeException.class)
    public void checkUriFormatWithOnlySlashFails() {
	uri("xmpp:/");
    }

    @Test(expected = RuntimeException.class)
    public void checkUriFormatWithoutHost() {
	uri("xmpp:test@/res");
    }

    @Test(expected = RuntimeException.class)
    public void checkUriFormatWithoutNodeFails() {
	uri("xmpp:@example/res");
    }

    @Test
    public void shouldCreateHostURI() {
	final XmppURI uri = uri("node@domain/resource");
	final XmppURI host = uri.getHostURI();
	assertNotNull(host);
	assertFalse(host.hasNode());
	assertFalse(host.hasResource());
	assertEquals("node@domain/resource", uri.toString());
    }

    @Test
    public void shouldParseNull() {
	assertNull(uri(null));
    }

    @Test
    public void shouldParseURIWithoutNode() {
	final XmppURI uri = uri("domain/res");
	assertFalse(uri.hasNode());
	assertNull(uri.getNode());
	assertEquals("domain", uri.getHost());
	assertEquals("res", uri.getResource());
	assertEquals("domain/res", uri.toString());
    }

    @Test
    public void shouldParseWithNoPrefix() {
	final XmppURI uri = uri("test@example/res");
	assertEquals("test", uri.getNode());
	assertEquals("example", uri.getHost());
	assertEquals("res", uri.getResource());
    }

    public void shouldParseWithNoResource() {
	final XmppURI uri = uri("xmpp:test@example");
	assertFalse(uri.hasResource());
    }

}
