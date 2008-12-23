package com.calclab.emite.im.client.chat;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.im.client.chat.Conversation.State;
import com.calclab.suco.testing.events.MockedListener;

public class ChatManagerTest extends AbstractChatManagerTest {

    @Test
    public void managerShouldCreateOneChatForSameResource() {
	final MockedListener<Conversation> listener = addOnChatCreatedListener();
	session.receives(new Message(uri("source@domain/resource1"), MYSELF, "message 1"));
	session.receives(new Message(uri("source@domain/resource1"), MYSELF, "message 2"));
	assertTrue(listener.isCalledOnce());
    }

    @Test
    public void oneToOneChatsAreAlwaysReadyWhenCreated() {
	final Conversation conversation = manager.openChat(uri("other@domain/resource"), null, null);
	assertSame(Conversation.State.ready, conversation.getState());
    }

    @Test
    public void shouldBlockChatWhenClosingIt() {
	final Conversation conversation = manager.openChat(uri("other@domain/resource"), null, null);
	manager.close(conversation);
	assertSame(Conversation.State.locked, conversation.getState());
    }

    @Test
    public void shouldCloseChatWhenLoggedOut() {
	final Conversation conversation = manager.openChat(uri("name@domain/resouce"), null, null);
	final MockedListener<State> listener = new MockedListener<State>();
	conversation.onStateChanged(listener);
	session.logout();
	assertTrue(listener.isCalledWithEquals(State.locked));
    }

    @Test
    public void shouldEventIncommingMessages() {
	final Conversation conversation = manager.openChat(uri("someone@domain"), null, null);
	final MockedListener<Message> listener = new MockedListener<Message>();
	conversation.onMessageReceived(listener);
	session.receives("<message type='chat' id='purplee8b92642' to='user@domain' "
		+ "from='someone@domain'><x xmlns='jabber:x:event'/><active"
		+ "xmlns='http://jabber.org/protocol/chatstates'/></message>");
	assertTrue(listener.isCalledOnce());
    }

    @Test
    public void shouldReuseChatIfNotResouceSpecified() {
	final MockedListener<Conversation> listener = addOnChatCreatedListener();
	session.receives(new Message(uri("source@domain"), MYSELF, "message 1"));
	session.receives(new Message(uri("source@domain/resource1"), MYSELF, "message 2"));
	assertTrue(listener.isCalled(2));
    }

    @Test
    public void shouldUseSameRoomWhenAnswering() {
	final MockedListener<Conversation> listener = addOnChatCreatedListener();
	final Conversation conversation = manager.openChat(uri("someone@domain"), null, null);
	assertTrue(listener.isCalledOnce());
	assertTrue(listener.isCalledWithSame(conversation));
	session.receives(new Message(uri("someone@domain/resource"), MYSELF, "answer"));
	assertTrue(listener.isCalled(2));
    }

    @Override
    protected ChatManagerImpl createChatManager() {
	final ChatManagerImpl chatManagerDefault = new ChatManagerImpl(session);
	return chatManagerDefault;
    }

    private MockedListener<Conversation> addOnChatCreatedListener() {
	final MockedListener<Conversation> listener = new MockedListener<Conversation>();
	manager.onChatCreated(listener);
	return listener;
    }
}
