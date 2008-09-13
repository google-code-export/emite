package com.calclab.emite.widgets.client.chat;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.suco.client.signal.Slot;
import com.calclab.suco.testing.signal.SignalTester;

@SuppressWarnings("unchecked")
public class ChatControllerTest extends AbstractChatControllerTest {
    private Session session;
    private ChatManager manager;
    private ChatController controller;
    private ChatWidget widget;

    @Before
    public void beforeAbstractChatTests() {
	session = mock(Session.class);
	manager = mock(ChatManager.class);
	controller = new ChatController(session, manager);
	widget = mock(ChatWidget.class);
	controller.setWidget(widget);
    }

    @Test
    public void shouldAttacheToOwnChat() {
	final Chat chat = createMockChat("user@domain/resource");
	controller.setChatJID("user@domain");
	mockOnChatCreated().fire(chat);
	verify(chat, times(1)).onMessageReceived((Slot<Message>) anyObject());
    }

    @Test
    public void shouldNotAttachToAnyChat() {
	final Chat chat = createMockChat("admin@domain");
	controller.setChatJID("user@domain");
	mockOnChatCreated().fire(chat);
	verify(chat, times(0)).onMessageReceived((Slot<Message>) anyObject());
    }

    private Chat createMockChat(final String chatURI) {
	final Chat chat = mock(Chat.class);
	stub(chat.getOtherURI()).toReturn(uri(chatURI));
	return chat;
    }

    private SignalTester<Chat> mockOnChatCreated() {
	final SignalTester<Chat> tester = new SignalTester<Chat>();
	tester.mock(manager).onChatCreated(tester.getSlot());
	return tester;
    }

}
