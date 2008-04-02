package com.calclab.emite.client.im.chat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.calclab.emite.client.core.bosh.EmiteBosh;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.mock.MockDispatcher;

public class TestChat {

	@Test
	public void testSend() {
		final MockDispatcher dispatcher = new MockDispatcher();
		final ChatManager chatManager = new ChatManager(dispatcher);
		chatManager.send("jid", "body");
		assertEquals(1, dispatcher.getLength());
		final Packet published = dispatcher.getPublished(0);
		assertEquals(EmiteBosh.Events.send.getType(), published.getAttribute("type"));
		final Packet message = published.getFirstChild("message");
		assertNotNull(message);
	}
}
