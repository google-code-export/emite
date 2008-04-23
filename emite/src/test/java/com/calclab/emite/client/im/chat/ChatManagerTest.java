package com.calclab.emite.client.im.chat;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.testing.EmiteStub;

public class ChatManagerTest {

    private static final String MYSELF = "self@domain";
    private ChatManagerDefault manager;
    private ChatManagerListener listener;
    private EmiteStub emite;

    @Before
    public void a() {
	emite = new EmiteStub();
	manager = new ChatManagerDefault(emite);
	listener = Mockito.mock(ChatManagerListener.class);
	manager.addListener(listener);
	manager.setUserURI(MYSELF);
	manager.install();
    }

    @Test
    public void everyChatShouldHaveThread() {
	final Chat chat = manager.openChat(XmppURI.parse("other@domain/resource"));
	assertNotNull(chat.getThread());
    }

    @Test
    public void managerShouldCreateOneChatForSameResource() {
	emite.receives(new Message("source@domain/resource1", MYSELF, "message 1"));
	emite.receives(new Message("source@domain/resource1", MYSELF, "message 2"));
	Mockito.verify(listener, Mockito.times(1)).onChatCreated((Chat) Mockito.anyObject());
    }

    @Test
    public void managerShouldCreateOneChatIfResourceIsNotAvailable() {
	emite.receives(new Message("source@domain", MYSELF, "message 1"));
	emite.receives(new Message("source@domain/resource1", MYSELF, "message 2"));
	Mockito.verify(listener, Mockito.times(1)).onChatCreated((Chat) Mockito.anyObject());
    }

    @Test
    public void shouldHandleIncommingMessages() {
	emite.receives("<message to='" + MYSELF + "' from='otherUser@dom/res' id='theId0001'>"
		+ "<body>This is the body</body></message>");
	Mockito.verify(listener).onChatCreated((Chat) Mockito.anyObject());
    }

    @Test
    public void shouldUseSameRoomWhenAnswering() {
	final Chat chat = manager.openChat(XmppURI.parse("someone@domain"));
	emite.receives(new Message("someone@domain/resource", MYSELF, "answer").Thread(chat.getThread()));
	Mockito.verify(listener, Mockito.times(1)).onChatCreated(chat);
    }
}
