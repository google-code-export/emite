package com.calclab.emite.client.x.im.presence;

import com.calclab.emite.client.Globals;
import com.calclab.emite.client.bosh.Connection;
import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.packet.stanza.Presence;
import com.calclab.emite.client.plugin.SenderPlugin;
import com.calclab.emite.client.plugin.dsl.Answer;
import com.calclab.emite.client.x.im.roster.Roster;

public class PresencePlugin extends SenderPlugin {
	private PresenceManager manager;

	public PresencePlugin(final Connection connection, final Globals globals) {
		super(connection);
	}

	/**
	 * Upon connecting to the server and becoming an active resource, a client
	 * SHOULD request the roster before sending initial presence
	 */
	@Override
	public void attach() {
		when.Event(Roster.Events.received).Send(manager.getInitialPresence());
		when.Packet("presence").Send(new Answer() {
			public Packet respondTo(final Packet received) {
				return manager.answerTo(new Presence(received));
			}
		});
	}

	@Override
	public void install() {
		manager = new PresenceManager(getGlobals());
		register("presence", manager);
	}

}
