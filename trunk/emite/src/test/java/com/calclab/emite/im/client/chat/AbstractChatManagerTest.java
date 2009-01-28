package com.calclab.emite.im.client.chat;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.testing.MockedSession;
import com.calclab.suco.testing.events.MockedListener;

public abstract class AbstractChatManagerTest {
    protected static final XmppURI MYSELF = uri("self@domain");
    protected static final XmppURI OTHER = uri("other@domain");
    protected ChatManagerImpl manager;
    protected MockedSession session;

    @Before
    public void beforeTests() {
	session = new MockedSession();
	manager = createChatManager();
	session.login(MYSELF, null);
    }

    @Test
    public void shouldEventWhenAChatIsClosed() {
	final Conversation conversation = manager.openChat(uri("other@domain/resource"), null, null);
	final MockedListener<Conversation> listener = new MockedListener<Conversation>();
	manager.onChatClosed(listener);
	manager.close(conversation);
	assertTrue(listener.isCalledOnce());
    }

    @Test
    public void shouldEventWhenChatCreated() {
	final MockedListener<Conversation> listener = new MockedListener<Conversation>();
	manager.onChatCreated(listener);
	manager.openChat(OTHER, null, null);
	assertTrue(listener.isCalledOnce());
    }

    protected abstract ChatManagerImpl createChatManager();
}
