package com.calclab.emite.client.xmpp.stanzas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.*;

public class XmppUriTest {

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
    public void checkUriFormatWithoutHostFails() {
	uri("xmpp:test/res");
    }

    @Test(expected = RuntimeException.class)
    public void checkUriFormatWithoutNodeFails() {
	uri("xmpp:@example/res");
    }

    public void checkUriFormatWithoutResource() {
	final XmppURI uri = uri("xmpp:test@example");
	assertFalse(uri.hasResource());
    }

    @Test
    public void checkUriFormatWithoutXmpp() {
	final XmppURI uri = uri("test@example/res");
	assertEquals("test", uri.getNode());
	assertEquals("example", uri.getHost());
	assertEquals("res", uri.getResource());
    }

}
