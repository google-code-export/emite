package com.calclab.emite.client.im.chat;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.calclab.emite.client.core.emite.Emite;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public class ChatManagerTests {

    private static final String MYSELF = "self@domain";
    private ChatManagerDefault chatManagerDefault;
    private ChatManagerListener listener;

    @Before
    public void a() {
	final Emite emite = Mockito.mock(Emite.class);
	chatManagerDefault = new ChatManagerDefault(emite);
	listener = Mockito.mock(ChatManagerListener.class);
	chatManagerDefault.addListener(listener);
	chatManagerDefault.setUserURI(MYSELF);
    }

    @Test
    public void everyChatShouldHaveThread() {
	final Chat chat = chatManagerDefault.openChat(XmppURI.parse("other@domain/resource"));
	assertNotNull(chat.getThread());
    }

    @Test
    public void managerShouldCreateOneChatForSameResource() {
	chatManagerDefault.onMessageReceived(new Message("source@domain/resource1", MYSELF, "message 1"));
	chatManagerDefault.onMessageReceived(new Message("source@domain/resource1", MYSELF, "message 2"));
	Mockito.verify(listener, Mockito.times(1)).onChatCreated((Chat) Mockito.anyObject());
    }

    @Test
    public void managerShouldCreateOneChatIfResourceIsNotAvailable() {
	chatManagerDefault.onMessageReceived(new Message("source@domain", MYSELF, "message 1"));
	chatManagerDefault.onMessageReceived(new Message("source@domain/resource1", MYSELF, "message 2"));
	Mockito.verify(listener, Mockito.times(1)).onChatCreated((Chat) Mockito.anyObject());
    }

    @Test
    public void shouldUseSameRoomWhenAnswering() {
	final Chat chat = chatManagerDefault.openChat(XmppURI.parse("someone@domain"));
	chatManagerDefault.onMessageReceived(new Message("someone@domain/resource", MYSELF, "answer").Thread(chat
		.getThread()));
	Mockito.verify(listener, Mockito.times(1)).onChatCreated(chat);
    }
}
