package com.calclab.emite.client;

import com.calclab.emite.client.bosh.BoshOptions;
import com.calclab.emite.client.connector.HttpConnector;
import com.calclab.emite.client.connector.HttpConnectorListener;
import com.calclab.emite.client.dispatcher.Dispatcher;
import com.calclab.emite.client.dispatcher.DispatcherPlugin;
import com.calclab.emite.client.packet.TigaseXMLService;
import com.calclab.emite.client.scheduler.ThreadScheduler;

public class TestHelper {

	public static Xmpp createXMPP() {
		return createXMPP(new HttpConnectorListener() {
			public void onError(final String id, final String cause) {
			}

			public void onFinish(final String id, final long duration) {
			}

			public void onResponse(final String id, final String response) {
			}

			public void onSend(final String id, final String xml) {
			}

			public void onStart(final String id) {
			}

		});
	}

	public static Xmpp createXMPP(final BoshOptions options,
			final HttpConnectorListener listener) {
		final Container c = new Container();

		c.installDefaultPlugins(new TigaseXMLService(), new HttpConnector(
				listener), new ThreadScheduler(), options);

		return new Xmpp(c.getComponents());
	}

	public static Xmpp createXMPP(
			final HttpConnectorListener httpConnectorListener) {
		final Xmpp xmpp = createXMPP(new BoshOptions(
				"http://localhost:8181/http-bind/", "localhost"),
				httpConnectorListener);
		final Components components = xmpp.getComponents();
		final Dispatcher dispatcher = DispatcherPlugin
				.getDispatcher(components);
		DispatcherPlugin.setDispatcher(components, new LoggerDispatcher(
				dispatcher));
		return xmpp;
	}

}
