package com.calclab.emite.im.client.chat;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.im.client.chat.Chat.State;
import com.calclab.suco.testing.listener.MockListener;

public class ChatManagerTest extends AbstractChatManagerTest {

    @Test
    public void managerShouldCreateOneChatForSameResource() {
	final MockListener<Chat> listener = addOnChatCreatedListener();
	session.receives(new Message(uri("source@domain/resource1"), MYSELF, "message 1"));
	session.receives(new Message(uri("source@domain/resource1"), MYSELF, "message 2"));
	assertTrue(listener.isCalledOnce());
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
	final MockListener<State> listener = new MockListener<State>();
	chat.onStateChanged(listener);
	session.logout();
	assertTrue(listener.isCalledWithEquals(State.locked));
    }

    @Test
    public void shouldEventIncommingMessages() {
	final Chat chat = manager.openChat(uri("someone@domain"), null, null);
	final MockListener<Message> listener = new MockListener<Message>();
	chat.onMessageReceived(listener);
	session.receives("<message type='chat' id='purplee8b92642' to='user@domain' "
		+ "from='someone@domain'><x xmlns='jabber:x:event'/><active"
		+ "xmlns='http://jabber.org/protocol/chatstates'/></message>");
	assertTrue(listener.isCalledOnce());
    }

    @Test
    public void shouldReuseChatIfNotResouceSpecified() {
	final MockListener<Chat> listener = addOnChatCreatedListener();
	session.receives(new Message(uri("source@domain"), MYSELF, "message 1"));
	session.receives(new Message(uri("source@domain/resource1"), MYSELF, "message 2"));
	assertTrue(listener.isCalledOnce());
    }

    @Test
    public void shouldUseSameRoomWhenAnswering() {
	final MockListener<Chat> listener = addOnChatCreatedListener();
	final Chat chat = manager.openChat(uri("someone@domain"), null, null);
	assertTrue(listener.isCalledOnce());
	assertTrue(listener.isCalledWithSame(chat));
	session.receives(new Message(uri("someone@domain/resource"), MYSELF, "answer").Thread(chat.getThread()));
	assertTrue(listener.isCalledOnce());
    }

    @Override
    protected ChatManagerImpl createChatManager() {
	final ChatManagerImpl chatManagerDefault = new ChatManagerImpl(session);
	return chatManagerDefault;
    }

    private MockListener<Chat> addOnChatCreatedListener() {
	final MockListener<Chat> listener = new MockListener<Chat>();
	manager.onChatCreated(listener);
	return listener;
    }
}
