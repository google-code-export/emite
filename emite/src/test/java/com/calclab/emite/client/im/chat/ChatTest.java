package com.calclab.emite.client.im.chat;

import org.junit.Before;
import static org.mockito.Mockito.*;
import org.junit.Test;

import com.calclab.emite.testing.EmiteStub;
import static com.calclab.emite.client.xmpp.stanzas.XmppURI.*;

public class ChatTest {
    private EmiteStub emite;
    private ChatDefault chat;
    private ChatListener listener;

    @Before
    public void aaCreate() {
	this.emite = new EmiteStub();
	chat = new ChatDefault(uri("other@domain/otherRes"), uri("self@domain/res"), "theThread", emite);
	listener = mock(ChatListener.class);
	chat.addListener(listener);
    }

    @Test
    public void shouldSendNoThreadWhenNotSpecified() {
	final ChatDefault noThreadChat = new ChatDefault(uri("other@domain/otherRes"), uri("self@domain/res"), null,
		emite);
	noThreadChat.send("the message");
	emite.verifySent("<message from='self@domain/res' to='other@domain/otherRes' "
		+ "type='chat' xmlns='jabber:client'><body>the message</body></message>");
    }

    @Test
    public void shouldSendThreadWhenSpecified() {

	chat.send("the message");
	emite.verifySent("<message from='self@domain/res' to='other@domain/otherRes' type='chat'"
		+ " xmlns='jabber:client'><body>the message</body><thread>theThread</thread></message>");
    }
}
