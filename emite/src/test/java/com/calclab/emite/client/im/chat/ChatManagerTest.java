package com.calclab.emite.client.im.chat;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.testing.EmiteStub;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.*;

public class ChatManagerTest {

    public static class ChatTrap extends ArgumentMatcher<Chat> {
	public Chat chat;

	@Override
	public boolean matches(final Object argument) {
	    this.chat = (Chat) argument;
	    return true;
	}

    }
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
	final Chat chat = manager.openChat(uri("other@domain/resource"));
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
	final ChatTrap trap = new ChatTrap();
	Mockito.verify(listener).onChatCreated(Mockito.argThat(trap));
	assertEquals("otherUser@dom/res", trap.chat.getOtherURI().toString());
	assertEquals(null, trap.chat.getThread());
    }

    @Test
    public void shouldUseSameRoomWhenAnswering() {
	final Chat chat = manager.openChat(uri("someone@domain"));
	emite.receives(new Message("someone@domain/resource", MYSELF, "answer").Thread(chat.getThread()));
	Mockito.verify(listener, Mockito.times(1)).onChatCreated(chat);
    }
}
