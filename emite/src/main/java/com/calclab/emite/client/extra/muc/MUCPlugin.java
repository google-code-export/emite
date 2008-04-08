package com.calclab.emite.client.extra.muc;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.components.ContainerPlugin;
import com.calclab.emite.client.components.Globals;
import com.calclab.emite.client.core.bosh.BoshPlugin;
import com.calclab.emite.client.core.bosh.Emite;

public class MUCPlugin {

    private static final String COMPONENTS_MANAGER = "muc:manager";

    public static RoomManager getRoomManager(final Container components) {
	return (RoomManager) components.get(COMPONENTS_MANAGER);
    }

    public static void install(final Container container) {
	final Emite emite = BoshPlugin.getEmite(container);
	final Globals globals = ContainerPlugin.getGlobals(container);
	final MUCRoomManager rooms = new MUCRoomManager(emite, globals);
	container.install(COMPONENTS_MANAGER, rooms);
    }
}
