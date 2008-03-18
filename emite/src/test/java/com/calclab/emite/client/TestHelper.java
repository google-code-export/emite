package com.calclab.emite.client;

import com.calclab.emite.client.bosh.BoshOptions;
import com.calclab.emite.client.connector.SimpleConnector;
import com.calclab.emite.client.dispatcher.SimpleParser;
import com.calclab.emite.client.log.LoggerOutput;

public class TestHelper {

	public static Xmpp createXMPP() {
		return createXMPP(new BoshOptions("localhost:8181/http-bind/", "localhost"));
	}

	public static Xmpp createXMPP(final BoshOptions options) {
		final Container c = new Container(new LoggerOutput() {
			public void log(final int level, final String message) {
				System.out.println("[" + level + "]: " + message);
			}
		});

		c.createComponents(new SimpleParser(), new SimpleConnector(), options);
		c.installDefaultPlugins();

		return new Xmpp(c.getComponents());
	}

	public static TestingConn getConnection(final Xmpp xmpp) {
		return (TestingConn) xmpp.getComponents().getConnection();
	}
}
