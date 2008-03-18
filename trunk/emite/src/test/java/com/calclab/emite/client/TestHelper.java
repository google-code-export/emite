package com.calclab.emite.client;

import com.calclab.emite.client.bosh.BoshOptions;
import com.calclab.emite.client.connector.TestingConnector;
import com.calclab.emite.client.dispatcher.TestingParser;
import com.calclab.emite.client.log.Logger;
import com.calclab.emite.client.log.LoggerAdapter;
import com.calclab.emite.client.log.LoggerOutput;

public class TestHelper {

	public static Logger createLogger() {
		return new LoggerAdapter(createLoggerOutput());
	}

	public static Xmpp createXMPP() {
		return createXMPP(new BoshOptions("http://localhost:8181/http-bind/", "localhost"));
	}

	public static Xmpp createXMPP(final BoshOptions options) {
		final Container c = new Container(createLoggerOutput());

		final Logger logger = c.getComponents().getLogger();
		c.createComponents(new TestingParser(logger), new TestingConnector(logger), options);
		c.installDefaultPlugins();

		return new Xmpp(c.getComponents());
	}

	private static LoggerOutput createLoggerOutput() {
		return new LoggerOutput() {
			public void log(final int level, final String message) {
				System.out.println("[" + level + "]: " + message);
			}
		};
	}
}
