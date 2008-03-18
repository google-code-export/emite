package com.calclab.emite.client;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.calclab.emite.client.packet.stanza.IQ;
import com.calclab.emite.client.x.im.session.Session;

public class BasicTest {

	@Test
	public synchronized void testSimple() throws InterruptedException {
		final Xmpp xmpp = TestHelper.createXMPP();
		final Session session = xmpp.getSession();

		assertEquals(Session.State.disconnected, session.getState());
		xmpp.login("admin", "easyeasy");
		wait(2000);
		assertEquals(Session.State.connecting, session.getState());
		assertEquals(Session.State.authorized, session.getState());
		final IQ iq = new IQ("bindRequest", IQ.Type.set);
		iq.add("bind", null).add("jid", null).setText("theJID");
		assertEquals(Session.State.connected, session.getState());
	}
}
