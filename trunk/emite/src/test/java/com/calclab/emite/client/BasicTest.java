package com.calclab.emite.client;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.calclab.emite.client.dispatcher.Dispatcher;
import com.calclab.emite.client.packet.BasicPacket;
import com.calclab.emite.client.packet.stanza.IQ;
import com.calclab.emite.client.x.im.session.Session;

public class BasicTest {

	@Test
	public void testSimple() {
		final Xmpp xmpp = TestHelper.createXMPP();
		final Session session = xmpp.getSession();
		final Dispatcher dispatcher = xmpp.getComponents().getDispatcher();

		assertEquals(Session.State.disconnected, session.getState());
		xmpp.login("user", "pass");
		assertEquals(Session.State.connecting, session.getState());
		dispatcher.publish(new BasicPacket("stream:features", null));
		dispatcher.publish(new BasicPacket("success", "urn:ietf:params:xml:ns:xmpp-sasl"));
		assertEquals(Session.State.authorized, session.getState());
		final IQ iq = new IQ("bindRequest", IQ.Type.set);
		iq.add("bind", null).add("jid", null).setText("theJID");
		dispatcher.publish(iq);
		dispatcher.publish(new IQ("requestSession", IQ.Type.set));
		assertEquals(Session.State.connected, session.getState());
	}
}
