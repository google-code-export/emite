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
package com.calclab.emite.client.xep.muc;

import static com.calclab.emite.client.core.dispatcher.matcher.Matchers.when;

import java.util.HashMap;
import java.util.List;

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.NoPacket;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.core.packet.PacketFilter;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.chat.ChatManagerDefault;
import com.calclab.emite.client.im.chat.ChatManagerListener;
import com.calclab.emite.client.xmpp.stanzas.BasicStanza;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.Stanza;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.IQ.Type;

public class MUCRoomManager extends ChatManagerDefault implements RoomManager {
    private final HashMap<XmppURI, Room> rooms;

    public MUCRoomManager(final Emite emite) {
	super(emite);
	this.rooms = new HashMap<XmppURI, Room>();
	install();
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
    public <T> Room openChat(final XmppURI roomURI, final java.lang.Class<T> dataType, final T dataValue) {
	Room room = rooms.get(roomURI.getJID());
	if (room == null) {
	    room = new Room(userURI, roomURI.getJID(), "the name of the room", emite);
	    if (dataType != null) {
		room.setData(dataType, dataValue);
	    }
	    rooms.put(roomURI.getJID(), room);
	    chats.add(room);
	    final Presence presence = new Presence(null, userURI, roomURI);
	    presence.addChild("x", "http://jabber.org/protocol/muc");
	    emite.send(presence);
	    fireChatCreated(room);
	}
	return room;
    }

    @Override
    protected void eventMessage(final Message message) {
	IPacket child;
	if (message.getType() == Message.Type.groupchat) {
	    final Room room = rooms.get(message.getFromURI().getJID());
	    if (room != null) {
		room.receive(message);
	    }
	} else if ((child = message.getFirstChild("x").getFirstChild("invite")) != NoPacket.INSTANCE) {
	    handleRoomInvitation(message.getFromURI(), new BasicStanza(child));
	}

    }

    private void createInstantRoom(final Room room) {

	final IQ iq = new IQ(Type.set, userURI, room.getOtherURI());
	iq.addQuery("http://jabber.org/protocol/muc#owner").addChild("x", "jabber:x:data").With("type", "submit");
	emite.sendIQ("rooms", iq, new PacketListener() {
	    public void handle(final IPacket received) {
		if (IQ.isSuccess(received)) {

		}
	    }
	});
    }

    /**
     * @see http://www.xmpp.org/extensions/xep-0045.html#createroom
     */
    private void eventPresence(final Presence presence) {
	final XmppURI occupantURI = presence.getFromURI();
	final Room room = rooms.get(occupantURI.getJID());
	if (room != null) {
	    if (presence.hasAttribute("type", "unavailable")) {
		room.removeOccupant(occupantURI);
	    } else {
		final List<? extends IPacket> children = presence.getChildren(new PacketFilter() {
		    public boolean isValid(final IPacket packet) {
			return packet.getName().equals("x")
				&& packet.hasAttribute("xmlns", "http://jabber.org/protocol/muc#user");
		    }
		});
		for (final IPacket child : children) {
		    final IPacket item = child.getFirstChild("item");
		    final String affiliation = item.getAttribute("affiliation");
		    final String role = item.getAttribute("role");
		    room.setOccupantPresence(occupantURI, affiliation, role);
		    if (isNewRoom(child)) {
			createInstantRoom(room);
		    }
		}
	    }
	}
    }

    private void fireInvitationReceived(final XmppURI invitor, final XmppURI roomURI, final String reason) {
	for (final ChatManagerListener listener : listeners) {
	    try {
		((RoomManagerListener) listener).onInvitationReceived(invitor, roomURI, reason);
	    } catch (final ClassCastException e) {
	    }
	}
    }

    private void handleRoomInvitation(final XmppURI roomURI, final Stanza invitation) {
	fireInvitationReceived(invitation.getFromURI(), roomURI, invitation.getFirstChild("reason").getText());
    }

    private void install() {
	emite.subscribe(when("presence"), new PacketListener() {
	    public void handle(final IPacket received) {
		eventPresence(new Presence(received));
	    }
	});
    }

    private boolean isNewRoom(final IPacket xtension) {
	final String code = xtension.getFirstChild("status").getAttribute("code");
	return code != null && code.equals("201");
    }

}
