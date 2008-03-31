package com.calclab.emite.client.x.core;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.core.bosh.BoshPlugin;
import com.calclab.emite.client.core.bosh.Connection;
import com.calclab.emite.client.core.services.Globals;
import com.calclab.emite.client.core.services.ServicesPlugin;

public class SASLPlugin {

	private static final String COMPONENT_SASL = "sasl";

	public static void install(final Container container) {
		final Connection connection = BoshPlugin.getConnection(container);
		final Globals globals = ServicesPlugin.getGlobals(container);
		container.register(COMPONENT_SASL, new SASLManager(connection, globals));
	}

}
