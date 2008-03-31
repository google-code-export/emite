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

	public static Bosh getConnection(final Container container) {
		return (Bosh) container.get(COMPONENT_BOSH);
	}

	public static void install(final Container container, final BoshOptions options) {
		final Dispatcher dispatcher = DispatcherPlugin.getDispatcher(container);
		final Globals globals = ServicesPlugin.getGlobals(container);
		final Connector connector = ServicesPlugin.getConnector(container);
		final XMLService xmler = ServicesPlugin.getXMLService(container);
		final Scheduler scheduler = ServicesPlugin.getScheduler(container);

		final BoshManager boshManager = new BoshManager(dispatcher, globals, connector, xmler, scheduler, options);

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
