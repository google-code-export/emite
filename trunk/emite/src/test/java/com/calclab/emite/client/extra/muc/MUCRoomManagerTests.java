package com.calclab.emite.client.extra.muc;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.calclab.emite.client.core.emite.Emite;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public class MUCRoomManagerTests {

    private MUCRoomManager manager;
    private RoomManagerListener listener;

    @Before
    public void aaCreate() {
	final Emite emite = mock(Emite.class);
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

    // FIXME: revisar si esto tiene lógica
    @Test
    public void shouldGiveSameRoomsWithSameURIS() {
	final Room room1 = manager.openChat(XmppURI.parse("room@domain/nick"));
	final Room room2 = manager.openChat(XmppURI.parse("room@domain/nick"));
	assertSame(room1, room2);
    }
}
