package com.calclab.emite.client.im.roster;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.core.bosh.BoshPlugin;
import com.calclab.emite.client.core.bosh.Emite;

public class RosterPlugin {

	public static Roster getRoster(final Container container) {
		return (Roster) container.get("roster");
	}

	public static void install(final Container container) {
		final Emite emite = BoshPlugin.getEmite(container);
		final Roster roster = new Roster();
		final RosterManager rosterManager = new RosterManager(emite, roster);
		container.register("roster", roster);
		container.install("rosterManager", rosterManager);
	}

}
