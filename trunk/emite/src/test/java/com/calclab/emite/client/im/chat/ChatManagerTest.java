package com.calclab.emite.client.im.chat;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;

import com.calclab.emite.client.xmpp.session.SessionManager;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.testing.EmiteTestHelper;

public class ChatManagerTest {

    public static class ChatTrap extends ArgumentMatcher<Chat> {
	public Chat chat;

	@Override
	public boolean matches(final Object argument) {
	    this.chat = (Chat) argument;
	    return true;
	}

    }
    private static final XmppURI MYSELF = uri("self@domain");
    private ChatManagerDefault manager;
    private ChatManagerListener listener;

    private EmiteTestHelper emite;

    @Before
    public void aaCreate() {
	emite = new EmiteTestHelper();
	manager = new ChatManagerDefault(emite);
	listener = Mockito.mock(ChatManagerListener.class);
	manager.addListener(listener);
	manager.setUserURI(MYSELF.toString());
    }

    @Test
    public void everyChatOpenedByUserShouldHaveThread() {
	final Chat chat = manager.openChat(uri("other@domain/resource"), null, null);
	assertNotNull(chat.getThread());
    }

    @Test
    public void managerShouldCreateOneChatForSameResource() {
	emite.receives(new Message(uri("source@domain/resource1"), MYSELF, "message 1"));
	emite.receives(new Message(uri("source@domain/resource1"), MYSELF, "message 2"));
	verify(listener, times(1)).onChatCreated((Chat) anyObject());
    }

    @Test
    public void managerShouldCreateOneChatIfResourceIsNotAvailable() {
	emite.receives(new Message(uri("source@domain"), MYSELF, "message 1"));
	emite.receives(new Message(uri("source@domain/resource1"), MYSELF, "message 2"));
	verify(listener, times(1)).onChatCreated((Chat) anyObject());
    }

    @Test
    public void shouldCloseChatWhenLoggedOut() {
	final Chat chat = manager.openChat(uri("name@domain/resouce"), null, null);
	emite.receives(SessionManager.Events.onLoggedOut);
	verify(listener).onChatClosed(same(chat));
    }

    @Test
    public void shouldHandleIncommingMessages() {
	emite.receives("<message to='" + MYSELF + "' from='otherUser@dom/res' id='theId0001'>"
		+ "<body>This is the body</body></message>");
	final ChatTrap trap = new ChatTrap();
	verify(listener).onChatCreated(argThat(trap));
	assertEquals("otherUser@dom/res", trap.chat.getOtherURI().toString());
	assertEquals(null, trap.chat.getThread());
    }

    @Test
    public void shouldPublishMessagesWithoutBody() {
	final Chat chat = manager.openChat(uri("someone@domain"), null, null);
	final ChatListener chatListener = mock(ChatListener.class);
	chat.addListener(chatListener);
	emite.receives("<message type='chat' id='purplee8b92642' to='user@domain' "
		+ "from='someone@domain'><x xmlns='jabber:x:event'/><active"
		+ "xmlns='http://jabber.org/protocol/chatstates'/></message>");
	verify(chatListener, times(1)).onMessageReceived(same(chat), (Message) anyObject());
    }

    @Test
    public void shouldUseSameRoomWhenAnswering() {
	final Chat chat = manager.openChat(uri("someone@domain"), null, null);
	emite.receives(new Message(uri("someone@domain/resource"), MYSELF, "answer").Thread(chat.getThread()));
	verify(listener, times(1)).onChatCreated(chat);
    }
}
