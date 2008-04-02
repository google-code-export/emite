package com.calclab.emite.client.im.presence;

import java.util.ArrayList;

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.bosh.SenderComponent;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.core.services.Globals;
import com.calclab.emite.client.im.roster.Roster;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.PresenceType;

public class PresenceManager extends SenderComponent {
	private Presence currentPresence;
	private final Globals globals;
	private final ArrayList<PresenceListener> listeners;

	public PresenceManager(final Emite emite, final Globals globals) {
		super(emite);
		this.globals = globals;
		this.listeners = new ArrayList<PresenceListener>();
		this.currentPresence = null;
	}

	public void addListener(final PresenceListener presenceListener) {
		this.listeners.add(presenceListener);
	}

	public Packet answerTo(final Presence presence) {
		return new Presence(globals.getXmppURI()).To(presence.getFrom());
	}

	public Packet answerToSessionLogout() {
		return new Presence(globals.getXmppURI()).With("type", "unavailable");
	}

	/**
	 * Upon connecting to the server and becoming an active resource, a client
	 * SHOULD request the roster before sending initial presence
	 */
	@Override
	public void attach() {
		when(Roster.Events.ready, new PacketListener() {
			public void handle(final Packet received) {
				currentPresence = createInitialPresence();
				emite.send(currentPresence);
			}
		});

		when("presence", new PacketListener() {
			public void handle(final Packet received) {
				onPresenceReceived(new Presence(received));
			}
		});

		when(Session.Events.logout, new PacketListener() {
			public void handle(final Packet received) {
				emite.send(answerToSessionLogout());
			}
		});

	}

	protected void onPresenceReceived(final Presence presence) {
		final PresenceType type = presence.getType();
		switch (type) {
		case subscribe:
			fireSubscriptionRequest(presence);
			break;
		case probe:
			emite.send(currentPresence);
			break;
		case error:
			break;
		default:
			firePresenceReceived(presence);
			break;
		}
	}

	private Presence createInitialPresence() {
		return new Presence(globals.getXmppURI()).With(Presence.Show.chat);
	}

	private void firePresenceReceived(final Presence presence) {
		for (final PresenceListener listener : listeners) {
			listener.onPresenceReceived(presence);
		}
	}

	private void fireSubscriptionRequest(final Presence presence) {
		for (final PresenceListener listener : listeners) {
			listener.onSubscriptionRequest(presence);
		}
	}
}
