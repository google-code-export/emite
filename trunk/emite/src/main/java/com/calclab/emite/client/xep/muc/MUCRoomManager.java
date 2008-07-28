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

import java.util.HashMap;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.core.packet.Filters;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.NoPacket;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.chat.ChatManagerDefault;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.stanzas.BasicStanza;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.Stanza;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.IQ.Type;
import com.calclab.suco.client.signal.Signal;
import com.calclab.suco.client.signal.Slot;

public class MUCRoomManager extends ChatManagerDefault implements RoomManager {
    private final HashMap<XmppURI, Room> rooms;
    private final Signal<RoomInvitation> onInvitationReceived;

    public MUCRoomManager(final Session session) {
	super(session);
	this.onInvitationReceived = new Signal<RoomInvitation>("roomManager:onInvitationReceived");
	this.rooms = new HashMap<XmppURI, Room>();

	// @see http://www.xmpp.org/extensions/xep-0045.html#createroom
	session.onPresence(new Slot<Presence>() {
	    public void onEvent(final Presence presence) {
		final XmppURI occupantURI = presence.getFrom();
		final Room room = rooms.get(occupantURI.getJID());
		if (room != null) {
		    Log.debug("changing room presence!");
		    changePresence(room, occupantURI, presence);
		}
	    }
	});
    }

    @Override
    public void close(final Chat whatToClose) {
	final Room room = rooms.remove(whatToClose.getOtherURI().getJID());
	if (room != null) {
	    room.close();
	    super.close(room);
	}
    }

    public void onInvitationReceived(final Slot<RoomInvitation> listener) {
	onInvitationReceived.add(listener);
    }

    @Override
    public <T> Room openChat(final XmppURI roomURI, final java.lang.Class<T> dataType, final T dataValue) {
	Room room = rooms.get(roomURI.getJID());
	if (room == null) {
	    room = new Room(session, roomURI.getJID(), "the name of the room");
	    if (dataType != null) {
		room.setData(dataType, dataValue);
	    }
	    rooms.put(roomURI.getJID(), room);
	    chats.add(room);
	    final Presence presence = new Presence(null, session.getCurrentUser(), roomURI);
	    presence.addChild("x", "http://jabber.org/protocol/muc");
	    session.send(presence);
	    onChatCreated.fire(room);
	} else {
	    room.setData(dataType, dataValue);
	}
	return room;
    }

    @Override
    protected void eventMessage(final Message message) {
	IPacket child;
	if (message.getType() == Message.Type.groupchat) {
	    final Room room = rooms.get(message.getFrom().getJID());
	    if (room != null) {
		room.receive(message);
	    }
	} else if ((child = message.getFirstChild("x").getFirstChild("invite")) != NoPacket.INSTANCE) {
	    handleRoomInvitation(message.getFrom(), new BasicStanza(child));
	}

    }

    private void changePresence(final Room room, final XmppURI occupantURI, final Presence presence) {
	if (presence.hasAttribute("type", "unavailable")) {
	    room.removeOccupant(occupantURI);
	} else {
	    final List<? extends IPacket> children = presence.getChildren(Filters.byNameAndXMLNS("x",
		    "http://jabber.org/protocol/muc#user"));
	    for (final IPacket child : children) {
		final IPacket item = child.getFirstChild("item");
		final String affiliation = item.getAttribute("affiliation");
		final String role = item.getAttribute("role");
		room.setOccupantPresence(occupantURI, affiliation, role);
		if (isNewRoom(child)) {
		    requestCreateInstantRoom(room);
		} else {
		    room.setStatus(Chat.Status.ready);
		}
	    }
	}
    }

    private void fireInvitationReceived(final XmppURI invitor, final XmppURI roomURI, final String reason) {
	onInvitationReceived.fire(new RoomInvitation(invitor, roomURI, reason));
    }

    private void handleRoomInvitation(final XmppURI roomURI, final Stanza invitation) {
	fireInvitationReceived(invitation.getFrom(), roomURI, invitation.getFirstChild("reason").getText());
    }

    private boolean isNewRoom(final IPacket xtension) {
	final String code = xtension.getFirstChild("status").getAttribute("code");
	return code != null && code.equals("201");
    }

    private void requestCreateInstantRoom(final Room room) {

	final IQ iq = new IQ(Type.set, session.getCurrentUser(), room.getOtherURI());
	iq.addQuery("http://jabber.org/protocol/muc#owner").addChild("x", "jabber:x:data").With("type", "submit");
	session.sendIQ("rooms", iq, new Slot<IPacket>() {
	    public void onEvent(final IPacket received) {
		if (IQ.isSuccess(received)) {
		    room.setStatus(Chat.Status.ready);
		}
	    }
	});
    }

}
