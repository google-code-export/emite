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

import java.util.Collection;
import java.util.HashMap;

import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.im.chat.AbstractChat;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.chat.ChatListener;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.stanzas.BasicStanza;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.Presence.Type;
import com.calclab.suco.client.signal.Signal;
import com.calclab.suco.client.signal.Signal2;
import com.calclab.suco.client.signal.Slot;
import com.calclab.suco.client.signal.Slot2;

public class Room extends AbstractChat implements Chat {
    private final HashMap<XmppURI, Occupant> occupants;
    private final String name;
    private final Signal<Occupant> onOccupantModified;
    private final Signal<Collection<Occupant>> onOccupantsChanged;
    private final Signal2<Occupant, String> onSubjectChanged;

    public Room(final Session session, final XmppURI roomURI, final String name) {
	super(session, roomURI);
	this.name = name;
	this.occupants = new HashMap<XmppURI, Occupant>();
	this.onOccupantModified = new Signal<Occupant>("onOccupantModified");
	this.onOccupantsChanged = new Signal<Collection<Occupant>>("onOccupantsChanged");
	this.onSubjectChanged = new Signal2<Occupant, String>("onSubjectChanged");
    }

    /**
     * In order to exit a multi-user chat room, an occupant sends a presence
     * stanza of type "unavailable" to the <room@service/nick> it is currently
     * using in the room.
     * 
     * @see http://www.xmpp.org/extensions/xep-0045.html#exit
     */
    public void close() {
	session.send(new Presence(Type.unavailable, getFromURI(), getOtherURI()));
	setStatus(Status.locked);
    }

    public Occupant findOccupant(final XmppURI uri) {
	return occupants.get(uri);
    }

    public String getID() {
	return other.toString();
    }

    /**
     * Return the room name
     * 
     * @return the name
     */
    public String getName() {
	return name;
    }

    public Object getOccupantsCount() {
	return occupants.size();
    }

    public String getThread() {
	return other.getNode();
    }

    public void onOccupantModified(final Slot<Occupant> slot) {
	onOccupantModified.add(slot);
    }

    public void onOccupantsChanged(final Slot<Collection<Occupant>> slot) {
	onOccupantsChanged.add(slot);
    }

    public void onSubjectChanged(final Slot2<Occupant, String> slot) {
	onSubjectChanged.add(slot);
    }

    @Override
    public void receive(final Message message) {
	final String subject = message.getSubject();
	if (subject != null) {
	    onBeforeReceive.fire(message);
	    fireSubjectChanged(message, subject);
	}
	if (message.getBody() != null) {
	    super.receive(message);
	}
    }

    public void removeOccupant(final XmppURI uri) {
	final Occupant occupant = occupants.remove(uri);
	if (occupant != null) {
	    fireOccupantsChanged();
	}
    }

    @Override
    public void send(final Message message) {
	message.setType(Message.Type.groupchat);
	super.send(message);
    }

    /**
     * 
     * http://www.xmpp.org/extensions/xep-0045.html#invite
     * 
     * @param userJid
     *            user to invite
     * @param reasonText
     *            reason for the invitation
     */
    public void sendInvitationTo(final String userJid, final String reasonText) {
	final BasicStanza message = new BasicStanza("message", null);
	message.setFrom(session.getCurrentUser());
	message.setTo(other);
	final IPacket x = message.addChild("x", "http://jabber.org/protocol/muc#user");
	final IPacket invite = x.addChild("invite", null);
	invite.setAttribute("to", userJid);
	final IPacket reason = invite.addChild("reason", null);
	reason.WithText(reasonText);
	session.send(message);
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

    @Override
    public void setStatus(final Status status) {
	super.setStatus(status);
    }

    /**
     * http://www.xmpp.org/extensions/xep-0045.html#subject-mod
     * 
     * @param subjectText
     */
    public void setSubject(final String subjectText) {
	final BasicStanza message = new BasicStanza("message", null);
	message.setFrom(session.getCurrentUser());
	message.setTo(other);
	message.setType(Message.Type.groupchat.toString());
	final IPacket subject = message.addChild("subject", null);
	subject.setText(subjectText);
	session.send(message);
    }

    @Override
    public String toString() {
	return "ROOM: " + other;
    }

    private void fireOccupantModified(final Occupant occupant) {
	onOccupantModified.fire(occupant);
	for (final ChatListener listener : listeners) {
	    try {
		((RoomListener) listener).onOccupantModified(occupant);
	    } catch (final ClassCastException e) {
	    }
	}
    }

    private void fireOccupantsChanged() {
	final Collection<Occupant> values = occupants.values();
	onOccupantsChanged.fire(values);
	for (final ChatListener listener : listeners) {
	    try {
		((RoomListener) listener).onOccupantsChanged(values);
	    } catch (final ClassCastException e) {
	    }
	}
    }

    private void fireSubjectChanged(final Message message, final String subject) {
	onSubjectChanged.fire(occupants.get(message.getFrom()), subject);
	for (final ChatListener listener : listeners) {
	    ((RoomListener) listener).onSubjectChanged(message.getFrom().getResource(), subject);
	}
    }

}
