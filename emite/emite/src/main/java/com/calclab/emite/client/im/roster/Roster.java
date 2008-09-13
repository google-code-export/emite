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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.signal.Signal;
import com.calclab.suco.client.signal.Slot;

public class Roster {

    // Only JIDs
    private final HashMap<XmppURI, RosterItem> items;

    private final Signal<RosterItem> onItemChanged;
    private final Signal<Collection<RosterItem>> onRosterChanged;

    public Roster() {
	items = new HashMap<XmppURI, RosterItem>();
	this.onItemChanged = new Signal<RosterItem>("roster:onItemChanged");
	this.onRosterChanged = new Signal<Collection<RosterItem>>("roster:onRosterChanged");
    }

    public void changePresence(final XmppURI uri, final Presence presence) {
	final RosterItem item = findItemByJID(uri);
	if (item != null) {
	    item.setPresence(presence);
	    onItemChanged.fire(item);
	}
    }

    public void changeSubscription(final XmppURI jid, final String subscription) {
	final RosterItem item = findItemByJID(jid);
	if (item != null) {
	    item.setSubscription(subscription);
	    onItemChanged.fire(item);
	}
    }

    public void clear() {
	items.clear();
    }

    public RosterItem findItemByJID(final XmppURI jid) {
	return items.get(jid.getJID());
    }

    public Collection<RosterItem> getItems() {
	return items.values();
    }

    public void onItemChanged(final Slot<RosterItem> listener) {
	onItemChanged.add(listener);
    }

    public void onRosterChanged(final Slot<Collection<RosterItem>> listener) {
	onRosterChanged.add(listener);
    }

    public void removeItem(final XmppURI jid) {
	final RosterItem removed = items.remove(jid.getJID());
	if (removed != null) {
	    onRosterChanged.fire(getItems());
	}
    }

    void add(final RosterItem item) {
	items.put(item.getJID(), item);
	onRosterChanged.fire(getItems());
    }

    void setItems(final List<RosterItem> itemCollection) {
	items.clear();
	for (final RosterItem item : itemCollection) {
	    items.put(item.getJID(), item);
	}
	onRosterChanged.fire(getItems());
    }

}
