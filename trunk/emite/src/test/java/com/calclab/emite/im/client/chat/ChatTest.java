package com.calclab.emite.im.client.chat;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.im.client.chat.Conversation.State;
import com.calclab.emite.testing.MockedSession;
import com.calclab.suco.testing.listener.MockListener;

public class ChatTest extends AbstractChatTest {
    private ChatImpl chat;
    private MockedSession session;

    @Before
    public void beforeTests() {
	session = new MockedSession("self@domain/res");
	chat = new ChatImpl(session, uri("other@domain/other"), "theThread");
    }

    @Override
    public AbstractChat getChat() {
	return chat;
    }

    @Test
    public void shouldSendNoThreadWhenNotSpecified() {
	final AbstractChat noThreadChat = new ChatImpl(session, uri("other@domain/other"), null);
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
    public void shouldSetStateUsingSessionState() {
	final ChatImpl aChat = new ChatImpl(session, uri("someone@domain"), null);
	final MockListener<State> listener = new MockListener<State>();
	aChat.onStateChanged(listener);

	assertEquals(Conversation.State.ready, aChat.getState());
	session.logout();
	session.login(uri("self@domain/res"), "");
	assertTrue(listener.isCalledWithSame(Conversation.State.locked, Conversation.State.ready));
	listener.clear();
	session.logout();
	session.login(uri("differentUser@domain"), "");
	assertTrue(listener.isCalledWithSame(Conversation.State.locked));
    }

    @Test
    public void shoultEscapeMessageBody() {
	chat.send(new Message("&"));
	session.verifySent("<message from='self@domain/res' to='other@domain/other' type='chat'>"
		+ "<body>&amp;</body><thread>theThread</thread></message>");
    }

}
