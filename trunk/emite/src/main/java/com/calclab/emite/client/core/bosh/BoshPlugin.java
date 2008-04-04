package com.calclab.emite.client.core.bosh;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.components.ContainerPlugin;
import com.calclab.emite.client.components.Globals;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.DispatcherPlugin;
import com.calclab.emite.client.core.services.Connector;
import com.calclab.emite.client.core.services.Scheduler;
import com.calclab.emite.client.core.services.ServicesPlugin;
import com.calclab.emite.client.core.services.XMLService;

public class BoshPlugin {
    private static final String COMPONENT_BOSH = "bosh:manager";
    private static final String COMPONENT_EMITE = "emite";

    public static Emite getEmite(final Container container) {
	return (Emite) container.get(COMPONENT_EMITE);
    }

    public static void install(final Container container, final BoshOptions options) {
	final Dispatcher dispatcher = DispatcherPlugin.getDispatcher(container);
	final Globals globals = ContainerPlugin.getGlobals(container);
	final Connector connector = ServicesPlugin.getConnector(container);
	final XMLService xmler = ServicesPlugin.getXMLService(container);
	final Scheduler scheduler = ServicesPlugin.getScheduler(container);

	final EmiteBosh emite = new EmiteBosh(dispatcher, xmler);
	container.install(COMPONENT_EMITE, emite);

	final BoshManager boshManager = new BoshManager(dispatcher, globals, connector, scheduler, emite, options);

	container.install(COMPONENT_BOSH, boshManager);

    }

}
