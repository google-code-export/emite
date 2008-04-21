package com.calclab.emite.client.extra.muc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.extra.muc.Occupant.Affiliation;
import com.calclab.emite.client.extra.muc.Occupant.Role;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.j2se.services.TigaseXMLService;
import com.calclab.emite.testing.EmiteStub;

public class MUCRoomManagerTest {

    private EmiteStub emite;
    private RoomManagerListener listener;
    private MUCRoomManager manager;
    private TigaseXMLService xmler;

    @Before
    public void aaCreate() {
	xmler = new TigaseXMLService();
	emite = new EmiteStub();
	manager = new MUCRoomManager(emite);
	listener = mock(RoomManagerListener.class);
	manager.addListener(listener);
	manager.install();
    }

    @Test
    public void shouldCloseAllActiveRoomsWhenLoggedOut() {
	final Room room1 = manager.openChat(XmppURI.parse("room1@domain/nick"));
	final Room room2 = manager.openChat(XmppURI.parse("room2@domain/nick"));
	manager.eventLoggedOut();
	verify(listener).onChatClosed(room2);
	verify(listener).onChatClosed(room1);
    }

    @Test
    public void shouldCreateInstantRoomIfNeeded() {
	manager.setUserURI("user@localhost/resource");
	manager.openChat(XmppURI.parse("newroomtest1@rooms.localhost/nick"));
	emite.simulate("<presence from=\"newroomtest1@rooms.localhost/nick\" to=\"user@localhost/resource\" >"
		+ "<priority>5</priority>" + "<x xmlns=\"http://jabber.org/protocol/muc#user\">"
		+ "<item affiliation=\"owner\" role=\"moderator\" jid=\"vjrj@localhost/Psi\" />"
		+ "<status code=\"201\" />" + "</x>" + "</presence>");
	emite.verifySendCallback(new IQ(Type.set));
    }

    // FIXME: revisar si esto tiene lógica
    @Test
    public void shouldGiveSameRoomsWithSameURIS() {
	final Room room1 = manager.openChat(XmppURI.parse("room@domain/nick"));
	final Room room2 = manager.openChat(XmppURI.parse("room@domain/nick"));
	assertSame(room1, room2);
    }

    @Test
    public void shouldHandleRoomInvitations() {
	final String message = "<message from='user@domain/resource' to='room@conference.domain'>"
		+ "<x xmlns='http://jabber.org/protocol/muc#user'>\n"
		+ "    <invite to='hecate@shakespeare.lit'><reason>"
		+ "The reason here</reason></invite></x></message>";
	manager.eventMessage(new Message(xmler.toXML(message)));
	// verify(listener).onInvitationReceived(XmppURI.parse("user@domain/resource"),
	// XmppURI.parse("room@conference.domain", "The reason here!");
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
