package com.calclab.emite.client.xmpp.sasl;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.core.bosh.BoshPlugin;
import com.calclab.emite.client.core.bosh.Connection;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.DispatcherPlugin;
import com.calclab.emite.client.core.services.Globals;
import com.calclab.emite.client.core.services.ServicesPlugin;

public class SASLPlugin {

	private static final String COMPONENT_SASL = "sasl";

	public static void install(final Container container) {
		final Dispatcher dispatcher = DispatcherPlugin.getDispatcher(container);

		final Connection connection = BoshPlugin.getConnection(container);
		final Globals globals = ServicesPlugin.getGlobals(container);
		container.install(COMPONENT_SASL, new SASLManager(dispatcher, connection, globals));
	}

}
