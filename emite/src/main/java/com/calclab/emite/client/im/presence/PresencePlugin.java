package com.calclab.emite.client.im.presence;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.components.ContainerPlugin;
import com.calclab.emite.client.components.Globals;
import com.calclab.emite.client.core.bosh.BoshPlugin;
import com.calclab.emite.client.core.bosh.Emite;

public class PresencePlugin {
	private static final String COMPONENT_MANAGER = "presence:manager";

	public static PresenceManager getManager(final Container container) {
		return (PresenceManager) container.get(COMPONENT_MANAGER);
	}

	public static void install(final Container container) {
		final Emite emite = BoshPlugin.getEmite(container);
		final Globals globals = ContainerPlugin.getGlobals(container);
		final PresenceManager manager = new PresenceManager(emite, globals);
		container.install(COMPONENT_MANAGER, manager);
	}

}
