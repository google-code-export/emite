package com.calclab.emite.client.extra.muc;

import static com.calclab.emite.testing.TestMatchers.packetLike;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.extra.muc.Occupant.Affiliation;
import com.calclab.emite.client.extra.muc.Occupant.Role;
import com.calclab.emite.client.xmpp.session.SessionManager;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.testing.EmiteStub;

public class MUCRoomManagerTest {

    private EmiteStub emite;
    private RoomManagerListener listener;
    private MUCRoomManager manager;

    @Before
    public void aaCreate() {
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
	emite.receives(SessionManager.Events.onLoggedOut);
	verify(listener).onChatClosed(room2);
	verify(listener).onChatClosed(room1);
    }

    @Test
    public void shouldCreateInstantRoomIfNeeded() {
	manager.setUserURI("user@localhost/resource");
	manager.openChat(XmppURI.parse("newroomtest1@rooms.localhost/nick"));
	emite.receives("<presence from='newroomtest1@rooms.localhost/nick' to='user@localhost/resource' >"
		+ "<priority>5</priority>" + "<x xmlns='http://jabber.org/protocol/muc#user'>"
		+ "<item affiliation='owner' role='moderator' jid='vjrj@localhost/Psi' />" + "<status code='201' />"
		+ "</x>" + "</presence>");
	emite.verifySendCallback(new IQ(Type.set));
    }

    @Test
    public void shouldFireChatMessages() {
	manager.setUserURI("user@domain/resource");
	final Room chat = manager.openChat(XmppURI.parse("room@rooms.domain/user"));
	final RoomListener roomListener = mock(RoomListener.class);
	chat.addListener(roomListener);
	final String message = "<message from='room@rooms.domain/other' to='user@domain/resource' "
		+ "type='groupchat'><body>the message body</body></message>";
	emite.receives(message);
	verify(roomListener).onMessageReceived(eq(chat), (Message) packetLike(message));
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
		+ "<x xmlns='http://jabber.org/protocol/muc#user'><invite to='hecate@shakespeare.lit'>"
		+ "<reason>The reason here</reason></invite></x></message>";
	emite.receives(message);
	// verify(listener).onInvitationReceived(XmppURI.parse("user@domain/resource"),
	// XmppURI.parse("room@conference.domain", "The reason here!");
    }

    @Test
    public void shouldUpdateRoomPresence() {
	manager.setUserURI("user@domain/resource");
	final Room room = manager.openChat(XmppURI.parse("room1@domain/nick"));

	emite.receives("<presence to='user@domain/resource' xmlns='jabber:client' from='room1@domain/otherUser'>"
		+ "<x xmlns='http://jabber.org/protocol/muc#user'>"
		+ "<item role='moderator' affiliation='owner' /></x></presence>");
	assertEquals(1, room.getOccupantsCount());
	Occupant user = room.findOccupant(XmppURI.parse("room1@domain/otherUser"));
	assertNotNull(user);
	assertEquals(Affiliation.owner, user.getAffiliation());
	assertEquals(Role.moderator, user.getRole());

	emite.receives("<presence to='user@domain/resource' xmlns='jabber:client' from='room1@domain/otherUser'>"
		+ "<x xmlns='http://jabber.org/protocol/muc#user'>"
		+ "<item role='participant' affiliation='member' /></x></presence>");
	assertEquals(1, room.getOccupantsCount());
	user = room.findOccupant(XmppURI.parse("room1@domain/otherUser"));
	assertNotNull(user);
	assertEquals(Affiliation.member, user.getAffiliation());
	assertEquals(Role.participant, user.getRole());

	emite
		.receives("<presence to='user@domain/res1' type='unavailable' xmlns='jabber:client' from='room1@domain/otherUser'>"
			+ "<status>custom message</status><x xmlns='http://jabber.org/protocol/muc#user'>"
			+ "<item role='none' affiliation='member' /></x></presence>");
	assertEquals(0, room.getOccupantsCount());

    }
}
