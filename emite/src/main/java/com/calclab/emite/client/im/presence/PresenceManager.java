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

import java.util.ArrayList;

import com.calclab.emite.client.components.Globals;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.bosh.EmiteComponent;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.im.roster.RosterManager;
import com.calclab.emite.client.xmpp.session.SessionManager;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.client.xmpp.stanzas.Presence.Type;

public class PresenceManager extends EmiteComponent {
    private Presence currentPresence;
    private final Globals globals;
    private final ArrayList<PresenceListener> listeners;
    private Presence presence;

    public PresenceManager(final Emite emite, final Globals globals) {
        super(emite);
        this.globals = globals;
        this.listeners = new ArrayList<PresenceListener>();
        this.currentPresence = null;
    }

    /**
     * subscribed -- The sender has allowed the recipient to receive their
     * presence.
     */
    public void acceptSubscription(final Presence presence) {
        replySubscription(presence, Presence.Type.subscribed);
    }

    public void addListener(final PresenceListener presenceListener) {
        this.listeners.add(presenceListener);
    }

    public IPacket answerTo(final Presence presence) {
        return new Presence(globals.getOwnURI()).To(presence.getFrom());
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
    public IPacket answerToSessionLogout() {
        final Presence presence = new Presence(Type.unavailable, globals.getOwnURI().toString(), globals.getDomain());
        currentPresence = presence;
        return presence;
    }

    /**
     * Upon connecting to the server and becoming an active resource, a client
     * SHOULD request the roster before sending initial presence
     */
    @Override
    public void attach() {
        when(RosterManager.Events.ready, new PacketListener() {
            public void handle(final IPacket received) {
                currentPresence = createInitialPresence();
                emite.send(currentPresence);
            }
        });

        when("presence", new PacketListener() {
            public void handle(final IPacket received) {
                onPresenceReceived(new Presence(received));
            }
        });

        when(SessionManager.Events.loggedOut, new PacketListener() {
            public void handle(final IPacket received) {
                emite.send(answerToSessionLogout());
            }
        });

    }

    /**
     * If a user would like to cancel a previously-granted subscription request,
     * it sends a presence stanza of type "unsubscribed".
     */
    public void cancelSubscriptor(final XmppURI to) {
        final Presence unsubscription = new Presence(Presence.Type.unsubscribed, globals.getOwnURI(), to);
        emite.send(unsubscription);
    }

    public Presence createInitialPresence() {
        return new Presence(globals.getOwnURI()).With(Presence.Show.chat);
    }

    /**
     * unsubscribed -- The subscription request has been denied or a
     * previously-granted subscription has been cancelled.
     */
    public void denySubscription(final Presence presence) {
        replySubscription(presence, Presence.Type.unsubscribed);
    }

    /**
     * If a user would like to unsubscribe from the presence of another entity,
     * it sends a presence stanza of type "unsubscribe".
     */
    public void requestUnsubscribe(final XmppURI to) {
        final Presence unsubscribeRequest = new Presence(Presence.Type.unsubscribe, globals.getOwnURI(), to);
        emite.send(unsubscribeRequest);
    }

    public void setBusyPresence(final String statusMessage) {
        presence = new Presence(globals.getOwnURI()).With(Presence.Show.dnd);
        if (statusMessage != null) {
            presence.setStatus(statusMessage);
        }
        emite.send(presence);
        currentPresence = presence;
    }

    // FIXME Dani (Presence): pienso que la librería debe tener está lógica de
    // mandar la presencia propia no el UI (unas veces sí y otras veces no).
    /**
     * 5.1.2. Presence Broadcast
     * 
     * After sending initial presence, the user MAY update its presence
     * information for broadcasting at any time during its session by sending a
     * presence stanza with no 'to' address and either no 'type' attribute or a
     * 'type' attribute with a value of "unavailable". (
     */
    public void setOwnPresence(final String statusMessage, final Show show) {
        final Show showValue = show != null ? show : Presence.Show.available;
        presence = new Presence(globals.getOwnURI()).With(showValue);
        if (statusMessage != null) {
            presence.setStatus(statusMessage);
        }
        emite.send(presence);
        currentPresence = presence;
    }

    void onPresenceReceived(final Presence presence) {
        final Type type = presence.getType();
        switch (type) {
        case subscribe:
            fireSubscriptionRequest(presence);
            break;
        case unsubscribed:
            fireUnsubscriptionReceived(presence);
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

    private void fireUnsubscriptionReceived(final Presence presence) {
        for (final PresenceListener listener : listeners) {
            listener.onUnsubscriptionReceived(presence);
        }
    }

    private void replySubscription(final Presence presence, final Presence.Type type) {
        if (presence.getType() == Presence.Type.subscribe) {
            final Presence response = new Presence(type, globals.getOwnURI(), presence.getFromURI());
            emite.send(response);
        } else {
            // throw exception: its a programming error
            throw new RuntimeException("Trying to accept/deny a non subscription request");
        }
    }
}
