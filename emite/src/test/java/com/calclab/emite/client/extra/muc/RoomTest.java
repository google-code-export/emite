package com.calclab.emite.client.extra.muc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.testing.TestMatchers;

@SuppressWarnings("unchecked")
public class RoomTest {

    private RoomListener listener;
    private Room room;

    @Before
    public void aaCreate() {
	final XmppURI userURI = XmppURI.parse("user@domain/res");
	final XmppURI roomURI = XmppURI.parse("room@domain/nick");
	final Emite emite = mock(Emite.class);
	room = new Room(userURI, roomURI, "roomName", emite);
	listener = mock(RoomListener.class);
	room.addListener(listener);
    }

    @Test
    public void shouldAddOccupantAndFireListeners() {
	final XmppURI uri = XmppURI.parse("room@domain/name");
	final Occupant occupant = room.setOccupantPresence(uri, "aff", "role");
	verify(listener).onOccupantsChanged(TestMatchers.isCollectionOfSize(1));
	final Occupant result = room.findOccupant(uri);
	assertEquals(occupant, result);
    }

    @Test
    public void shouldFireListenersWhenMessage() {
	final Message message = new Message("someone@domain/res", "room@domain", "message");
	room.dispatch(message);
	verify(listener).onMessageReceived((Chat) anyObject(), eq(message));
    }

    @Test
    public void shouldRemoveOccupant() {
	final XmppURI uri = XmppURI.parse("room@domain/name");
	room.setOccupantPresence(uri, "owner", "participant");
	assertEquals(1, room.getOccupantsCount());
	room.removeOccupant(uri);
	assertEquals(0, room.getOccupantsCount());
	verify(listener, times(2)).onOccupantsChanged((Collection<Occupant>) anyObject());
	assertNull(room.findOccupant(uri));

    }

    @Test
    public void shouldUpdateOccupantAndFireListeners() {
	final XmppURI uri = XmppURI.parse("room@domain/name");
	final Occupant occupant = room.setOccupantPresence(uri, "owner", "participant");
	final Occupant occupant2 = room.setOccupantPresence(uri, "admin", "moderator");
	verify(listener).onOccupantModified(occupant2);
	assertSame(occupant, occupant2);
    }
}
