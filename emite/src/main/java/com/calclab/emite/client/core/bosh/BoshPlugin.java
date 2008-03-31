package com.calclab.emite.client.core.bosh;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.connector.Connector;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.DispatcherPlugin;
import com.calclab.emite.client.core.dispatcher.DispatcherStateListener;
import com.calclab.emite.client.core.services.Globals;
import com.calclab.emite.client.core.services.ServicesPlugin;
import com.calclab.emite.client.packet.XMLService;
import com.calclab.emite.client.scheduler.Scheduler;

public class BoshPlugin {
	private static final String COMPONENT_BOSH = "bosh";

	public static Connection getConnection(final Container container) {
		return (Connection) container.get(COMPONENT_BOSH);
	}

	public static void install(final Container container, final BoshOptions options) {
		final Dispatcher dispatcher = DispatcherPlugin.getDispatcher(container);
		final Globals globals = ServicesPlugin.getGlobals(container);
		final Connector connector = ServicesPlugin.getConnector(container);
		final XMLService xmler = ServicesPlugin.getXMLService(container);
		final Scheduler scheduler = ServicesPlugin.getScheduler(container);

		final Bosh bosh = new Bosh(dispatcher, globals, connector, xmler, scheduler, options);

		container.register(COMPONENT_BOSH, bosh);
		dispatcher.addListener(new DispatcherStateListener() {
			public void afterDispatching() {
				bosh.firePackets();
			}

			public void beforeDispatching() {
				bosh.catchPackets();
			}
		});
	}

}
