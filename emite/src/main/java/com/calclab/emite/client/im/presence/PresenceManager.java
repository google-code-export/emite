package com.calclab.emite.client.im.presence;

import com.calclab.emite.client.components.Answer;
import com.calclab.emite.client.components.SenderComponent;
import com.calclab.emite.client.core.bosh.Connection;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.core.services.Globals;
import com.calclab.emite.client.im.roster.Roster;
import com.calclab.emite.client.im.session.Session;
import com.calclab.emite.client.xmpp.stanzas.Presence;

public class PresenceManager extends SenderComponent {
	private final Globals globals;

	public PresenceManager(final Dispatcher dispatcher, final Connection connection, final Globals globals) {
		super(dispatcher, connection);
		this.globals = globals;
	}

	public Packet answerTo(final Presence presence) {
		return new Presence(globals.getJID()).To(presence.getFrom());
	}

	public Packet answerToSessionLogout() {
		return new Presence(globals.getJID()).With("type", "unavailable");
	}

	/**
	 * Upon connecting to the server and becoming an active resource, a client
	 * SHOULD request the roster before sending initial presence
	 */
	@Override
	public void attach() {
		when(Roster.Events.ready).Send(new Answer() {
			public Packet respondTo(final Packet received) {
				return getInitialPresence();
			}
		});
		when(new Presence()).Send(new Answer() {
			public Packet respondTo(final Packet received) {
				return answerTo(new Presence(received));
			}
		});
		when(Session.Events.logout).Send(new Answer() {
			public Packet respondTo(final Packet received) {
				return answerToSessionLogout();
			}
		});
	}

	private Presence getInitialPresence() {
		return new Presence(globals.getJID()).With(Presence.Show.chat);
	}
}
