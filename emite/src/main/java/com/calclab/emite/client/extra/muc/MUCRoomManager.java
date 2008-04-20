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

import static com.calclab.emite.client.core.dispatcher.matcher.Matchers.when;

import java.util.HashMap;
import java.util.List;

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.chat.ChatManagerDefault;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.IQ.Type;

public class MUCRoomManager extends ChatManagerDefault implements RoomManager {
    private final HashMap<XmppURI, Room> rooms;

    public MUCRoomManager(final Emite emite) {
	super(emite);
	this.rooms = new HashMap<XmppURI, Room>();
    }

    @Override
    public void close(final Chat whatToClose) {
	final Room room = rooms.remove(whatToClose.getOtherURI().getJID());
	if (room != null) {
	    room.close();
	    fireChatClosed(room);
	}
    }

    @Override
    public void eventLoggedOut() {
	closeAll(rooms.values());
    }

    @Override
    public void install() {
	super.install();
	emite.subscribe(when("presence"), new PacketListener() {
	    public void handle(final IPacket received) {
		eventPresence(new Presence(received));
	    }
	});
    }

    @Override
    public Room openChat(final XmppURI roomURI) {
	Room room = rooms.get(roomURI.getJID());
	if (room == null) {
	    room = new Room(userURI, roomURI.getJID(), "the name of the room", emite);
	    rooms.put(roomURI.getJID(), room);
	    final Presence presence = new Presence(null, userURI, roomURI);
	    presence.addChild(new Packet("x", "http://jabber.org/protocol/muc"));
	    emite.send(presence);
	    fireChatCreated(room);
	}
	return room;
    }

    @Override
    protected void eventMessage(final Message message) {
	if (message.getType() == Message.Type.groupchat) {
	    final Room room = rooms.get(message.getFromURI().getJID());
	    if (room != null) {
		room.dispatch(message);
	    }
	}
    }

    protected void sendMUCSupportQuery() {
	final IQ iq = new IQ(IQ.Type.get, userURI, userURI.getHost());
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
	final IQ iq = new IQ(IQ.Type.get, userURI, userURI.getHost());
	iq.setQuery("http://jabber.org/protocol/disco#items");
	emite.send("rooms", iq, new PacketListener() {
	    public void handle(final IPacket received) {
		onRoomsQuery(received);
	    }
	});
    }

    /**
     * @see http://www.xmpp.org/extensions/xep-0045.html#createroom
     */
    void eventPresence(final Presence presence) {
	final XmppURI occupantURI = presence.getFromURI();
	final Room room = rooms.get(occupantURI.getJID());
	if (room != null) {
	    if (presence.hasAttribute("type", "unavailable")) {
		room.removeOccupant(occupantURI);
	    } else {
		final IPacket xtension = presence.getFirstChild("x");
		if (xtension.hasAttribute("xmlns", "http://jabber.org/protocol/muc#user")) {
		    final IPacket item = xtension.getFirstChild("item");
		    final String affiliation = item.getAttribute("affiliation");
		    final String role = item.getAttribute("role");

		    room.setOccupantPresence(occupantURI, affiliation, role);

		    if (isNewRoom(xtension)) {
			createInstantRoom(room);
		    }
		}
	    }
	}
    }

    private void createInstantRoom(final Room room) {
	final IQ iq = new IQ(Type.set, userURI, room.getOtherURI().toString()).WithQuery(
		"http://jabber.org/protocol/muc#owner", new Packet("x", "jabber:x:data").With("type", "submit"));
	emite.send("rooms", iq, new PacketListener() {
	    public void handle(final IPacket received) {
		if (!received.hasAttribute("type", "result")) {
		    // TODO: ISSUE error!
		}
	    }
	});
    }

    private boolean isNewRoom(final IPacket xtension) {
	final String code = xtension.getFirstChild("status").getAttribute("code");
	return (code != null && code.equals("201"));
    }

    // FIXME: (dani de dani) no entiendo bien...
    private void onRoomsQuery(final IPacket received) {
	final List<? extends IPacket> items = received.getFirstChild("query").getChildren();
	rooms.clear();
	for (final IPacket packet : items) {
	    final XmppURI uri = XmppURI.parse(packet.getAttribute("jid"));
	    rooms.put(uri, new Room(userURI, uri, packet.getAttribute("name"), emite));
	}
    }
}
