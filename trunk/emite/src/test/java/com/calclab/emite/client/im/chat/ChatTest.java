package com.calclab.emite.client.im.chat;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.testing.EmiteStub;
import static com.calclab.emite.client.xmpp.stanzas.XmppURI.*;

public class ChatTest {
    private EmiteStub emite;

    @Before
    public void aaCreate() {
	this.emite = new EmiteStub();
    }

    @Test
    public void shouldSendNoThreadWhenNotSpecified() {
	final ChatDefault chat = new ChatDefault(uri("other@domain/otherRes"), uri("self@domain/res"), null, emite);
	chat.send("the message");
	emite.verifySent("<message from='self@domain/res' to='other@domain/otherRes' "
		+ "type='chat' xmlns='jabber:client'><body>the message</body></message>");
    }

    @Test
    public void shouldSendThreadWhenSpecified() {
	final ChatDefault chat = new ChatDefault(uri("other@domain/otherRes"), uri("self@domain/res"), "theThread",
		emite);
	chat.send("the message");
	emite.verifySent("<message from='self@domain/res' to='other@domain/otherRes' type='chat'"
		+ " xmlns='jabber:client'><body>the message</body><thread>theThread</thread></message>");
    }
}
