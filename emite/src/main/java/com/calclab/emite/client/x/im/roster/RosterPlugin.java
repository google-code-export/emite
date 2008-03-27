package com.calclab.emite.client.x.im.roster;

import com.calclab.emite.client.Components;
import com.calclab.emite.client.bosh.Connection;
import com.calclab.emite.client.plugin.SenderPlugin;
import com.calclab.emite.client.x.core.ResourcePlugin;

public class RosterPlugin extends SenderPlugin {

	public static Roster getRoster(final Components components) {
		return (Roster) components.get("roster");
	}

	private RosterManager rosterManager;

	public RosterPlugin(final Connection connection) {
		super(connection);
	}

	/**
	 * Upon connecting to the server and becoming an active resource, a client
	 * SHOULD request the roster before sending initial presence
	 */
	@Override
	public void attach() {
		when.Event(ResourcePlugin.Events.binded).Send(rosterManager.requestRoster);
		when.IQ("roster").Publish(rosterManager.setRosterItems);
	}

	@Override
	public void install() {
		final Roster roster = new Roster();
		rosterManager = new RosterManager(roster);
		register("roster", roster);
	}

}
