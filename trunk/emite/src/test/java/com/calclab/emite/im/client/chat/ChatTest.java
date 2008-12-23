package com.calclab.emite.im.client.chat;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.Conversation.State;
import com.calclab.emite.testing.MockedSession;
import com.calclab.suco.testing.events.MockedListener;

public class ChatTest extends AbstractChatTest {
    private static final XmppURI CHAT_URI = uri("other@domain/other");
    private static final XmppURI USER_URI = uri("self@domain/res");
    private Chat chat;
    private MockedSession session;

    @Before
    public void beforeTests() {
	session = new MockedSession(USER_URI);
	chat = new Chat(session, CHAT_URI, "theThread");
    }

    @Override
    public AbstractConversation getChat() {
	return chat;
    }

    @Test
    public void shouldBeReadyIfSessionLogedIn() {
	final Chat aChat = new Chat(session, uri("someone@domain"), null);
	assertEquals(Conversation.State.ready, aChat.getState());
    }

    @Test
    public void shouldLockIfLogout() {
	final MockedListener<State> listener = new MockedListener<State>();
	chat.onStateChanged(listener);
	session.logout();
	session.login(USER_URI, "");
	assertTrue(listener.isCalledWithSame(Conversation.State.locked, Conversation.State.ready));
    }

    @Test
    public void shouldLockIfReLoginWithDifferentJID() {
	session.logout();
	session.login(uri("differentUser@domain"), "");
	assertEquals(Conversation.State.locked, chat.getState());

    }

    @Test
    public void shouldReceiveMessages() {
	final MockedListener<Message> messageReceived = new MockedListener<Message>();
	chat.onMessageReceived(messageReceived);
	session.receives(new Message(CHAT_URI, USER_URI, "the body"));
	assertTrue("should receive messages", messageReceived.isCalled(1));
    }

    @Test
    public void shouldSendNoThreadWhenNotSpecified() {
	final AbstractConversation noThreadChat = new Chat(session, CHAT_URI, null);
	noThreadChat.setState(State.ready);
	noThreadChat.send(new Message("the message"));
	session.verifySent("<message from='self@domain/res' to='other@domain/other' "
		+ "type='chat'><body>the message</body></message>");
    }

    @Test
    public void shouldSendThreadWhenSpecified() {
	chat.send(new Message("the message"));
	session.verifySent("<message from='self@domain/res' to='other@domain/other' type='chat'>"
		+ "<body>the message</body><thread>theThread</thread></message>");
    }

    @Test
    public void shouldSendValidChatMessages() {
	chat.send(new Message(uri("from@uri"), uri("to@uri"), "this is the body").Thread("otherThread").Type(
		Message.Type.groupchat));
	session.verifySent("<message from='self@domain/res' to='other@domain/other' type='chat'>"
		+ "<body>this is the body</body><thread>theThread</thread></message>");
    }

    @Test
    public void shouldUnlockIfReloginWithSameJID() {
	session.logout();
	session.login(XmppURI.uri(USER_URI.getNode(), USER_URI.getHost(), "different_resource"), "");
	assertEquals(Conversation.State.ready, chat.getState());
    }

    @Test
    public void shoultEscapeMessageBody() {
	chat.send(new Message("&"));
	session.verifySent("<message from='self@domain/res' to='other@domain/other' type='chat'>"
		+ "<body>&amp;</body><thread>theThread</thread></message>");
    }

}
