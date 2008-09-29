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
import com.calclab.emite.im.client.chat.Conversation;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.suco.client.listener.Listener;
import com.calclab.suco.testing.listener.EventTester;

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
	final Conversation conversation = createMockChat("user@domain/resource");
	controller.setChatJID("user@domain");
	mockOnChatCreated().fire(conversation);
	verify(conversation, times(1)).onMessageReceived((Listener<Message>) anyObject());
    }

    @Test
    public void shouldNotAttachToAnyChat() {
	final Conversation conversation = createMockChat("admin@domain");
	controller.setChatJID("user@domain");
	mockOnChatCreated().fire(conversation);
	verify(conversation, times(0)).onMessageReceived((Listener<Message>) anyObject());
    }

    private Conversation createMockChat(final String chatURI) {
	final Conversation conversation = mock(Conversation.class);
	stub(conversation.getOtherURI()).toReturn(uri(chatURI));
	return conversation;
    }

    private EventTester<Conversation> mockOnChatCreated() {
	final EventTester<Conversation> tester = new EventTester<Conversation>();
	tester.mock(manager).onChatCreated(tester.getListener());
	return tester;
    }

}
