package com.calclab.emite.client.xmpp.sasl;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.core.bosh.BoshPlugin;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.services.Globals;
import com.calclab.emite.client.core.services.ServicesPlugin;

public class SASLPlugin {

	private static final String COMPONENT_SASL = "sasl";

	public static void install(final Container container) {
		final Emite emite = BoshPlugin.getEmite(container);
		final Globals globals = ServicesPlugin.getGlobals(container);
		container.install(COMPONENT_SASL, new SASLManager(emite, globals));
	}

}
