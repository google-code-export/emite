package com.calclab.emite.client.x.im.presence;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.core.bosh.BoshPlugin;
import com.calclab.emite.client.core.bosh.Connection;
import com.calclab.emite.client.core.services.Globals;
import com.calclab.emite.client.core.services.ServicesPlugin;

public class PresencePlugin {

	public static void install(final Container container) {
		final Connection connection = BoshPlugin.getConnection(container);
		final Globals globals = ServicesPlugin.getGlobals(container);
		final PresenceManager manager = new PresenceManager(connection, globals);
		container.register("presenceManager", manager);
	}

}
