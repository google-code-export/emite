package com.calclab.emite.client;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.calclab.emite.client.log.Logger;
import com.calclab.emite.client.x.im.session.Session;

public class RealServerTest {

	@Test
	public synchronized void testSimple() throws InterruptedException {
		final Xmpp xmpp = TestHelper.createXMPP(Logger.DEBUG);
		final Session session = xmpp.getSession();

		assertEquals(Session.State.disconnected, session.getState());
		xmpp.login("admin", "easyeasy");
		xmpp.send("testuser1", "hola!");
		wait(6000);
		assertEquals(Session.State.connected, session.getState());
	}
}
