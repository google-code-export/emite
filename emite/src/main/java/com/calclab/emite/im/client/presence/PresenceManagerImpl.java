package com.calclab.emite.im.client.presence;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Type;
import com.calclab.emite.im.client.roster.Roster;
import com.calclab.emite.im.client.roster.RosterManager;
import com.calclab.suco.client.signal.Signal;
import com.calclab.suco.client.signal.Slot;

public class PresenceManagerImpl implements PresenceManager {
	private Presence delayedPresence;
	private Presence ownPresence;
	private final Signal<Presence> onOwnPresenceChanged;
	private final Signal<Presence> onPresenceReceived;
	private final Session session;

	public PresenceManagerImpl(final Session session,
			final RosterManager rosterManager) {
		this.session = session;
		this.ownPresence = new Presence(Type.unavailable, null, null);
		this.onPresenceReceived = new Signal<Presence>(
				"presenceManager:onPresenceReceived");
		this.onOwnPresenceChanged = new Signal<Presence>(
				"presenceManager:onOwnPresenceChanged");

		// Upon connecting to the server and becoming an active resource, a
		// client SHOULD request the roster before sending initial presence

		rosterManager.onRosterReady(new Slot<Roster>() {
			public void onEvent(final Roster parameter) {
				final Presence initialPresence = new Presence(session
						.getCurrentUser());
				broadcastPresence(initialPresence);
				if (delayedPresence != null) {
					delayedPresence.setFrom(session.getCurrentUser());
					broadcastPresence(delayedPresence);
					delayedPresence = null;
				}
			}
		});

		session.onPresence(new Slot<Presence>() {
			public void onEvent(final Presence presence) {
				switch (presence.getType()) {
				case probe:
					session.send(ownPresence);
					break;
				case error:
					// FIXME: what should we do?
					Log.warn("Error presence!!!");
					break;
				default:
					onPresenceReceived.fire(presence);
					break;
				}
			}
		});
		session.onLoggedOut(new Slot<XmppURI>() {
			public void onEvent(final XmppURI user) {
				logOut(user);
			}
		});
	}

	/**
	 * Return the current logged in user presence or a Presence with type
	 * unavailable if logged out
	 * 
	 * @return
	 */
	public Presence getOwnPresence() {
		return ownPresence;
	}

	public void onOwnPresenceChanged(final Slot<Presence> listener) {
		onOwnPresenceChanged.add(listener);
	}

	public void onPresenceReceived(final Slot<Presence> slot) {
		onPresenceReceived.add(slot);
	}

	/**
	 * Set the logged in user's presence. If the user is not logged in, the
	 * presence is sent just after the initial presence
	 * 
	 * @see http://www.xmpp.org/rfcs/rfc3921.html#presence
	 * 
	 * @param presence
	 */
	public void setOwnPresence(final Presence presence) {
		if (session.isLoggedIn()) {
			broadcastPresence(presence);
		} else {
			delayedPresence = presence;
		}
	}


	private void broadcastPresence(final Presence presence) {
		presence.setFrom(session.getCurrentUser());
		session.send(presence);
		ownPresence = presence;
		onOwnPresenceChanged.fire(ownPresence);
	}

	/**
	 * 5.1.5. Unavailable Presence (rfc 3921)
	 * 
	 * Before ending its session with a server, a client SHOULD gracefully
	 * become unavailable by sending a final presence stanza that possesses no
	 * 'to' attribute and that possesses a 'type' attribute whose value is
	 * "unavailable" (optionally, the final presence stanza MAY contain one or
	 * more <status/> elements specifying the reason why the user is no longer
	 * available).
	 * 
	 * @param userURI
	 */
	private void logOut(final XmppURI userURI) {
		final Presence presence = new Presence(Type.unavailable, userURI,
				userURI.getHostURI());
		delayedPresence = null;
		broadcastPresence(presence);
	}


}
