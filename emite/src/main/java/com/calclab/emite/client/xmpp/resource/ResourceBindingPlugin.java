package com.calclab.emite.client.xmpp.resource;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.core.bosh.BoshPlugin;
import com.calclab.emite.client.core.bosh.Bosh;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.DispatcherPlugin;
import com.calclab.emite.client.core.services.Globals;
import com.calclab.emite.client.core.services.ServicesPlugin;

public class ResourceBindingPlugin {

	public static void install(final Container container) {
		final Dispatcher dispatcher = DispatcherPlugin.getDispatcher(container);
		final Bosh bosh = BoshPlugin.getConnection(container);
		final Globals globals = ServicesPlugin.getGlobals(container);
		container.install("resourceManager", new ResourceBindingManager(dispatcher, bosh, globals));
	}

}
