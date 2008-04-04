/**
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

import java.util.ArrayList;

import com.calclab.emite.client.components.Globals;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.bosh.EmiteComponent;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.im.roster.RosterManager;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.Presence.Type;

public class PresenceManager extends EmiteComponent {
    private Presence currentPresence;
    private final Globals globals;
    private final ArrayList<PresenceListener> listeners;

    public PresenceManager(final Emite emite, final Globals globals) {
	super(emite);
	this.globals = globals;
	this.listeners = new ArrayList<PresenceListener>();
	this.currentPresence = null;
    }

    public void acceptSubscription(final Presence presence) {
	if (presence.getType() == Presence.Type.subscribe) {
	    final Presence response = new Presence(Presence.Type.subscribed, globals.getOwnURI(), presence.getFromURI());
	    emite.send(response);
	} else {
	    // throw exception: its a programming error
	    throw new RuntimeException("Tryng to accept a non subscription request");
	}
    }

    public void addListener(final PresenceListener presenceListener) {
	this.listeners.add(presenceListener);
    }

    public Packet answerTo(final Presence presence) {
	return new Presence(globals.getOwnURI()).To(presence.getFrom());
    }

    public Packet answerToSessionLogout() {
	return new Presence(globals.getOwnURI()).With("type", "unavailable");
    }

    /**
     * Upon connecting to the server and becoming an active resource, a client
     * SHOULD request the roster before sending initial presence
     */
    @Override
    public void attach() {
	when(RosterManager.Events.ready, new PacketListener() {
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

	when(Session.Events.loggedOut, new PacketListener() {
	    public void handle(final Packet received) {
		emite.send(answerToSessionLogout());
	    }
	});

    }

    protected void onPresenceReceived(final Presence presence) {
	final Type type = presence.getType();
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
	return new Presence(globals.getOwnURI()).With(Presence.Show.chat);
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
