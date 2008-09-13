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

package com.calclab.emite.im.client.xold_roster;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.signal.Signal;
import com.calclab.suco.client.signal.Slot;

public class XRoster {

    // Only JIDs
    private final HashMap<XmppURI, XRosterItem> items;

    private final Signal<XRosterItem> onItemChanged;
    private final Signal<Collection<XRosterItem>> onRosterChanged;

    public XRoster() {
	items = new HashMap<XmppURI, XRosterItem>();
	this.onItemChanged = new Signal<XRosterItem>("roster:onItemChanged");
	this.onRosterChanged = new Signal<Collection<XRosterItem>>("roster:onRosterChanged");
    }

    public void changePresence(final XmppURI uri, final Presence presence) {
	final XRosterItem item = findItemByJID(uri);
	if (item != null) {
	    item.setPresence(presence);
	    onItemChanged.fire(item);
	}
    }

    public void changeSubscription(final XmppURI jid, final String subscription) {
	final XRosterItem item = findItemByJID(jid);
	if (item != null) {
	    item.setSubscription(subscription);
	    onItemChanged.fire(item);
	}
    }

    public void clear() {
	items.clear();
    }

    public XRosterItem findItemByJID(final XmppURI jid) {
	return items.get(jid.getJID());
    }

    public Collection<XRosterItem> getItems() {
	return items.values();
    }

    public void onItemChanged(final Slot<XRosterItem> listener) {
	onItemChanged.add(listener);
    }

    public void onRosterChanged(final Slot<Collection<XRosterItem>> listener) {
	onRosterChanged.add(listener);
    }

    public void removeItem(final XmppURI jid) {
	final XRosterItem removed = items.remove(jid.getJID());
	if (removed != null) {
	    onRosterChanged.fire(getItems());
	}
    }

    void add(final XRosterItem item) {
	items.put(item.getJID(), item);
	onRosterChanged.fire(getItems());
    }

    void setItems(final List<XRosterItem> itemCollection) {
	items.clear();
	for (final XRosterItem item : itemCollection) {
	    items.put(item.getJID(), item);
	}
	onRosterChanged.fire(getItems());
    }

}
