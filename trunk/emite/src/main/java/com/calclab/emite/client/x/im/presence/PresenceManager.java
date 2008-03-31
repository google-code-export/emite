package com.calclab.emite.client.x.im.presence;

import com.calclab.emite.client.components.SenderComponent;
import com.calclab.emite.client.core.bosh.Connection;
import com.calclab.emite.client.core.services.Globals;
import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.packet.stanza.Presence;
import com.calclab.emite.client.plugin.dsl.Answer;
import com.calclab.emite.client.x.im.session.Session;

public class PresenceManager extends SenderComponent {
	private final Globals globals;

	public PresenceManager(final Connection connection, final Globals globals) {
		super(connection);
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
		when(Session.Events.login).Send(getInitialPresence());
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

	/**
	 * 5.1.1. Initial Presence
	 * 
	 * After establishing a session, a client SHOULD send initial presence to
	 * the server in order to signal its availability for communications. As
	 * defined herein, the initial presence stanza (1) MUST possess no 'to'
	 * address (signalling that it is meant to be broadcasted by the server on
	 * behalf of the client) and (2) MUST possess no 'type' attribute
	 * (signalling the user's availability). After sending initial presence, an
	 * active resource is said to be an "available resource"
	 * 
	 * @link http://www.xmpp.org/rfcs/rfc3921.html#presence
	 * 
	 */
	public Answer getInitialPresence() {
		return new Answer() {
			public Packet respondTo(final Packet received) {
				return new Presence(globals.getJID()).With(Presence.Show.chat);
			}
		};
	}
}
