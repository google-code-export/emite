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

import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.packet.BasicPacket;
import com.calclab.emite.client.core.packet.Packet;

public class Roster {
    public static final SubscriptionMode DEF_SUBSCRIPTION_MODE = SubscriptionMode.manual;

    public static enum SubscriptionMode {
        auto_accept_all, auto_reject_all, manual
    }

    private final ArrayList<RosterItem> items;
    private final ArrayList<RosterListener> listeners;
    private final Dispatcher dispatcher;
    private SubscriptionMode subscriptionMode;

    public Roster(final Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
        listeners = new ArrayList<RosterListener>();
        items = new ArrayList<RosterItem>();
        subscriptionMode = DEF_SUBSCRIPTION_MODE;
    }

    public void setSubscriptionMode(final SubscriptionMode subscriptionMode) {
        this.subscriptionMode = subscriptionMode;
    }

    public SubscriptionMode getSubscriptionMode() {
        return subscriptionMode;
    }

    public void addListener(final RosterListener listener) {
        listeners.add(listener);
    }

    public void clear() {
        items.clear();
    }

    public RosterItem getItem(final int index) {
        return items.get(index);
    }

    public int getSize() {
        return items.size();
    }

    public void requestAddItem(final String uri, final String name, final String group) {
        final Packet item = new BasicPacket("item").With("jid", uri).With("name", name);
        if (group != null) {
            item.addChild(new BasicPacket("group").WithText(group));
        }
        dispatcher.publish(RosterManager.Events.addItem.With(item));
    }

    void add(final RosterItem item) {
        items.add(item);
    }

    void fireRosterInitialized() {
        for (final RosterListener listener : listeners) {
            listener.onRosterInitialized(items);
        }
    }

}
