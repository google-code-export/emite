package com.calclab.emite.client;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.session.SessionListener;
import com.calclab.emite.client.xmpp.session.Session.State;

public class RealServerTest {

	@Test
	public synchronized void testSimple() throws InterruptedException {
		final Xmpp xmpp = TestHelper.createXMPP();
		final Session session = xmpp.getSession();

		assertEquals(Session.State.disconnected, session.getState());
		xmpp.getSession().addListener(new SessionListener() {
			public void onStateChanged(final State old, final State current) {
				Log.debug("Session state: " + current);
				switch (current) {
				case connected:
					xmpp.send("testuser1@localhost", "hola!");
				}
			}
		});
		xmpp.login("admin", "easyeasy");
		wait(6000);
		assertEquals(Session.State.connected, session.getState());
	}
}
