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
package com.calclab.emite.client.extra.muc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.calclab.emite.client.components.Globals;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.bosh.EmiteComponent;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.xmpp.session.SessionManager;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public class MUCRoomManager extends EmiteComponent implements RoomManager {
    private final Globals globals;
    private final ArrayList<RoomManagerListener> listeners;
    private final HashMap<XmppURI, Room> rooms;

    public MUCRoomManager(final Emite emite, final Globals globals) {
	super(emite);
	this.globals = globals;
	this.listeners = new ArrayList<RoomManagerListener>();
	this.rooms = new HashMap<XmppURI, Room>();
    }

    public void addListener(final RoomManagerListener listener) {
	listeners.add(listener);
    }

    @Override
    public void attach() {
	when(SessionManager.Events.loggedIn, new PacketListener() {
	    public void handle(final IPacket received) {
		sendMUCSupportQuery();
	    }
	});
	when("message", new PacketListener() {
	    public void handle(final IPacket received) {
		onMessageReceived(new Message(received));
	    }
	});
	when("presence", new PacketListener() {
	    public void handle(final IPacket received) {
		onPresenceReceived(new Presence(received));
	    }
	});
    }

    public Room enterRoom(final String jid, final String alias) {
	final XmppURI ownURI = globals.getOwnURI();
	final XmppURI roomURI = XmppURI.parse(jid + "/" + alias);
	final Room room = new Room(ownURI, roomURI, "fix me!", emite);
	rooms.put(roomURI.getJID(), room);
	final Presence presence = new Presence(null, ownURI.toString(), jid + "/" + alias);
	presence.addChild(new Packet("x", "http://jabber.org/protocol/muc"));
	emite.send(presence);
	fireRoomCreated(room);
	return room;
    }

    protected void sendMUCSupportQuery() {
	final IQ iq = new IQ(IQ.Type.get).From(globals.getOwnURI()).To(globals.getDomain());
	iq.setQuery("http://jabber.org/protocol/disco#info");
	emite.send("disco", iq, new PacketListener() {
	    public void handle(final IPacket received) {
		System.out.println("MUC!!: " + received);
		// sendRoomsQuery();
	    }
	});
    }

    /**
     * @see http://www.xmpp.org/extensions/xep-0045.html#disco-rooms
     */
    protected void sendRoomsQuery() {
	final IQ iq = new IQ(IQ.Type.get).From(globals.getOwnURI()).To(globals.getDomain());
	iq.setQuery("http://jabber.org/protocol/disco#items");
	emite.send("rooms", iq, new PacketListener() {
	    public void handle(final IPacket received) {
		onRoomsQuery(received);
	    }
	});
    }

    void onMessageReceived(final Message message) {
	if (message.getType() == Message.Type.groupchat) {
	    final Room room = rooms.get(message.getFromURI());
	    if (room != null) {
		room.dispatch(message);
	    }
	}
    }

    void onPresenceReceived(final Presence presence) {
	final Room room = rooms.get(presence.getFromURI().getJID());
	if (room != null) {
	    final IPacket xtension = presence.getFirstChild("x");
	    if (xtension != null && xtension.hasAttribute("xmlns", "http://jabber.org/protocol/muc#user")) {
		final IPacket item = xtension.getFirstChild("item");
		room.addUser(new RoomUser(presence.getFromURI(), item.getAttribute("affiliation"), item
			.getAttribute("role")));
	    }
	}
    }

    private void fireRoomCreated(final Room room) {
	for (final RoomManagerListener listener : listeners) {
	    listener.onRoomCreated(room);
	}
    }

    // FIXME: (dani de dani) no entiendo bien...
    private void onRoomsQuery(final IPacket received) {
	final List<? extends IPacket> items = received.getFirstChild("query").getChildren();
	rooms.clear();
	final XmppURI ownURI = globals.getOwnURI();
	for (final IPacket packet : items) {
	    final XmppURI uri = XmppURI.parse(packet.getAttribute("jid"));
	    rooms.put(uri, new Room(ownURI, uri, packet.getAttribute("name"), emite));
	}
    }
}
