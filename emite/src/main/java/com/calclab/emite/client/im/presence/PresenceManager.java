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

public class PresenceManager extends SessionComponent {
    private Presence delayedPresence;
    private Presence currentPresence;
    private final ArrayList<PresenceListener> listeners;

    public PresenceManager(final Emite emite) {
	super(emite);
	this.listeners = new ArrayList<PresenceListener>();
	this.currentPresence = null;
    }

    public void addListener(final PresenceListener presenceListener) {
	this.listeners.add(presenceListener);
    }

    /**
     * Returns the current's user presence
     * 
     * @return The current users presence
     */
    public Presence getCurrentPresence() {
	return currentPresence;
    }

    /**
     * Upon connecting to the server and becoming an active resource, a client
     * SHOULD request the roster before sending initial presence
     */
    @Override
    public void install() {
	super.install();

	emite.subscribe(when(RosterManager.Events.ready), new PacketListener() {
	    public void handle(final IPacket received) {
		currentPresence = new Presence(userURI).With(Presence.Show.chat);
		emite.send(currentPresence);
		if (delayedPresence != null)
		    sendDelayedPresence();
	    }
	});

	emite.subscribe(when("presence"), new PacketListener() {
	    public void handle(final IPacket received) {

		final Presence presence = new Presence(received);
		switch (presence.getType()) {

		case probe:
		    emite.send(currentPresence);
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
    public void loggedOut() {
	if (isLoggedIn()) {
	    final Presence presence = new Presence(Type.unavailable, userURI, userURI.getHostURI());
	    emite.send(presence);
	    delayedPresence = null;
	    currentPresence = null;
	}
	super.loggedOut();
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
	final Show showValue = show != null ? show : Presence.Show.chat;
	final Presence presence = new Presence().With(showValue);
	if (statusMessage != null) {
	    presence.setStatus(statusMessage);
	}

	if (isLoggedIn()) {
	    emite.send(presence);
	    currentPresence = presence;
	} else {
	    delayedPresence = presence;
	}
    };

    private void firePresenceReceived(final Presence presence) {
	for (final PresenceListener listener : listeners) {
	    listener.onPresenceReceived(presence);
	}
    }

    private void sendDelayedPresence() {
	delayedPresence.setFrom(userURI);
	emite.send(delayedPresence);
	currentPresence = delayedPresence;
	delayedPresence = null;
    }
}
