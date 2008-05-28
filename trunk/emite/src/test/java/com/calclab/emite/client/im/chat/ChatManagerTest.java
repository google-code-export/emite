package com.calclab.emite.client.im.chat;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.Mockito;

import com.calclab.emite.client.im.chat.Chat.State;
import com.calclab.emite.client.xmpp.session.SessionManager;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.testing.TestingListener;

public class ChatManagerTest extends AbstractChatManagerTest {

    @Test
    public void managerShouldCreateOneChatForSameResource() {
	final TestingListener<Chat> listener = addListener();
	emite.receives(new Message(uri("source@domain/resource1"), MYSELF, "message 1"));
	listener.verify();
	listener.reset();
	emite.receives(new Message(uri("source@domain/resource1"), MYSELF, "message 2"));
	listener.verifyNotCalled();
    }

    @Test
    public void oneToOneChatsAreAlwaysReadyWhenCreated() {
	final Chat chat = manager.openChat(uri("other@domain/resource"), null, null);
	assertSame(Chat.State.ready, chat.getState());
    }

    @Test
    public void shouldBlockChatWhenClosingIt() {
	final Chat chat = manager.openChat(uri("other@domain/resource"), null, null);
	manager.close(chat);
	assertSame(Chat.State.locked, chat.getState());
    }

    @Test
    public void shouldCloseChatWhenLoggedOut() {
	final Chat chat = manager.openChat(uri("name@domain/resouce"), null, null);
	final TestingListener<State> listener = new TestingListener<State>();
	chat.onStateChanged(listener);
	emite.receives(SessionManager.Events.onLoggedOut);
	listener.verify();
	assertEquals(State.locked, listener.getValue());
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
    public void shouldReuseChatIfNotResouceSpecified() {
	final TestingListener<Chat> listener = addListener();
	emite.receives(new Message(uri("source@domain"), MYSELF, "message 1"));
	listener.verify();
	listener.reset();
	emite.receives(new Message(uri("source@domain/resource1"), MYSELF, "message 2"));
	listener.verifyNotCalled();
    }

    @Test
    public void shouldUseSameRoomWhenAnswering() {
	final TestingListener<Chat> listener = addListener();
	final Chat chat = manager.openChat(uri("someone@domain"), null, null);
	listener.verify();
	assertSame(chat, listener.getValue());
	listener.reset();
	emite.receives(new Message(uri("someone@domain/resource"), MYSELF, "answer").Thread(chat.getThread()));
	listener.verifyNotCalled();
    }

    @Test
    public void XXListenermanagerShouldCreateOneChatForSameResource() {
	final ChatManagerListener listener = Mockito.mock(ChatManagerListener.class);
	manager.addListener(listener);
	emite.receives(new Message(uri("source@domain/resource1"), MYSELF, "message 1"));
	emite.receives(new Message(uri("source@domain/resource1"), MYSELF, "message 2"));
	verify(listener, times(1)).onChatCreated((Chat) anyObject());
    }

    @Deprecated
    @Test
    public void XXListenermanagerShouldCreateOneChatIfResourceIsNotAvailable() {
	final ChatManagerListener listener = Mockito.mock(ChatManagerListener.class);
	manager.addListener(listener);
	emite.receives(new Message(uri("source@domain"), MYSELF, "message 1"));
	emite.receives(new Message(uri("source@domain/resource1"), MYSELF, "message 2"));
	verify(listener, times(1)).onChatCreated((Chat) anyObject());
    }

    @Deprecated
    @Test
    public void XXListenershouldHandleIncommingMessages() {
	final ChatManagerListener listener = Mockito.mock(ChatManagerListener.class);
	manager.addListener(listener);
	emite.receives("<message to='" + MYSELF + "' from='otherUser@dom/res' id='theId0001'>"
		+ "<body>This is the body</body></message>");
	verify(listener).onChatCreated((Chat) anyObject());
    }

    @Override
    protected ChatManagerDefault createChatManager() {
	final ChatManagerDefault chatManagerDefault = new ChatManagerDefault(emite);
	return chatManagerDefault;
    }

    private TestingListener<Chat> addListener() {
	final TestingListener<Chat> listener = new TestingListener<Chat>();
	manager.onChatCreated(listener);
	return listener;
    }
}
