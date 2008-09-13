package com.calclab.emite.client.xep.muc;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.im.chat.AbstractChat;
import com.calclab.emite.client.im.chat.AbstractChatTest;
import com.calclab.emite.client.im.chat.Chat.Status;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.testing.MockedSession;
import com.calclab.suco.testing.signal.MockSlot;
import com.calclab.suco.testing.signal.MockSlot2;

public class RoomTest extends AbstractChatTest {

    private Room room;
    private XmppURI userURI;
    private XmppURI roomURI;
    private MockedSession session;

    @Before
    public void beforeTests() {
	userURI = uri("user@domain/res");
	roomURI = uri("room@domain/nick");
	session = new MockedSession(userURI);
	room = new Room(session, roomURI, "roomName");
	room.setStatus(Status.ready);
    }

    @Override
    public AbstractChat getChat() {
	return room;
    }

    @Test
    public void shouldAddOccupantAndFireListeners() {
	final MockSlot<Collection<Occupant>> slot = new MockSlot<Collection<Occupant>>();
	room.onOccupantsChanged(slot);
	final XmppURI uri = uri("room@domain/name");
	final Occupant occupant = room.setOccupantPresence(uri, "aff", "role");
	MockSlot.verifyCalled(slot, 1);
	final Occupant result = room.findOccupant(uri);
	assertEquals(occupant, result);
    }

    @Test
    public void shouldChangeSubject() {
	room.setSubject("Some subject");
	session.verifySent("<message type=\"groupchat\" from=\"" + userURI + "\" to=\"" + room.getOtherURI()
		+ "\"><subject>Some subject</subject></message></body>");
    }

    @Test
    public void shouldFireListenersWhenMessage() {
	final MockSlot<Message> slot = new MockSlot<Message>();
	room.onMessageReceived(slot);
	final Message message = new Message(uri("someone@domain/res"), uri("room@domain"), "message");
	room.receive(message);
	MockSlot.verifyCalledWith(slot, message);
    }

    @Test
    public void shouldFireListenersWhenSubjectChange() {
	final MockSlot<Message> messageSlot = new MockSlot<Message>();
	room.onMessageReceived(messageSlot);
	final MockSlot2<Occupant, String> subjectSlot = new MockSlot2<Occupant, String>();
	room.onSubjectChanged(subjectSlot);

	room.receive(new Message(uri("someone@domain/res"), uri("room@domain"), null).Subject("the subject"));
	assertEquals(1, subjectSlot.getCalledTimes());
	// FIXME
	// verify(listener).onSubjectChanged(userURI.getResource(),
	// "the subject");
	assertEquals(0, messageSlot.getCalledTimes());
    }

    @Test
    public void shouldRemoveOccupant() {
	final MockSlot<Collection<Occupant>> slot = new MockSlot<Collection<Occupant>>();
	room.onOccupantsChanged(slot);
	final XmppURI uri = uri("room@domain/name");
	room.setOccupantPresence(uri, "owner", "participant");
	assertEquals(1, room.getOccupantsCount());
	room.removeOccupant(uri);
	assertEquals(0, room.getOccupantsCount());
	assertEquals(2, slot.getCalledTimes());
	assertNull(room.findOccupant(uri));
    }

    @Test
    public void shouldSendRoomInvitation() {
	room.sendInvitationTo("otherUser@domain/resource", "this is the reason");
	session.verifySent("<message from='" + userURI + "' to='" + roomURI
		+ "'><x xmlns='http://jabber.org/protocol/muc#user'>"
		+ "<invite to='otherUser@domain/resource'><reason>this is the reason</reason></invite></x></message>");
    }

    @Test
    public void shouldUpdateOccupantAndFireListeners() {
	final MockSlot<Occupant> slot = new MockSlot<Occupant>();
	room.onOccupantModified(slot);
	final XmppURI uri = uri("room@domain/name");
	final Occupant occupant = room.setOccupantPresence(uri, "owner", "participant");
	final Occupant occupant2 = room.setOccupantPresence(uri, "admin", "moderator");
	assertEquals(1, slot.getCalledTimes());
	assertSame(occupant, occupant2);
    }

}
