package com.calclab.emite.client.im.chat;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import com.calclab.emite.client.components.Globals;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public class ChatManagerTests {

    private static final String MYSELF = "self@domain";
    private ChatManagerDefault chatManagerDefault;
    private ChatManagerListener listener;

    @Before
    public void a() {
	final Emite emite = Mockito.mock(Emite.class);
	final Globals globals = Mockito.mock(Globals.class);
	Mockito.stub(globals.getOwnURI()).toReturn(XmppURI.parse(MYSELF));
	chatManagerDefault = new ChatManagerDefault(emite, globals);
	listener = Mockito.mock(ChatManagerListener.class);
	chatManagerDefault.addListener(listener);
    }

    @Test
    public void everyChatShouldHaveThread() {
	final Chat chat = chatManagerDefault.newChat(XmppURI.parse("other@domain/resource"));
	assertNotNull(chat.getThread());
    }

    @Test
    public void managerShouldCreateOneChatForSameResource() {
	chatManagerDefault.onReceived(new Message("source@domain/resource1", MYSELF, "message 1"));
	chatManagerDefault.onReceived(new Message("source@domain/resource1", MYSELF, "message 2"));
	Mockito.verify(listener, Mockito.times(1)).onChatCreated((Chat) Mockito.anyObject());
    }

    @Test
    public void managerShouldCreateOneChatIfResourceIsNotAvailable() {
	chatManagerDefault.onReceived(new Message("source@domain", MYSELF, "message 1"));
	chatManagerDefault.onReceived(new Message("source@domain/resource1", MYSELF, "message 2"));
	Mockito.verify(listener, Mockito.times(1)).onChatCreated((Chat) Mockito.anyObject());
    }

    @Test
    public void shouldUseSameRoomWhenAnswering() {
	final Chat chat = chatManagerDefault.newChat(XmppURI.parse("someone@domain"));
	chatManagerDefault
		.onReceived(new Message("someone@domain/resource", MYSELF, "answer").Thread(chat.getThread()));
	Mockito.verify(listener, Mockito.times(1)).onChatCreated(chat);
    }
}
