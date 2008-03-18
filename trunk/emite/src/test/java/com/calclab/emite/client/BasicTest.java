package com.calclab.emite.client;

import org.junit.Test;

public class BasicTest {

	@Test
	public void testSimple() {
		final Xmpp xmpp = TestHelper.createXMPP();

		xmpp.login("user", "pass");
	}
}
