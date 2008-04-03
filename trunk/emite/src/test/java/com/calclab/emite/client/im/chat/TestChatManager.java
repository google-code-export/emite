package com.calclab.emite.client.im.chat;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.components.ContainerPlugin;
import com.calclab.emite.client.components.Globals;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.DispatcherPlugin;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public class TestChatManager {

    private Dispatcher dispatcher;
    private Globals globals;
    private ChatManagerDefault manager;

    @Before
    public void createManager() {
	final Container container = ContainerPlugin.create();
	DispatcherPlugin.install(container);
	ChatPlugin.install(container);
	container.start();
	dispatcher = DispatcherPlugin.getDispatcher(container);
	globals = ContainerPlugin.getGlobals(container);
	manager = ChatPlugin.getChat(container);
	globals.setOwnURI(XmppURI.parse("own@owndomain/ownres"));
    }

    @Test
    public void managerShouldCreateNewRoomsAndFireToListeners() {
	final ChatManagerListener listener = Mockito.mock(ChatManagerListener.class);
	manager.addListener(listener);

	assertEquals(0, manager.getChats().size());
	dispatcher.publish(new Message("from@localhost", "to@localhost", "hola!"));
	assertEquals(1, manager.getChats().size());

	final ChatDefault theOnlyChat = manager.getChats().toArray(new ChatDefault[1])[0];
	Mockito.verify(listener).onChatCreated(theOnlyChat);

	final ChatListener chatListener = Mockito.mock(ChatListener.class);
	theOnlyChat.addListener(chatListener);
	final Message secondMessage = new Message("from@localhost", "to@localhost", "adiós!");
	dispatcher.publish(secondMessage);
	Mockito.verify(chatListener).onMessageReceived(theOnlyChat, secondMessage);
    }
}
