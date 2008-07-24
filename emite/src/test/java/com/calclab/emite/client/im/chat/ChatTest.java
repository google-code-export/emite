package com.calclab.emite.client.im.chat;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.im.chat.Chat.Status;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.testing.MockedSession;

public class ChatTest extends AbstractChatTest {
    private ChatDefault chat;
    private ChatListener listener;
    private MockedSession session;

    @Before
    public void beforeTests() {
	session = new MockedSession("self@domain/res");
	chat = new ChatDefault(session, uri("other@domain/otherRes"), "theThread");
	chat.setStatus(Status.ready);
	listener = mock(ChatListener.class);
	chat.addListener(listener);
    }

    @Override
    public AbstractChat getChat() {
	return chat;
    }

    @Test
    public void shouldSendNoThreadWhenNotSpecified() {
	final AbstractChat noThreadChat = new ChatDefault(session, uri("other@domain/otherRes"), null);
	noThreadChat.setStatus(Status.ready);
	noThreadChat.send(new Message("the message"));
	session.verifySent("<message from='self@domain/res' to='other@domain/otherRes' "
		+ "type='chat'><body>the message</body></message>");
    }

    @Test
    public void shouldSendThreadWhenSpecified() {
	chat.send(new Message("the message"));
	session.verifySent("<message from='self@domain/res' to='other@domain/otherRes' type='chat'>"
		+ "<body>the message</body><thread>theThread</thread></message>");
    }

    @Test
    public void shouldSendValidChatMessages() {
	chat.send(new Message(uri("from@uri"), uri("to@uri"), "this is the body").Thread("otherThread").Type(
		Message.Type.groupchat));
	session.verifySent("<message from='self@domain/res' to='other@domain/otherRes' type='chat'>"
		+ "<body>this is the body</body><thread>theThread</thread></message>");
    }

    @Test
    public void shoultEscapeMessageBody() {
	chat.send(new Message("&"));
	session.verifySent("<message from='self@domain/res' to='other@domain/otherRes' type='chat'>"
		+ "<body>&amp;</body><thread>theThread</thread></message>");
    }

}
