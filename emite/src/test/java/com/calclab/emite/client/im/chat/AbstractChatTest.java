package com.calclab.emite.client.im.chat;

import org.junit.Test;

import com.calclab.emite.client.im.chat.Chat.Status;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.testing.EmiteTestHelper;
import com.calclab.emite.testing.MockSlot;

public abstract class AbstractChatTest {

    protected final EmiteTestHelper emite;

    public AbstractChatTest() {
	this.emite = new EmiteTestHelper();
    }

    public abstract AbstractChat getChat();

    @Test
    public void shouldInterceptIncomingMessages() {
	final AbstractChat chat = getChat();
	final MockSlot<Message> interceptor = new MockSlot<Message>();
	chat.onBeforeReceive(interceptor);
	final Message message = new Message("body");
	chat.receive(message);
	MockSlot.verifyCalledWithSame(interceptor, message);
    }

    @Test
    public void shouldInterceptOutcomingMessages() {
	final AbstractChat chat = getChat();
	final MockSlot<Message> interceptor = new MockSlot<Message>();
	chat.onBeforeSend(interceptor);
	final Message message = new Message("body");
	chat.send(message);
	MockSlot.verifyCalledWithSame(interceptor, message);
    }

    @Test
    public void shouldNotSendMessagesWhenStatusIsNotReady() {
	final AbstractChat chat = getChat();
	chat.setStatus(Status.locked);
	chat.send(new Message("a message"));
	emite.verifyNotSent("<message />");
    }

    @Test
    public void shouldSetNullData() {
	getChat().setData(null, null);
    }
}
