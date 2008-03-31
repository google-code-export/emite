package com.calclab.emite.client.x.im.roster;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.core.bosh.BoshPlugin;
import com.calclab.emite.client.core.bosh.Connection;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.DispatcherPlugin;

public class RosterPlugin {

	public static Roster getRoster(final Container container) {
		return (Roster) container.get("roster");
	}

	public static void install(final Container container) {
		final Dispatcher dispatcher = DispatcherPlugin.getDispatcher(container);
		final Connection connection = BoshPlugin.getConnection(container);
		final Roster roster = new Roster();
		final RosterManager rosterManager = new RosterManager(dispatcher, connection, roster);
		container.register("roster", roster);
		container.install("rosterManager", rosterManager);
	}

}
