package com.calclab.emite.client.x.im.roster;

import com.calclab.emite.client.components.Container;

public class RosterPlugin {

	public static Roster getRoster(final Container container) {
		return (Roster) container.get("roster");
	}

	private RosterManager rosterManager;

	public void install() {
		final Roster roster = new Roster();
		rosterManager = new RosterManager(roster);
		// register("roster", roster);
	}

}
