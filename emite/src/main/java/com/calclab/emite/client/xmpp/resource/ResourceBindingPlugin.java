package com.calclab.emite.client.xmpp.resource;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.core.bosh.BoshPlugin;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.DispatcherPlugin;
import com.calclab.emite.client.core.services.Globals;
import com.calclab.emite.client.core.services.ServicesPlugin;

public class ResourceBindingPlugin {

	public static void install(final Container container) {
		final Dispatcher dispatcher = DispatcherPlugin.getDispatcher(container);
		final Emite emite = BoshPlugin.getEmite(container);
		final Globals globals = ServicesPlugin.getGlobals(container);
		container.install("resourceManager", new ResourceBindingManager(emite, globals));
	}

}
