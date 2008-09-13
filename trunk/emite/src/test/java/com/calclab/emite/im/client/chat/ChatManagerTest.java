package com.calclab.emite.im.client.chat;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.ChatManagerImpl;
import com.calclab.emite.im.client.chat.Chat.Status;
import com.calclab.suco.testing.signal.MockSlot;

public class ChatManagerTest extends AbstractChatManagerTest {

    @Test
    public void managerShouldCreateOneChatForSameResource() {
	final MockSlot<Chat> listener = addOnChatCreatedSlot();
	session.receives(new Message(uri("source@domain/resource1"), MYSELF, "message 1"));
	session.receives(new Message(uri("source@domain/resource1"), MYSELF, "message 2"));
	MockSlot.verifyCalled(listener, 1);
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
	final MockSlot<Status> listener = new MockSlot<Status>();
	chat.onStateChanged(listener);
	session.logout();
	MockSlot.verifyCalledWith(listener, Status.locked);
    }

    @Test
    public void shouldReuseChatIfNotResouceSpecified() {
	final MockSlot<Chat> listener = addOnChatCreatedSlot();
	session.receives(new Message(uri("source@domain"), MYSELF, "message 1"));
	session.receives(new Message(uri("source@domain/resource1"), MYSELF, "message 2"));
	MockSlot.verifyCalled(listener, 1);
    }

    @Test
    public void shouldSignalIncommingMessages() {
	final Chat chat = manager.openChat(uri("someone@domain"), null, null);
	final MockSlot<Message> slot = new MockSlot<Message>();
	chat.onMessageReceived(slot);
	session.receives("<message type='chat' id='purplee8b92642' to='user@domain' "
		+ "from='someone@domain'><x xmlns='jabber:x:event'/><active"
		+ "xmlns='http://jabber.org/protocol/chatstates'/></message>");
	MockSlot.verifyCalled(slot);
    }

    @Test
    public void shouldUseSameRoomWhenAnswering() {
	final MockSlot<Chat> listener = addOnChatCreatedSlot();
	final Chat chat = manager.openChat(uri("someone@domain"), null, null);
	MockSlot.verifyCalledWithSame(listener, chat);
	session.receives(new Message(uri("someone@domain/resource"), MYSELF, "answer").Thread(chat.getThread()));
	MockSlot.verifyCalled(listener, 1);
    }

    @Override
    protected ChatManagerImpl createChatManager() {
	final ChatManagerImpl chatManagerDefault = new ChatManagerImpl(session);
	return chatManagerDefault;
    }

    private MockSlot<Chat> addOnChatCreatedSlot() {
	final MockSlot<Chat> listener = new MockSlot<Chat>();
	manager.onChatCreated(listener);
	return listener;
    }
}
