package com.calclab.emite.client.im.roster;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.core.bosh.BoshPlugin;
import com.calclab.emite.client.core.bosh.Bosh;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.DispatcherPlugin;

public class RosterPlugin {

	public static Roster getRoster(final Container container) {
		return (Roster) container.get("roster");
	}

	public static void install(final Container container) {
		final Dispatcher dispatcher = DispatcherPlugin.getDispatcher(container);
		final Bosh bosh = BoshPlugin.getConnection(container);
		final Roster roster = new Roster();
		final RosterManager rosterManager = new RosterManager(dispatcher, bosh, roster);
		container.register("roster", roster);
		container.install("rosterManager", rosterManager);
	}

}
