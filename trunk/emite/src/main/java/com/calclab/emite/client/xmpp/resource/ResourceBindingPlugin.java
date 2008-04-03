package com.calclab.emite.client.xmpp.resource;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.components.ContainerPlugin;
import com.calclab.emite.client.components.Globals;
import com.calclab.emite.client.core.bosh.BoshPlugin;
import com.calclab.emite.client.core.bosh.Emite;

public class ResourceBindingPlugin {

	public static void install(final Container container) {
		final Emite emite = BoshPlugin.getEmite(container);
		final Globals globals = ContainerPlugin.getGlobals(container);
		container.install("resourceManager", new ResourceBindingManager(emite, globals));
	}

}
