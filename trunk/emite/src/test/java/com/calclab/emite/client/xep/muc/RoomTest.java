package com.calclab.emite.client.xep.muc;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.im.chat.AbstractChat;
import com.calclab.emite.client.im.chat.AbstractChatTest;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.chat.Chat.Status;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.testing.MockedSession;
import com.calclab.emite.testing.MockitoEmiteHelper;

@SuppressWarnings("unchecked")
public class RoomTest extends AbstractChatTest {

    private RoomListener listener;
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
	listener = mock(RoomListener.class);
	room.addListener(listener);
    }

    @Override
    public AbstractChat getChat() {
	return room;
    }

    @Test
    public void shouldAddOccupantAndFireListeners() {
	final XmppURI uri = uri("room@domain/name");
	final Occupant occupant = room.setOccupantPresence(uri, "aff", "role");
	verify(listener).onOccupantsChanged(MockitoEmiteHelper.isCollectionOfSize(1));
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
	final Message message = new Message(uri("someone@domain/res"), uri("room@domain"), "message");
	room.receive(message);
	verify(listener).onMessageReceived((Chat) anyObject(), eq(message));
    }

    @Test
    public void shouldFireListenersWhenSubjectChange() {
	room.receive(new Message(uri("someone@domain/res"), uri("room@domain"), null).Subject("the subject"));
	verify(listener).onSubjectChanged(userURI.getResource(), "the subject");
	verify(listener, never()).onMessageReceived((Chat) anyObject(), (Message) anyObject());
    }

    @Test
    public void shouldRemoveOccupant() {
	final XmppURI uri = uri("room@domain/name");
	room.setOccupantPresence(uri, "owner", "participant");
	assertEquals(1, room.getOccupantsCount());
	room.removeOccupant(uri);
	assertEquals(0, room.getOccupantsCount());
	verify(listener, times(2)).onOccupantsChanged((Collection<Occupant>) anyObject());
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
	final XmppURI uri = uri("room@domain/name");
	final Occupant occupant = room.setOccupantPresence(uri, "owner", "participant");
	final Occupant occupant2 = room.setOccupantPresence(uri, "admin", "moderator");
	verify(listener).onOccupantModified(occupant2);
	assertSame(occupant, occupant2);
    }

}
