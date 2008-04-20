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

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.chat.ChatListener;
import com.calclab.emite.client.xmpp.stanzas.BasicStanza;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.Presence.Type;

public class Room implements Chat {
    private final Emite emite;
    private final ArrayList<ChatListener> listeners;
    private final String name;
    private final HashMap<XmppURI, Occupant> occupants;
    private final XmppURI roomURI;
    private final XmppURI userURI;

    public Room(final XmppURI userURI, final XmppURI roomURI, final String name, final Emite emite) {
	this.userURI = userURI;
	this.roomURI = roomURI;
	this.name = name;
	this.emite = emite;
	this.occupants = new HashMap<XmppURI, Occupant>();
	this.listeners = new ArrayList<ChatListener>();
    }

    /**
     * RoomListener are welcomed!
     */
    public void addListener(final ChatListener listener) {
	listeners.add(listener);
    }

    /**
     * In order to exit a multi-user chat room, an occupant sends a presence
     * stanza of type "unavailable" to the <room@service/nick> it is currently
     * using in the room.
     * 
     * @see http://www.xmpp.org/extensions/xep-0045.html#exit
     */
    public void close() {
	emite.send(new Presence(Type.unavailable, userURI, roomURI));
    }

    public Occupant findOccupant(final XmppURI uri) {
	return occupants.get(uri);
    }

    public String getID() {
	return roomURI.toString();
    }

    public String getName() {
	return name;
    }

    public Object getOccupantsCount() {
	return occupants.size();
    }

    public XmppURI getOtherURI() {
	return roomURI;
    }

    public String getThread() {
	return roomURI.getNode();
    }

    /**
     * 
     * http://www.xmpp.org/extensions/xep-0045.html#invite
     * 
     * @param userJid
     *                user to invite
     * @param reasonText
     *                reason for the invitation
     */
    // FIXME: Dani, check if this is correct (the way to construct the message)
    public void inviteUser(final String userJid, final String reasonText) {
	final BasicStanza message = new BasicStanza("message", null);
	message.setFrom(userURI.toString());
	message.setTo(roomURI.toString());
	final IPacket x = message.add("x", "http://jabber.org/protocol/muc#user");
	final IPacket invite = x.add("invite", null);
	invite.setAttribute("to", userJid);
	final IPacket reason = invite.add("reason", null);
	reason.WithText(reasonText);
	emite.send(message);
    }

    public void removeOccupant(final XmppURI uri) {
	final Occupant occupant = occupants.remove(uri);
	if (occupant != null) {
	    fireOccupantsChanged();
	}
    }

    public void send(final String text) {
	final Message message = new Message(userURI, roomURI, text);
	message.setType(Message.Type.groupchat);
	emite.send(message);
	for (final ChatListener listener : listeners) {
	    listener.onMessageSent(this, message);
	}
    }

    public Occupant setOccupantPresence(final XmppURI uri, final String affiliation, final String role) {
	Occupant occupant = findOccupant(uri);
	if (occupant == null) {
	    occupant = new Occupant(uri, affiliation, role);
	    occupants.put(occupant.getUri(), occupant);
	    fireOccupantsChanged();
	} else {
	    occupant.setAffiliation(affiliation);
	    occupant.setRole(role);
	    fireOccupantModified(occupant);
	}
	return occupant;
    }

    /**
     * http://www.xmpp.org/extensions/xep-0045.html#subject-mod
     * 
     * @param subjectText
     */
    // FIXME: Dani, check this message
    // What about have a Message constructor, without body and neither xmlns?
    public void setSubject(final String subjectText) {
	final BasicStanza message = new BasicStanza("message", null);
	message.setFrom(userURI.toString());
	message.setTo(roomURI.toString());
	message.setType(Message.Type.groupchat.toString());
	final IPacket subject = message.add("subject", null);
	subject.addText(subjectText);
	emite.send(message);
    }

    @Override
    public String toString() {
	return "ROOM: " + roomURI;
    }

    void dispatch(final Message message) {
	final String subject = message.getFirstChild("Subject").getText();
	if (subject != null) {
	    for (final ChatListener listener : listeners) {
		((RoomListener) listener).onSubjectSet(message.getFromURI().getResource(), subject);
	    }
	} else {
	    for (final ChatListener listener : listeners) {
		listener.onMessageReceived(this, message);
	    }
	}
    }

    private void fireOccupantModified(final Occupant occupant) {
	for (final ChatListener listener : listeners) {
	    try {
		((RoomListener) listener).onOccupantModified(occupant);
	    } catch (final ClassCastException e) {
	    }
	}
    }

    private void fireOccupantsChanged() {
	for (final ChatListener listener : listeners) {
	    try {
		((RoomListener) listener).onOccupantsChanged(occupants.values());
	    } catch (final ClassCastException e) {
	    }
	}
    }

}
