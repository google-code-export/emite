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
package com.calclab.emite.client.im.roster;

import java.util.ArrayList;
import java.util.List;

import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.Presence.Type;

public class RosterItem {

    public static enum Subscription {
        /**
         * "both" -- both the user and the contact have subscriptions to each
         * other's presence information
         */
        both,
        /**
         * "from" -- the contact has a subscription to the user's presence
         * information, but the user does not have a subscription to the
         * contact's presence information
         */
        from,
        /**
         * "none" -- the user does not have a subscription to the contact's
         * presence information, and the contact does not have a subscription to
         * the user's presence information
         */
        none,
        /**
         * "to" -- the user has a subscription to the contact's presence
         * information, but the contact does not have a subscription to the
         * user's presence information
         */
        to
    }

    private final ArrayList<String> groups;
    private final String name;
    private final Subscription subscription;
    private final XmppURI uri;
    private Presence presence;

    public RosterItem(final XmppURI uri, final Subscription subscription, final String name) {
        this.uri = uri;
        this.subscription = subscription;
        this.name = name;
        this.groups = new ArrayList<String>();
    }

    public List<String> getGroups() {
        return groups;
    }

    public String getName() {
        return name;
    }

    public Presence getPresence() {
        return presence;
    }

    public Type getPresenceType() {
        return presence != null ? presence.getType() : Type.unavailable;
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public XmppURI getXmppURI() {
        return uri;
    }

    public void setPresence(final Presence presence) {
        this.presence = presence;
    }

}
