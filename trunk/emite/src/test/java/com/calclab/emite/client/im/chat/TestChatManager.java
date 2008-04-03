package com.calclab.emite.client.im.chat;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.DispatcherDefault;
import com.calclab.emite.client.xmpp.stanzas.Message;

public class TestChatManager {

	private Dispatcher dispatcher;
	private ChatManager manager;

	@Before
	public void createManager() {
		dispatcher = new DispatcherDefault();
		manager = new ChatManager(dispatcher);
	}

	@Test
	public void managerShouldCreateNewRoomsAndFireToListeners() {
		final ChatManagerListener listener = Mockito.mock(ChatManagerListener.class);
		manager.addListener(listener);

		assertEquals(0, manager.getChats().size());
		dispatcher.publish(new Message("from@localhost", "to@localhost"));
		assertEquals(1, manager.getChats().size());

		final Chat theOnlyChat = manager.getChats().toArray(new Chat[1])[0];
		Mockito.verify(listener).onChatCreated(theOnlyChat);
	}
}
