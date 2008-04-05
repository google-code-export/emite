package com.calclab.emite.client.xmpp.sasl;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.components.ContainerPlugin;
import com.calclab.emite.client.components.Globals;
import com.calclab.emite.client.core.bosh.BoshPlugin;
import com.calclab.emite.client.core.bosh.Emite;

public class SASLPlugin {

	private static final String COMPONENT_SASL = "sasl";

	public static void install(final Container container) {
		final Emite emite = BoshPlugin.getEmite(container);
		final Globals globals = ContainerPlugin.getGlobals(container);
		container.install(COMPONENT_SASL, new SASLManager(emite, globals));
	}

}
