package com.calclab.emite.im.client.chat;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.im.client.chat.Conversation.State;
import com.calclab.emite.xep.chatstate.client.ChatStateManager;
import com.calclab.suco.testing.events.MockedListener;

public class ChatManagerTest extends AbstractChatManagerTest {

    @Test
    public void chatStateDontFireOnChatCreatedButMustAfterOpenChat() {
	final Message message = new Message(OTHER, MYSELF, null);
	message.addChild("gone", ChatStateManager.XMLNS);

	final MockedListener<Conversation> listener = addOnChatCreatedListener();
	session.receives(message);
	assertTrue(listener.isNotCalled());
	manager.openChat(OTHER);
	assertTrue(listener.isCalled());
    }

    @Test
    public void managerShouldCreateOneChatForSameResource() {
	final MockedListener<Conversation> listener = addOnChatCreatedListener();
	session.receives(new Message(uri("source@domain/resource1"), MYSELF, "message 1"));
	session.receives(new Message(uri("source@domain/resource1"), MYSELF, "message 2"));
	assertTrue(listener.isCalledOnce());
    }

    @Test
    public void oneToOneChatsAreAlwaysReadyWhenCreated() {
	final Conversation conversation = manager.openChat(uri("other@domain/resource"));
	assertSame(Conversation.State.ready, conversation.getState());
    }

    @Test
    public void roomInvitationsShouldDontFireOnChatCreated() {
	final MockedListener<Conversation> listener = addOnChatCreatedListener();
	session.receives("<message to='" + MYSELF
		+ "' from='someroom@domain'><x xmlns='http://jabber.org/protocol/muc#user'>" + "<invite from='" + OTHER
		+ "'><reason>Join to our conversation</reason></invite>"
		+ "</x><x jid='someroom@domain' xmlns='jabber:x:conference' /></message>");
	assertTrue(listener.isNotCalled());
    }

    @Test
    public void roomInvitationsShouldDontFireOnChatCreatedButMustAfterOpenChat() {
	final MockedListener<Conversation> listener = addOnChatCreatedListener();
	session.receives("<message to='" + MYSELF
		+ "' from='someroom@domain'><x xmlns='http://jabber.org/protocol/muc#user'>" + "<invite from='" + OTHER
		+ "'><reason>Join to our conversation</reason></invite>"
		+ "</x><x jid='someroom@domain' xmlns='jabber:x:conference' /></message>");
	assertTrue(listener.isNotCalled());
	manager.openChat(OTHER);
	assertTrue(listener.isCalled());
    }

    @Test
    public void shouldBlockChatWhenClosingIt() {
	final Conversation conversation = manager.openChat(uri("other@domain/resource"));
	manager.close(conversation);
	assertSame(Conversation.State.locked, conversation.getState());
    }

    @Test
    public void shouldCloseChatWhenLoggedOut() {
	final Conversation conversation = manager.openChat(uri("name@domain/resouce"));
	final MockedListener<State> listener = new MockedListener<State>();
	conversation.onStateChanged(listener);
	session.logout();
	assertTrue(listener.isCalledWithEquals(State.locked));
    }

    @Test
    public void shouldEventIncommingMessages() {
	final Conversation conversation = manager.openChat(uri("someone@domain"));
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
	assertTrue(listener.isCalled(1));
    }

    @Test
    public void shouldUseSameRoomWhenAnswering() {
	final MockedListener<Conversation> listener = addOnChatCreatedListener();
	final Conversation conversation = manager.openChat(uri("someone@domain"));
	assertTrue(listener.isCalledOnce());
	assertTrue(listener.isCalledWithSame(conversation));
	session.receives(new Message(uri("someone@domain/resource"), MYSELF, "answer"));
	assertTrue(listener.isCalled(1));
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
