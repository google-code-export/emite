/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.client.im.presence;

import static com.calclab.emite.client.core.dispatcher.matcher.Matchers.when;

import java.util.ArrayList;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.im.roster.RosterManager;
import com.calclab.emite.client.xmpp.session.SessionComponent;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.client.xmpp.stanzas.Presence.Type;
import com.calclab.modular.client.signal.Listener;
import com.calclab.modular.client.signal.Signal;

public class PresenceManager extends SessionComponent {
    private Presence delayedPresence;
    private Presence ownPresence;
    private final ArrayList<PresenceListener> listeners;
    private final Signal<Presence> onOwnPresenceChanged;

    public PresenceManager(final Emite emite) {
	super(emite);
	this.listeners = new ArrayList<PresenceListener>();
	this.ownPresence = new Presence(Type.unavailable, null, null);
	this.onOwnPresenceChanged = new Signal<Presence>();
	install();
    }

    public void addListener(final PresenceListener presenceListener) {
	this.listeners.add(presenceListener);
    }

    /**
     * Returns the current's user presence
     * 
     * @see getOwnPresence
     * @return The current users presence
     */
    @Deprecated
    public Presence getCurrentPresence() {
	return ownPresence;
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

    /**
     * 5.1.5. Unavailable Presence (rfc 3921)
     * 
     * Before ending its session with a server, a client SHOULD gracefully
     * become unavailable by sending a final presence stanza that possesses no
     * 'to' attribute and that possesses a 'type' attribute whose value is
     * "unavailable" (optionally, the final presence stanza MAY contain one or
     * more <status/> elements specifying the reason why the user is no longer
     * available).
     */
    @Override
    public void logOut() {
	if (isLoggedIn()) {
	    final Presence presence = new Presence(Type.unavailable, userURI, userURI.getHostURI());
	    delayedPresence = null;
	    broadcastPresence(presence);
	}
	super.logOut();
    }

    public void onOwnPresenceChanged(final Listener<Presence> listener) {
	onOwnPresenceChanged.add(listener);
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
	if (isLoggedIn()) {
	    broadcastPresence(presence);
	} else {
	    delayedPresence = presence;
	}
    }

    /**
     * 5.1.2. Presence Broadcast
     * 
     * After sending initial presence, the user MAY update its presence
     * information for broadcasting at any time during its session by sending a
     * presence stanza with no 'to' address and either no 'type' attribute or a
     * 'type' attribute with a value of "unavailable". (
     */
    public void setOwnPresence(final String statusMessage, final Show show) {
	final Presence presence = new Presence();

	if (show != null && show != Show.notSpecified) {
	    presence.setShow(show);
	}
	if (statusMessage != null) {
	    presence.setStatus(statusMessage);
	}

	setOwnPresence(presence);
    }

    private void broadcastPresence(final Presence presence) {
	presence.setFrom(userURI);
	emite.send(presence);
	ownPresence = presence;
	onOwnPresenceChanged.fire(ownPresence);
    }

    private void firePresenceReceived(final Presence presence) {
	for (final PresenceListener listener : listeners) {
	    listener.onPresenceReceived(presence);
	}
    };

    /**
     * Upon connecting to the server and becoming an active resource, a client
     * SHOULD request the roster before sending initial presence
     */
    private void install() {
	emite.subscribe(when(RosterManager.Events.ready), new PacketListener() {
	    public void handle(final IPacket received) {
		final Presence initialPresence = new Presence(userURI).With(Presence.Show.chat);
		broadcastPresence(initialPresence);
		if (delayedPresence != null) {
		    delayedPresence.setFrom(userURI);
		    broadcastPresence(delayedPresence);
		    delayedPresence = null;
		}
	    }
	});

	emite.subscribe(when("presence"), new PacketListener() {
	    public void handle(final IPacket received) {

		final Presence presence = new Presence(received);
		switch (presence.getType()) {

		case probe:
		    emite.send(ownPresence);
		    break;
		case error:
		    // FIXME: what should we do?
		    Log.warn("Error presence!!!");
		    break;
		default:
		    firePresenceReceived(presence);
		    break;
		}
	    }
	});

    }

}
