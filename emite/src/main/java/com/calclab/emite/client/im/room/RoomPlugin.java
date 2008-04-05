package com.calclab.emite.client.im.room;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.core.bosh.BoshPlugin;
import com.calclab.emite.client.core.bosh.Emite;

public class RoomPlugin {
	private static final String COMPONENT_MANAGER = "room:manager";

	public static void install(final Container container) {
		final Emite emite = BoshPlugin.getEmite(container);
		final RoomManager manager = new RoomManager(emite);
		container.install(COMPONENT_MANAGER, manager);
	}
}
