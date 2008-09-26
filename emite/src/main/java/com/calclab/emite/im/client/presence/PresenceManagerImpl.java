package com.calclab.emite.im.client.presence;

import java.util.Collection;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.SessionReadyManager;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Type;
import com.calclab.emite.im.client.roster.Roster;
import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.suco.client.listener.Event;
import com.calclab.suco.client.listener.Event0;
import com.calclab.suco.client.listener.Listener;
import com.calclab.suco.client.listener.Listener0;
import com.calclab.suco.client.log.Logger;

public class PresenceManagerImpl implements PresenceManager, SessionReadyManager {
    private Presence ownPresence;
    private final Event<Presence> onOwnPresenceChanged;
    private final Session session;
    private final Event0 onSessionReady;

    public PresenceManagerImpl(final Session session, final Roster roster) {
	this.session = session;
	this.ownPresence = new Presence(Type.unavailable, null, null);
	this.onOwnPresenceChanged = new Event<Presence>("presenceManager:onOwnPresenceChanged");
	this.onSessionReady = new Event0("presenceManager:sessionReady");

	// Upon connecting to the server and becoming an active resource, a
	// client SHOULD request the roster before sending initial presence
	roster.onRosterRetrieved(new Listener<Collection<RosterItem>>() {
	    public void onEvent(final Collection<RosterItem> parameter) {
		Logger.debug("Sending initial presence");
		final Presence initialPresence = new Presence(session.getCurrentUser());
		broadcastPresence(initialPresence);
		onSessionReady.fire();
	    }
	});

	session.onPresence(new Listener<Presence>() {
	    public void onEvent(final Presence presence) {
		switch (presence.getType()) {
		case probe:
		    session.send(ownPresence);
		    break;
		case error:
		    // FIXME: what should we do?
		    Log.warn("Error presence!!!");
		    break;
		}
	    }
	});

	session.onStateChanged(new Listener<Session.State>() {
	    public void onEvent(final Session.State state) {
		if (state == Session.State.loggingOut) {
		    logOut(session.getCurrentUser());
		}
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

    public void onOwnPresenceChanged(final Listener<Presence> listener) {
	onOwnPresenceChanged.add(listener);
    }

    public void onSessionReady(final Listener0 listener) {
	onSessionReady.add(listener);
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
	broadcastPresence(presence);
    }

    private void broadcastPresence(final Presence presence) {
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
	final Presence presence = new Presence(Type.unavailable, userURI, null);
	broadcastPresence(presence);
    }

}