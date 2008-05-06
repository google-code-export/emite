package com.calclab.emite.client.extra.muc;

import static com.calclab.emite.testing.MockitoEmiteHelper.packetLike;
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
import com.calclab.emite.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.testing.EmiteTestHelper;
import static com.calclab.emite.client.xmpp.stanzas.XmppURI.*;

public class MUCRoomManagerTest {

    private EmiteTestHelper emite;
    private RoomManagerListener listener;
    private MUCRoomManager manager;

    @Before
    public void aaCreate() {
	emite = new EmiteTestHelper();
	manager = new MUCRoomManager(emite);
	listener = mock(RoomManagerListener.class);
	manager.addListener(listener);
	manager.install();
    }

    @Test
    public void shouldCloseAllActiveRoomsWhenLoggedOut() {
	final Room room1 = manager.openChat(uri("room1@domain/nick"));
	final Room room2 = manager.openChat(uri("room2@domain/nick"));
	emite.receives(SessionManager.Events.onLoggedOut);
	verify(listener).onChatClosed(room2);
	verify(listener).onChatClosed(room1);
    }

    @Test
    public void shouldCreateInstantRoomIfNeeded() {
	manager.setUserURI("user@localhost/resource");
	manager.openChat(uri("newroomtest1@rooms.localhost/nick"));
	emite.receives("<presence from='newroomtest1@rooms.localhost/nick' to='user@localhost/resource' >"
		+ "<priority>5</priority>" + "<x xmlns='http://jabber.org/protocol/muc#user'>"
		+ "<item affiliation='owner' role='moderator' jid='vjrj@localhost/Psi' />" + "<status code='201' />"
		+ "</x>" + "</presence>");
	emite.verifyIQSent(new IQ(Type.set));
    }

    @Test
    public void shouldFireChatMessages() {
	manager.setUserURI("user@domain/resource");
	final Room chat = manager.openChat(uri("room@rooms.domain/user"));
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
	final Room room1 = manager.openChat(uri("room@domain/nick"));
	final Room room2 = manager.openChat(uri("room@domain/nick"));
	assertSame(room1, room2);
    }

    @Test
    public void shouldHandleRoomInvitations() {
	manager.setUserURI("user@domain/resource");
	final String message = "<message to='user@domain/resource' from='room@conference.domain'>"
		+ "<x xmlns='http://jabber.org/protocol/muc#user'><invite from='otherUser@domain/resource'>"
		+ "<reason>The reason here</reason></invite></x></message>";
	emite.receives(message);
	verify(listener).onInvitationReceived(uri("otherUser@domain/resource"), uri("room@conference.domain"),
		"The reason here");
    }

    @Test
    public void shouldUpdateRoomPresence() {
	manager.setUserURI("user@domain/resource");
	final Room room = manager.openChat(uri("room1@domain/nick"));

	emite.receives("<presence to='user@domain/resource' xmlns='jabber:client' from='room1@domain/otherUser'>"
		+ "<x xmlns='http://jabber.org/protocol/muc#user'>"
		+ "<item role='moderator' affiliation='owner' /></x></presence>");
	assertEquals(1, room.getOccupantsCount());
	Occupant user = room.findOccupant(uri("room1@domain/otherUser"));
	assertNotNull(user);
	assertEquals(Affiliation.owner, user.getAffiliation());
	assertEquals(Role.moderator, user.getRole());

	emite.receives("<presence to='user@domain/resource' xmlns='jabber:client' from='room1@domain/otherUser'>"
		+ "<x xmlns='http://jabber.org/protocol/muc#user'>"
		+ "<item role='participant' affiliation='member' /></x></presence>");
	assertEquals(1, room.getOccupantsCount());
	user = room.findOccupant(uri("room1@domain/otherUser"));
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
