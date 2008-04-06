package com.calclab.emite.client;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.connector.HttpConnector;
import com.calclab.emite.client.connector.HttpConnectorListener;
import com.calclab.emite.client.core.bosh.BoshOptions;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.DispatcherPlugin;
import com.calclab.emite.client.packet.TigaseXMLService;
import com.calclab.emite.client.scheduler.ThreadScheduler;

public class TestHelper {

	public static AbstractXmpp createXMPP() {
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

	public static AbstractXmpp createXMPP(final BoshOptions options, final HttpConnectorListener listener) {

		final TigaseXMLService xmlService = new TigaseXMLService();
		final HttpConnector connector = new HttpConnector(listener);
		final ThreadScheduler scheduler = new ThreadScheduler();
		return Xmpp.create(connector, xmlService, scheduler, options);
	}

	public static AbstractXmpp createXMPP(final HttpConnectorListener httpConnectorListener) {

		final AbstractXmpp xmpp = createXMPP(new BoshOptions("http://localhost:8383/http-bind/", "localhost"),
				httpConnectorListener);
		final Container container = xmpp.getComponents();
		final Dispatcher dispatcher = DispatcherPlugin.getDispatcher(container);
		DispatcherPlugin.setDispatcher(container, new LoggerDispatcher(dispatcher));
		return xmpp;
	}

}
