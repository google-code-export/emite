package com.calclab.emite.client;

import com.calclab.emite.client.bosh.BoshOptions;
import com.calclab.emite.client.connector.TestingConnector;
import com.calclab.emite.client.dispatcher.TestingParser;
import com.calclab.emite.client.log.Logger;
import com.calclab.emite.client.log.LoggerAdapter;
import com.calclab.emite.client.log.LoggerOutput;

public class TestHelper {

	public static Logger createLogger(final int level) {
		return new LoggerAdapter(createLoggerOutput(level));
	}

	public static Xmpp createXMPP(final BoshOptions options, final int level) {
		final Container c = new Container(createLoggerOutput(level));

		final Logger logger = c.getComponents().getLogger();
		c.installDefaultPlugins(new TestingParser(logger), new TestingConnector(logger), options);

		return new Xmpp(c.getComponents());
	}

	public static Xmpp createXMPP(final int level) {
		return createXMPP(new BoshOptions("http://localhost:8181/http-bind/", "localhost"), level);
	}

	private static LoggerOutput createLoggerOutput(final int globalLevel) {
		return new LoggerOutput() {
			public void log(final int level, final String message) {
				if (level <= globalLevel) {
					System.out.println("[" + level + "]: " + message);
				}
			}
		};
	}
}
