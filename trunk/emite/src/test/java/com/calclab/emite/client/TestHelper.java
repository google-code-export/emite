package com.calclab.emite.client;

import com.calclab.emite.client.log.LoggerOutput;

public class TestHelper {

	public static Xmpp createXMPP() {
		final Components cs = XMPPPlugin.createComponents(new LoggerOutput() {
			public void log(final int level, final String message) {
				System.out.println("[" + level + "]: " + message);
			}
		});

		cs.setConnection(new TestingConn(cs.getLogger()));

		XMPPPlugin.installPlugins(cs);

		return new Xmpp(cs);
	}

	public static TestingConn getConnection(final Xmpp xmpp) {
		return (TestingConn) xmpp.getComponents().getConnection();
	}
}
