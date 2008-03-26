package com.calclab.emite.client.x.imcom.calclab.emite.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.calclab.emite.client.bosh.Connection;
import com.calclab.emite.client.mock.MockDispatcher;
import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.x.im.Chat;

public class TestChat {

	@Test
	public void testSend() {
		final MockDispatcher dispatcher = new MockDispatcher();
		final Chat chat = new Chat(dispatcher);
		chat.send("jid", "body");
		assertEquals(1, dispatcher.getLength());
		final Packet published = dispatcher.getPublished(0);
		assertEquals(Connection.Events.send.getType(), published
				.getAttribute("type"));
		final Packet message = published.getFirstChild("message");
		assertNotNull(message);
	}
}
