package com.calclab.emite.client.x.core;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.core.bosh.BoshPlugin;
import com.calclab.emite.client.core.bosh.Connection;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.DispatcherPlugin;
import com.calclab.emite.client.core.services.Globals;
import com.calclab.emite.client.core.services.ServicesPlugin;

public class ResourcePlugin {

	public static void install(final Container container) {
		final Dispatcher dispatcher = DispatcherPlugin.getDispatcher(container);
		final Connection connection = BoshPlugin.getConnection(container);
		final Globals globals = ServicesPlugin.getGlobals(container);
		container.register("resourceManager", new ResourceManager(dispatcher, connection, globals));
	}

}
