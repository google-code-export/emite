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

import java.util.List;

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.bosh.EmiteComponent;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.Event;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.im.roster.RosterItem.Subscription;
import com.calclab.emite.client.xmpp.session.SessionManager;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.Presence.Type;

public class RosterManager extends EmiteComponent {

    public static class Events {
	public static final Event ready = new Event("roster:on:ready");
    }

    private int id;
    private final Roster roster;

    public RosterManager(final Emite emite, final Roster roster) {
	super(emite);
	this.roster = roster;
	id = 1;
    }

    /**
     * Upon connecting to the server and becoming an active resource, a client
     * SHOULD request the roster BEFORE! sending initial presence
     */
    @Override
    public void attach() {
	when(SessionManager.Events.loggedIn, new PacketListener() {
	    public void handle(final IPacket received) {
		emite.send("roster", new IQ(IQ.Type.get).WithQuery("jabber:iq:roster", null), new PacketListener() {
		    public void handle(final IPacket received) {
			setRosterItems(roster, received);
			emite.publish(RosterManager.Events.ready);
		    }
		});
	    }
	});

	when("presence", new PacketListener() {
	    public void handle(final IPacket received) {
		onPresenceReceived(new Presence(received));
	    }
	});

    }

    public void requestAddItem(final String JID, final String name, final String group) {
	final IPacket item = new Packet("item").With("jid", JID).With("name", name);
	if (group != null) {
	    item.addChild(new Packet("group").WithText(group));
	}

	final IPacket iq = new IQ(nextID(), IQ.Type.set, null).WithQuery("jabber:iq:roster", item);
	emite.send("roster", iq, new PacketListener() {
	    public void handle(final IPacket received) {
		final Presence presenceRequest = new Presence(Type.subscribe, null, XmppURI.parse(JID));
		emite.send(presenceRequest);
	    }
	});
	roster.add(new RosterItem(XmppURI.parse(JID), RosterItem.Subscription.from, name));
    }

    public void requestRemoveItem(final String JID) {
	final IQ iq = new IQ(nextID(), IQ.Type.set, null).WithQuery("jabber:iq:roster", new Packet("item").With("jid",
		JID).With("subscription", "remove"));
	emite.send(iq);
    }

    protected void onPresenceReceived(final Presence presence) {

	final RosterItem item = roster.findItemByURI(presence.getFromURI());
	if (item != null) {
	    item.setPresence(presence);
	    roster.fireItemPresenceChanged(item);
	}

    }

    private RosterItem convert(final IPacket item) {
	final String jid = item.getAttribute("jid");
	final XmppURI uri = XmppURI.parse(jid);
	final Subscription subscription = RosterItem.Subscription.valueOf(item.getAttribute("subscription"));
	return new RosterItem(uri, subscription, item.getAttribute("name"));
    }

    private List<? extends IPacket> getItems(final IPacket iPacket) {
	final List<? extends IPacket> items = iPacket.getFirstChild("query").getChildren();
	return items;
    }

    private String nextID() {
	return "roster_" + id++;
    }

    private void setRosterItems(final Roster roster, final IPacket received) {
	roster.clear();
	for (final IPacket item : getItems(received)) {
	    roster.add(convert(item));
	}
	roster.fireRosterInitialized();
    }
}
