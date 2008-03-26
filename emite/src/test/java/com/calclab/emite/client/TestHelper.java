package com.calclab.emite.client;

import com.calclab.emite.client.bosh.BoshOptions;
import com.calclab.emite.client.connector.HttpConnector;
import com.calclab.emite.client.connector.HttpConnectorListener;
import com.calclab.emite.client.packet.TigaseXMLService;

public class TestHelper {

	public static Xmpp createXMPP() {
		return createXMPP(new HttpConnectorListener() {
			public void onError(String id, final String cause) {
			}

			public void onFinish(final String id, long duration) {
			}

			public void onResponse(String id, final String response) {
			}

			public void onSend(String id, final String xml) {
			}

			public void onStart(final String id) {
			}

		});
	}

	public static Xmpp createXMPP(final BoshOptions options,
			final HttpConnectorListener listener) {
		final Container c = new Container();

		c.installDefaultPlugins(new TigaseXMLService(), new HttpConnector(
				listener), options);

		return new Xmpp(c.getComponents());
	}

	public static Xmpp createXMPP(
			final HttpConnectorListener httpConnectorListener) {
		return createXMPP(new BoshOptions("http://localhost:8181/http-bind/",
				"localhost"), httpConnectorListener);
	}

}
