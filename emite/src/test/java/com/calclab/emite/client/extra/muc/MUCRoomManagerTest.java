package com.calclab.emite.client.extra.muc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.TestMatchers;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.extra.muc.Occupant.Affiliation;
import com.calclab.emite.client.extra.muc.Occupant.Role;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.j2se.services.TigaseXMLService;

public class MUCRoomManagerTest {

    private Emite emite;
    private RoomManagerListener listener;
    private MUCRoomManager manager;
    private TigaseXMLService xmler;

    @Before
    public void aaCreate() {
	xmler = new TigaseXMLService();
	emite = mock(Emite.class);
	manager = new MUCRoomManager(emite);
	listener = mock(RoomManagerListener.class);
	manager.addListener(listener);
    }

    @Test
    public void shouldCloseAllActiveRoomsWhenLoggedOut() {
	final Room room1 = manager.openChat(XmppURI.parse("room1@domain/nick"));
	final Room room2 = manager.openChat(XmppURI.parse("room2@domain/nick"));
	manager.onLoggedOut();
	verify(listener).onChatClosed(room2);
	verify(listener).onChatClosed(room1);
    }

    @Test
    public void shouldCreateInstantRoomIfNeeded() {
	manager.setUserURI("user@localhost/resource");
	manager.openChat(XmppURI.parse("newroomtest1@rooms.localhost/nick"));
	final IPacket packet = xmler
		.toXML("<presence from=\"newroomtest1@rooms.localhost/nick\" to=\"user@localhost/resource\" >"
			+ "<priority>5</priority>" + "<x xmlns=\"http://jabber.org/protocol/muc#user\">"
			+ "<item affiliation=\"owner\" role=\"moderator\" jid=\"vjrj@localhost/Psi\" />"
			+ "<status code=\"201\" />" + "</x>" + "</presence>");
	manager.eventPresence(new Presence(packet));
	final IQ expected = new IQ(Type.set);
	verify(emite).send(anyString(), TestMatchers.isPacket(expected), (PacketListener) anyObject());
    }

    // FIXME: revisar si esto tiene lógica
    @Test
    public void shouldGiveSameRoomsWithSameURIS() {
	final Room room1 = manager.openChat(XmppURI.parse("room@domain/nick"));
	final Room room2 = manager.openChat(XmppURI.parse("room@domain/nick"));
	assertSame(room1, room2);
    }

    @Test
    public void shouldUpdateRoomPresence() {
	manager.setUserURI("user@domain/resource");
	final Room room = manager.openChat(XmppURI.parse("room1@domain/nick"));

	final IPacket presence = new Presence(null, "room1@domain/otherUser", "user@domain/resource").With(new Packet(
		"x", "http://jabber.org/protocol/muc#user").With(new Packet("item").With("affiliation", "owner").With(
		"role", "moderator")));
	manager.eventPresence(new Presence(presence));
	assertEquals(1, room.getOccupantsCount());
	Occupant user = room.findOccupant(XmppURI.parse("room1@domain/otherUser"));
	assertNotNull(user);
	assertEquals(Affiliation.owner, user.getAffiliation());
	assertEquals(Role.moderator, user.getRole());

	final IPacket presence2 = new Presence(null, "room1@domain/otherUser", "user@domain/resource").With(new Packet(
		"x", "http://jabber.org/protocol/muc#user").With(new Packet("item").With("affiliation", "member").With(
		"role", "participant")));
	manager.eventPresence(new Presence(presence2));
	assertEquals(1, room.getOccupantsCount());
	user = room.findOccupant(XmppURI.parse("room1@domain/otherUser"));
	assertNotNull(user);
	assertEquals(Affiliation.member, user.getAffiliation());
	assertEquals(Role.participant, user.getRole());

	final IPacket presence3 = new Presence(null, "room1@domain/otherUser", "user@domain/resource").With("type",
		"unavailable").With(new Packet("status").WithText("custom message")).With(
		new Packet("x", "http://jabber.org/protocol/muc#user").With(new Packet("item").With("affiliation",
			"member").With("role", "none")));
	manager.eventPresence(new Presence(presence3));
	assertEquals(0, room.getOccupantsCount());

    }
}
