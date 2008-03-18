package com.calclab.emite.client;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.calclab.emite.client.packet.stanza.IQ;
import com.calclab.emite.client.x.im.session.Session;

public class IntegrationTests {

	@Test
	public synchronized void testSimple() throws InterruptedException {
		final Xmpp xmpp = TestHelper.createXMPP();
		final Session session = xmpp.getSession();

		assertEquals(Session.State.disconnected, session.getState());
		xmpp.login("admin", "easyeasy");
		final IQ iq = new IQ("bindRequest", IQ.Type.set);
		iq.add("bind", null).add("jid", null).setText("theJID");
		wait(6000);
		assertEquals(Session.State.connected, session.getState());
	}
}
