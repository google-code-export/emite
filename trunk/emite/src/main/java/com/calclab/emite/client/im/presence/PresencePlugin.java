package com.calclab.emite.client.im.presence;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.core.bosh.BoshPlugin;
import com.calclab.emite.client.core.bosh.Bosh;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.DispatcherPlugin;
import com.calclab.emite.client.core.services.Globals;
import com.calclab.emite.client.core.services.ServicesPlugin;

public class PresencePlugin {

	public static void install(final Container container) {
		final Dispatcher dispatcher = DispatcherPlugin.getDispatcher(container);
		final Bosh bosh = BoshPlugin.getConnection(container);
		final Globals globals = ServicesPlugin.getGlobals(container);
		final PresenceManager manager = new PresenceManager(dispatcher, bosh, globals);
		container.install("presenceManager", manager);
	}

}
