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
import com.calclab.emite.client.core.packet.ABasicPacket;
import com.calclab.emite.client.core.packet.Event;
import com.calclab.emite.client.core.packet.APacket;
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
	    public void handle(final APacket received) {
		emite.send(new IQ("roster", IQ.Type.get).WithQuery("jabber:iq:roster", null));
	    }
	});

	when("presence", new PacketListener() {
	    public void handle(final APacket received) {
		onPresenceReceived(new Presence(received));
	    }
	});

	when(new IQ("roster", IQ.Type.result, null), new PacketListener() {
	    public void handle(final APacket received) {
		setRosterItems(roster, received);
		emite.publish(RosterManager.Events.ready);
	    }
	});

    }

    public void requestAddItem(final String uri, final String name, final String group) {
	final APacket item = new ABasicPacket("item").With("jid", uri).With("name", name);
	if (group != null) {
	    item.addChild(new ABasicPacket("group").WithText(group));
	}
	final APacket iq = new IQ(nextID(), IQ.Type.set, null).WithQuery("jabber:iq:roster", item);
	emite.send(iq);
	final Presence presenceRequest = new Presence(Type.subscribe, null, XmppURI.parse(uri));
	emite.send(presenceRequest);
    }

    public void requestRemoveItem(final String JID) {
	final IQ iq = new IQ(nextID(), IQ.Type.set, null).WithQuery("jabber:iq:roster", new ABasicPacket("item").With(
		"jid", JID).With("subscription", "remove"));
	emite.send(iq);
    }

    protected void onPresenceReceived(final Presence presence) {

	final RosterItem item = roster.findItemByURI(presence.getFromURI());
	if (item != null) {
	    item.setPresence(presence);
	    roster.fireItemPresenceChanged(item);
	}

    }

    private RosterItem convert(final APacket item) {
	final String jid = item.getAttribute("jid");
	final XmppURI uri = XmppURI.parse(jid);
	final Subscription subscription = RosterItem.Subscription.valueOf(item.getAttribute("subscription"));
	return new RosterItem(uri, subscription, item.getAttribute("name"));
    }

    private List<? extends APacket> getItems(final APacket aPacket) {
	final List<? extends APacket> items = aPacket.getFirstChild("query").getChildren();
	return items;
    }

    private String nextID() {
	return "roster_" + id++;
    }

    private void setRosterItems(final Roster roster, final APacket received) {
	roster.clear();
	for (final APacket item : getItems(received)) {
	    roster.add(convert(item));
	}
	roster.fireRosterInitialized();
    }
}
