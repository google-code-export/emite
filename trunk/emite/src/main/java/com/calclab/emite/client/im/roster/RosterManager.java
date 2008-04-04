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
package com.calclab.emite.client.im.roster;

import java.util.List;

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.bosh.EmiteComponent;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.Event;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public class RosterManager extends EmiteComponent {

    public static class Events {
	public static final Event ready = new Event("roster:on:ready");
	public static final Event addItem = new Event("roster:do:addItem");
    }

    private final Roster roster;
    private int id;

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
	when(Session.Events.loggedIn, new PacketListener() {
	    public void handle(final Packet received) {
		emite.send(new IQ("roster", IQ.Type.get).WithQuery("jabber:iq:roster", null));
	    }
	});

	when(new IQ("roster", IQ.Type.result, null), new PacketListener() {
	    public void handle(final Packet received) {
		setRosterItems(roster, received);
		emite.publish(RosterManager.Events.ready);
	    }
	});

	when(RosterManager.Events.addItem, new PacketListener() {
	    public void handle(final Packet received) {
		final Packet iq = new IQ(nextID(), IQ.Type.set, null).WithQuery("jabber:iq:roster", received
			.getFirstChild("item"));
		emite.send(iq);
	    }
	});
    }

    private RosterItem convert(final Packet item) {
	final String jid = item.getAttribute("jid");
	final XmppURI uri = XmppURI.parse(jid);
	return new RosterItem(uri, item.getAttribute("subscription"), item.getAttribute("name"));
    }

    private List<? extends Packet> getItems(final Packet packet) {
	final List<? extends Packet> items = packet.getFirstChild("query").getChildren();
	return items;
    }

    private String nextID() {
	return "roster_" + id++;
    }

    private void setRosterItems(final Roster roster, final Packet received) {
	roster.clear();
	for (final Packet item : getItems(received)) {
	    roster.add(convert(item));
	}
	roster.fireRosterInitialized();
    }
}
