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
import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;

import java.util.ArrayList;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.components.Installable;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.im.roster.RosterManager;
import com.calclab.emite.client.xmpp.session.SessionManager;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.client.xmpp.stanzas.Presence.Type;

public class PresenceManager implements Installable {
    private Presence delayedPresence;
    private Presence currentPresence;
    private final ArrayList<PresenceListener> listeners;
    private XmppURI userURI;
    private final Emite emite;

    public PresenceManager(final Emite emite) {
	this.emite = emite;
	this.listeners = new ArrayList<PresenceListener>();
	this.currentPresence = null;
	this.userURI = null;
    }

    public void addListener(final PresenceListener presenceListener) {
	this.listeners.add(presenceListener);
    }

    public IPacket answerTo(final Presence presence) {
	return new Presence(userURI).To(presence.getFrom());
    }

    public Presence getCurrentPresence() {
	return currentPresence;
    }

    public XmppURI getUserURI() {
	return userURI;
    }

    /**
     * Upon connecting to the server and becoming an active resource, a client
     * SHOULD request the roster before sending initial presence
     */
    public void install() {
	emite.subscribe(when(RosterManager.Events.ready), new PacketListener() {
	    public void handle(final IPacket received) {
		eventRosterReady();
	    }
	});

	emite.subscribe(when("presence"), new PacketListener() {
	    public void handle(final IPacket received) {
		eventPresence(new Presence(received));
	    }
	});
	emite.subscribe(when(SessionManager.Events.onLoggedOut), new PacketListener() {
	    public void handle(final IPacket received) {
		eventLoggedOut();
	    }
	});
	emite.subscribe(when(SessionManager.Events.onLoggedIn), new PacketListener() {
	    public void handle(final IPacket received) {
		eventLoggedIn(received);
	    }

	});

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
	    setOwnPresence(presence);
	} else {
	    delayedPresence = presence;
	}
    }

    void eventLoggedIn(final IPacket received) {
	userURI = uri(received.getAttribute("uri"));
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
    void eventLoggedOut() {
	if (isLoggedIn()) {
	    final Presence presence = new Presence(Type.unavailable, userURI, userURI.getHostURI());
	    emite.send(presence);
	    delayedPresence = null;
	    currentPresence = null;
	}
	userURI = null;
    }

    void eventPresence(final Presence presence) {
	if (delayedPresence != null) {
	    sendDelayedPresence();
	}

	final Type type = presence.getType();
	switch (type) {

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

    void eventRosterReady() {
	currentPresence = createInitialPresence();
	emite.send(currentPresence);
    }

    private Presence createInitialPresence() {
	return new Presence(userURI).With(Presence.Show.chat);
    }

    private void firePresenceReceived(final Presence presence) {
	for (final PresenceListener listener : listeners) {
	    listener.onPresenceReceived(presence);
	}
    }

    private boolean isLoggedIn() {
	return currentPresence != null;
    }

    private void sendDelayedPresence() {
	delayedPresence.setFrom(userURI);
	delayedPresence.setTo(userURI.getHostURI());
	setOwnPresence(delayedPresence);
	delayedPresence = null;
    }

    private void setOwnPresence(final Presence presence) {
	emite.send(presence);
	currentPresence = presence;
    }
}
