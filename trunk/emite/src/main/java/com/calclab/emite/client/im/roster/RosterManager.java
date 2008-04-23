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

import static com.calclab.emite.client.core.dispatcher.matcher.Matchers.when;

import java.util.ArrayList;
import java.util.List;

import com.calclab.emite.client.components.Installable;
import com.calclab.emite.client.core.bosh.Emite;
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

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.*;

public class RosterManager implements Installable {

    public static class Events {
	public static final Event ready = new Event("roster:on:ready");
    }

    private final Roster roster;
    private final Emite emite;
    private XmppURI currentUser;

    public RosterManager(final Emite emite, final Roster roster) {
	this.emite = emite;
	this.roster = roster;
	this.currentUser = null;
    }

    public void install() {
	// Upon connecting to the server and becoming an active resource, a
	// client SHOULD request the roster BEFORE! sending initial presence
	emite.subscribe(when(SessionManager.Events.onLoggedIn), new PacketListener() {
	    public void handle(final IPacket received) {
		currentUser = uri(received.getAttribute("uri"));
		requestRoster();
	    }
	});

	emite.subscribe(when(new IQ(IQ.Type.set).WithQuery("jabber:iq:roster", null).With("xmlns", null)),
		new PacketListener() {
		    public void handle(final IPacket received) {
			emite.send(new IQ(IQ.Type.result).With("id", received.getAttribute("id")));
			final IPacket item = received.getFirstChild("query").getFirstChild("item");
			final String jid = item.getAttribute("jid");
			roster.changeSubscription(uri(jid), item.getAttribute("subscription"));
		    }
		});

	emite.subscribe(when("presence"), new PacketListener() {
	    public void handle(final IPacket received) {
		final Presence presence = new Presence(received);
		roster.changePresence(presence.getFromURI(), presence);
	    }
	});

    }

    /**
     * 7.4. Adding a Roster Item
     * 
     * At any time, a user MAY add an item to his or her roster.
     * 
     * @param JID
     * @param name
     * @param group
     * @see http://www.xmpp.org/rfcs/rfc3921.html#roster
     */
    public void requestAddItem(final XmppURI jid, final String name, final String group) {
	final IPacket item = new Packet("item").With("jid", jid.toString()).With("name", name);
	if (group != null) {
	    item.addChild(new Packet("group").WithText(group));
	}

	roster.add(new RosterItem(jid, Subscription.none, name));
	final IPacket iq = new IQ(IQ.Type.set, currentUser, null).WithQuery("jabber:iq:roster", item);
	emite.send("roster", iq, new PacketListener() {
	    public void handle(final IPacket received) {
		if (IQ.isSuccess(received)) {
		    final Presence presenceRequest = new Presence(Type.subscribe, null, jid);
		    emite.send(presenceRequest);
		} else {
		    roster.removeItem(jid);
		}
	    }
	});
    }

    public void requestRemoveItem(final XmppURI jid) {
	final IQ iq = new IQ(IQ.Type.set).WithQuery("jabber:iq:roster", new Packet("item").With("jid", jid.toString())
		.With("subscription", "remove"));
	emite.send("roster", iq, new PacketListener() {
	    public void handle(final IPacket received) {
		if (IQ.isSuccess(received)) {
		    roster.removeItem(jid);
		}
	    }
	});
    }

    private RosterItem convert(final IPacket item) {
	final String jid = item.getAttribute("jid");
	final XmppURI uri = uri(jid);
	final Subscription subscription = RosterItem.Subscription.valueOf(item.getAttribute("subscription"));
	return new RosterItem(uri, subscription, item.getAttribute("name"));
    }

    private List<? extends IPacket> getItems(final IPacket iPacket) {
	final List<? extends IPacket> items = iPacket.getFirstChild("query").getChildren();
	return items;
    }

    private void requestRoster() {
	emite.send("roster", new IQ(IQ.Type.get).WithQuery("jabber:iq:roster", null), new PacketListener() {
	    public void handle(final IPacket received) {
		setRosterItems(roster, received);
		emite.publish(RosterManager.Events.ready);
	    }

	});
    }

    private void setRosterItems(final Roster roster, final IPacket received) {
	final ArrayList<RosterItem> items = new ArrayList<RosterItem>();
	for (final IPacket item : getItems(received)) {
	    items.add(convert(item));
	}
	roster.setItems(items);
    }
}
