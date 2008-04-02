package com.calclab.emite.client.im.presence;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.core.bosh.BoshPlugin;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.DispatcherPlugin;
import com.calclab.emite.client.core.services.Globals;
import com.calclab.emite.client.core.services.ServicesPlugin;

public class PresencePlugin {

	public static void install(final Container container) {
		final Dispatcher dispatcher = DispatcherPlugin.getDispatcher(container);
		final Emite emite = BoshPlugin.getEmite(container);
		final Globals globals = ServicesPlugin.getGlobals(container);
		final PresenceManager manager = new PresenceManager(emite, globals);
		container.install("presenceManager", manager);
	}

}
