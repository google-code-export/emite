package com.calclab.emite.client.im.presence;

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.bosh.SenderComponent;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.core.services.Globals;
import com.calclab.emite.client.im.roster.Roster;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.stanzas.Presence;

public class PresenceManager extends SenderComponent {
	private final Globals globals;

	public PresenceManager(final Emite emite, final Globals globals) {
		super(emite);
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
		when(Roster.Events.ready, new PacketListener() {
			public void handle(final Packet received) {
				emite.send(getInitialPresence());
			}
		});

		when("presence", new PacketListener() {
			public void handle(final Packet received) {
			}
		});

		when(Session.Events.logout, new PacketListener() {
			public void handle(final Packet received) {
				emite.send(answerToSessionLogout());
			}
		});

	}

	private Presence getInitialPresence() {
		return new Presence(globals.getJID()).With(Presence.Show.chat);
	}
}
