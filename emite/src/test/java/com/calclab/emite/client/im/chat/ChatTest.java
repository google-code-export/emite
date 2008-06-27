package com.calclab.emite.client.im.chat;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.im.chat.Chat.Status;
import com.calclab.emite.client.xmpp.stanzas.Message;

public class ChatTest extends AbstractChatTest {
    private ChatDefault chat;
    private ChatListener listener;

    @Before
    public void beforeTests() {
	chat = new ChatDefault(uri("self@domain/res"), uri("other@domain/otherRes"), "theThread", emite);
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
	final AbstractChat noThreadChat = new ChatDefault(uri("self@domain/res"), uri("other@domain/otherRes"), null,
		emite);
	noThreadChat.setStatus(Status.ready);
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
