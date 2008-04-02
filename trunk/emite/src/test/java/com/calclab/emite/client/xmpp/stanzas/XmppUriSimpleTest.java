package com.calclab.emite.client.xmpp.stanzas;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class XmppUriSimpleTest {

	@Test
	public void checkUriFormat() {
		final XmppURI uri = XmppURI.parseURI("xmpp:test@example/res");
		assertEquals("test", uri.getJid().getNode());
		assertEquals("example", uri.getJid().getHost());
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

	@Test(expected = RuntimeException.class)
	public void checkUriFormatWithoutResourceFails() {
		XmppURI.parseURI("xmpp:test@example");
	}

	@Test(expected = RuntimeException.class)
	public void checkUriFormatWithoutXmpp() {
		XmppURI.parseURI("test@example/res");
	}

}
