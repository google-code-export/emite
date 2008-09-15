package com.calclab.emite.im.client.chat;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.ChatManagerImpl;
import com.calclab.emite.im.client.chat.Chat.Status;
import com.calclab.suco.testing.listener.MockListener;

public class ChatManagerTest extends AbstractChatManagerTest {

    @Test
    public void managerShouldCreateOneChatForSameResource() {
	final MockListener<Chat> listener = addOnChatCreatedListener();
	session.receives(new Message(uri("source@domain/resource1"), MYSELF, "message 1"));
	session.receives(new Message(uri("source@domain/resource1"), MYSELF, "message 2"));
	MockListener.verifyCalled(listener, 1);
    }

    @Test
    public void oneToOneChatsAreAlwaysReadyWhenCreated() {
	final Chat chat = manager.openChat(uri("other@domain/resource"), null, null);
	assertSame(Chat.Status.ready, chat.getState());
    }

    @Test
    public void shouldBlockChatWhenClosingIt() {
	final Chat chat = manager.openChat(uri("other@domain/resource"), null, null);
	manager.close(chat);
	assertSame(Chat.Status.locked, chat.getState());
    }

    @Test
    public void shouldCloseChatWhenLoggedOut() {
	final Chat chat = manager.openChat(uri("name@domain/resouce"), null, null);
	final MockListener<Status> listener = new MockListener<Status>();
	chat.onStateChanged(listener);
	session.logout();
	MockListener.verifyCalledWith(listener, Status.locked);
    }

    @Test
    public void shouldReuseChatIfNotResouceSpecified() {
	final MockListener<Chat> listener = addOnChatCreatedListener();
	session.receives(new Message(uri("source@domain"), MYSELF, "message 1"));
	session.receives(new Message(uri("source@domain/resource1"), MYSELF, "message 2"));
	MockListener.verifyCalled(listener, 1);
    }

    @Test
    public void shouldEventIncommingMessages() {
	final Chat chat = manager.openChat(uri("someone@domain"), null, null);
	final MockListener<Message> listener = new MockListener<Message>();
	chat.onMessageReceived(listener);
	session.receives("<message type='chat' id='purplee8b92642' to='user@domain' "
		+ "from='someone@domain'><x xmlns='jabber:x:event'/><active"
		+ "xmlns='http://jabber.org/protocol/chatstates'/></message>");
	MockListener.verifyCalled(listener);
    }

    @Test
    public void shouldUseSameRoomWhenAnswering() {
	final MockListener<Chat> listener = addOnChatCreatedListener();
	final Chat chat = manager.openChat(uri("someone@domain"), null, null);
	MockListener.verifyCalledWithSame(listener, chat);
	session.receives(new Message(uri("someone@domain/resource"), MYSELF, "answer").Thread(chat.getThread()));
	MockListener.verifyCalled(listener, 1);
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
