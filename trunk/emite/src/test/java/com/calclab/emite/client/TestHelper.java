package com.calclab.emite.client;

import com.calclab.emite.client.bosh.BoshOptions;
import com.calclab.emite.client.connector.TestingConnector;
import com.calclab.emite.client.packet.TigaseXMLService;

public class TestHelper {

	public static Xmpp createXMPP() {
		return createXMPP(new BoshOptions("http://localhost:8181/http-bind/",
				"localhost"));
	}

	public static Xmpp createXMPP(final BoshOptions options) {
		final Container c = new Container();

		c.installDefaultPlugins(new TigaseXMLService(), new TestingConnector(),
				options);

		return new Xmpp(c.getComponents());
	}

}
