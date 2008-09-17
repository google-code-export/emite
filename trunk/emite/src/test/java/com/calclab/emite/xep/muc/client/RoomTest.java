package com.calclab.emite.xep.muc.client;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.AbstractChat;
import com.calclab.emite.im.client.chat.AbstractChatTest;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.Chat.State;
import com.calclab.emite.testing.MockedSession;
import com.calclab.suco.testing.listener.MockListener;
import com.calclab.suco.testing.listener.MockListener2;

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
	room = new Room(session, roomURI);
	room.setStatus(State.ready);
    }

    @Override
    public AbstractChat getChat() {
	return room;
    }

    @Test
    public void shouldAddOccupantAndFireListeners() {
	final MockListener<Collection<Occupant>> listener = new MockListener<Collection<Occupant>>();
	room.onOccupantsChanged(listener);
	final XmppURI uri = uri("room@domain/name");
	final Occupant occupant = room.setOccupantPresence(uri, "aff", "role");
	MockListener.verifyCalled(listener, 1);
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
    public void shouldCreateInstantRooms() {
	final MockListener<State> listener = new MockListener<Chat.State>();
	room.onStateChanged(listener);
	session.receives("<presence to='user@domain/res' from='room@domain/nick'>"
		+ "<x xmlns='http://jabber.org/protocol/muc#user'>"
		+ "<item affiliation='owner' role='moderator'/><status code='201'/></x></presence>");
	session.verifyIQSent("<iq to='room@domain/nick' type='set'>"
		+ "<query xmlns='http://jabber.org/protocol/muc#owner'>"
		+ "<x xmlns='jabber:x:data' type='submit'/></query></iq>");
	session.answerSuccess();
	MockListener.verifyCalled(listener);
    }

    @Test
    public void shouldFireListenersWhenMessage() {
	final MockListener<Message> listener = new MockListener<Message>();
	room.onMessageReceived(listener);
	final Message message = new Message(uri("someone@domain/res"), uri("room@domain"), "message");
	room.receive(message);
	MockListener.verifyCalledWith(listener, message);
    }

    @Test
    public void shouldFireListenersWhenSubjectChange() {
	final MockListener<Message> messageListener = new MockListener<Message>();
	room.onMessageReceived(messageListener);
	final MockListener2<Occupant, String> subjectListener = new MockListener2<Occupant, String>();
	room.onSubjectChanged(subjectListener);

	room.receive(new Message(uri("someone@domain/res"), uri("room@domain"), null).Subject("the subject"));
	assertEquals(1, subjectListener.getCalledTimes());
	// FIXME
	// verify(listener).onSubjectChanged(userURI.getResource(),
	// "the subject");
	assertEquals(0, messageListener.getCalledTimes());
    }

    @Test
    public void shouldRemoveOccupant() {
	final MockListener<Collection<Occupant>> listener = new MockListener<Collection<Occupant>>();
	room.onOccupantsChanged(listener);
	final XmppURI uri = uri("room@domain/name");
	room.setOccupantPresence(uri, "owner", "participant");
	assertEquals(1, room.getOccupantsCount());
	room.removeOccupant(uri);
	assertEquals(0, room.getOccupantsCount());
	assertEquals(2, listener.getCalledTimes());
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
    public void shouldSendRoomPresenceWhenCreated() {
	session.verifySent("<presence to='room@domain/nick'><x xmlns='http://jabber.org/protocol/muc' /></presence>");
    }

    @Test
    public void shouldUpdateOccupantAndFireListeners() {
	final MockListener<Occupant> listener = new MockListener<Occupant>();
	room.onOccupantModified(listener);
	final XmppURI uri = uri("room@domain/name");
	final Occupant occupant = room.setOccupantPresence(uri, "owner", "participant");
	final Occupant occupant2 = room.setOccupantPresence(uri, "admin", "moderator");
	assertEquals(1, listener.getCalledTimes());
	assertSame(occupant, occupant2);
    }

}
