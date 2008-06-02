package com.calclab.emite.client.im.chat;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.testing.EmiteTestHelper;

public class ChatTest {
    private EmiteTestHelper emite;
    private ChatDefault chat;
    private ChatListener listener;

    @Before
    public void beforeTests() {
	this.emite = new EmiteTestHelper();
	chat = new ChatDefault(uri("self@domain/res"), uri("other@domain/otherRes"), "theThread", emite);
	listener = mock(ChatListener.class);
	chat.addListener(listener);
    }

    @Test
    public void shouldInterceptIncomingMessages() {
	final MessageInterceptor interceptor = mock(MessageInterceptor.class);
	chat.addMessageInterceptor(interceptor);
	final Message message = new Message();
	chat.receive(message);
	verify(interceptor).onBeforeReceive(message);
    }

    @Test
    public void shouldInterceptOutcomingMessages() {
	final MessageInterceptor interceptor = mock(MessageInterceptor.class);
	chat.addMessageInterceptor(interceptor);
	final Message message = new Message();
	chat.send(message);
	verify(interceptor).onBeforeSend(same(message));
    }

    @Test
    public void shouldSendNoThreadWhenNotSpecified() {
	final AbstractChat noThreadChat = new ChatDefault(uri("self@domain/res"), uri("other@domain/otherRes"), null,
		emite);
	noThreadChat.send(new Message("the message"));
	emite.verifySent("<message from='self@domain/res' to='other@domain/otherRes' "
		+ "type='chat'><body>the message</body></message>");
    }

    @Test
    public void shouldSendThreadWhenSpecified() {
	chat.send(new Message("the message"));
	emite.verifySent("<message from='self@domain/res' to='other@domain/otherRes' type='chat'>"
		+ "<body>the message</body><thread>theThread</thread></message>");
    }

    @Test
    public void shouldSendValidChatMessages() {
	chat.send(new Message(uri("from@uri"), uri("to@uri"), "this is the body").Thread("otherThread").Type(
		Message.Type.groupchat));
	emite.verifySent("<message from='self@domain/res' to='other@domain/otherRes' type='chat'>"
		+ "<body>this is the body</body><thread>theThread</thread></message>");
    }

    @Test
    public void shoultEscapeMessageBody() {
	chat.send(new Message("&"));
	emite.verifySent("<message from='self@domain/res' to='other@domain/otherRes' type='chat'>"
		+ "<body>&amp;</body><thread>theThread</thread></message>");
    }
}
