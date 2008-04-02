package com.calclab.emite.client.core.bosh;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.DispatcherPlugin;
import com.calclab.emite.client.core.dispatcher.DispatcherStateListener;
import com.calclab.emite.client.core.services.Connector;
import com.calclab.emite.client.core.services.Globals;
import com.calclab.emite.client.core.services.Scheduler;
import com.calclab.emite.client.core.services.ServicesPlugin;
import com.calclab.emite.client.core.services.XMLService;

public class BoshPlugin {
	private static final String COMPONENT_BOSH = "bosh";
	private static final String COMPONENT_RESPONDER = "bosh:responder";

	public static Bosh getConnection(final Container container) {
		return (Bosh) container.get(COMPONENT_BOSH);
	}

	public static Responder getResponder(final Container container) {
		return (Responder) container.get(COMPONENT_RESPONDER);
	}

	public static void install(final Container container, final BoshOptions options) {
		final Dispatcher dispatcher = DispatcherPlugin.getDispatcher(container);
		final Globals globals = ServicesPlugin.getGlobals(container);
		final Connector connector = ServicesPlugin.getConnector(container);
		final XMLService xmler = ServicesPlugin.getXMLService(container);
		final Scheduler scheduler = ServicesPlugin.getScheduler(container);

		final BoshResponder responder = new BoshResponder(dispatcher, xmler);
		container.register(COMPONENT_RESPONDER, responder);

		final BoshManager boshManager = new BoshManager(dispatcher, globals, connector, scheduler, responder, options);

		container.install(COMPONENT_BOSH, boshManager);

		dispatcher.addListener(new DispatcherStateListener() {
			public void afterDispatching() {
				boshManager.firePackets();
			}

			public void beforeDispatching() {
				boshManager.catchPackets();
			}
		});
	}
}
