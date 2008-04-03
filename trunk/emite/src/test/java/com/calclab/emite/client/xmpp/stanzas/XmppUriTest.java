package com.calclab.emite.client.xmpp.stanzas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class XmppUriTest {

	@Test
	public void checkEqualsContract() {
		final XmppURI uri1 = XmppURI.parseURI("xmpp:test@example/res");
		final XmppURI uri2 = XmppURI.parseURI("xmpp:test@example/res");
		assertEquals(uri1, uri2);
		assertEquals(uri1.hashCode(), uri2.hashCode());
	}

	@Test
	public void checkUriFormat() {
		final XmppURI uri = XmppURI.parseURI("xmpp:test@example/res");
		assertEquals("test", uri.getNode());
		assertEquals("example", uri.getHost());
		assertEquals("res", uri.getResource());
	}

	@Test(expected = RuntimeException.class)
	public void checkUriFormatWithOnlyAtFails() {
		XmppURI.parseURI("xmpp:@");
	}

	@Test(expected = RuntimeException.class)
	public void checkUriFormatWithOnlyHostFails() {
		XmppURI.parseURI("xmpp:@example");
	}

	@Test(expected = RuntimeException.class)
	public void checkUriFormatWithOnlyNodeFails() {
		XmppURI.parseURI("xmpp:test@");
	}

	@Test(expected = RuntimeException.class)
	public void checkUriFormatWithOnlySlashFails() {
		XmppURI.parseURI("xmpp:/");
	}

	@Test(expected = RuntimeException.class)
	public void checkUriFormatWithoutHost() {
		XmppURI.parseURI("xmpp:test@/res");
	}

	@Test(expected = RuntimeException.class)
	public void checkUriFormatWithoutHostFails() {
		XmppURI.parseURI("xmpp:test/res");
	}

	@Test(expected = RuntimeException.class)
	public void checkUriFormatWithoutNodeFails() {
		XmppURI.parseURI("xmpp:@example/res");
	}

	public void checkUriFormatWithoutResource() {
		final XmppURI uri = XmppURI.parseURI("xmpp:test@example");
		assertFalse(uri.hasResource());
	}

	@Test
	public void checkUriFormatWithoutXmpp() {
		final XmppURI uri = XmppURI.parseURI("test@example/res");
		assertEquals("test", uri.getNode());
		assertEquals("example", uri.getHost());
		assertEquals("res", uri.getResource());
	}

}
