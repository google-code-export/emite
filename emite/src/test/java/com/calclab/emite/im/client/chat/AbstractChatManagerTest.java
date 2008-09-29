package com.calclab.emite.im.client.chat;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.Conversation.State;
import com.calclab.emite.testing.MockedSession;
import com.calclab.suco.testing.listener.MockListener;

public abstract class AbstractChatManagerTest {
    protected static final XmppURI MYSELF = uri("self@domain");
    protected ChatManagerImpl manager;
    protected MockedSession session;

    @Before
    public void beforeTests() {
	session = new MockedSession();
	manager = createChatManager();
	session.login(MYSELF, null);
    }

    @Test
    public void everyChatOpenedByUserShouldHaveThread() {
	final Conversation conversation = manager.openChat(uri("other@domain/resource"), null, null);
	assertNotNull(conversation.getThread());
    }

    @Test
    public void shouldEventWhenAChatIsClosed() {
	final Conversation conversation = manager.openChat(uri("other@domain/resource"), null, null);
	final MockListener<Conversation> listener = new MockListener<Conversation>();
	manager.onChatClosed(listener);
	manager.close(conversation);
	assertTrue(listener.isCalledOnce());
    }

    @Test
    public void shouldEventWhenChatCreated() {
	final MockListener<Conversation> listener = new MockListener<Conversation>();
	manager.onChatCreated(listener);
	manager.openChat(uri("other@domain"), null, null);
	assertTrue(listener.isCalledOnce());
    }

    @Test
    public void shouldLockChatsWhenLoggedOut() {
	final Conversation conversation = manager.openChat(uri("other@domain"), null, null);
	final MockListener<State> listener = new MockListener<State>();
	conversation.onStateChanged(listener);
	session.logout();
	assertTrue(listener.isCalledWithEquals(State.locked));
    }

    @Test
    public void shouldUnlockChatsIfLoggedWithSameUserEvenWithDifferentResource() {
	final Conversation conversation = manager.openChat(uri("other@domain"), null, null);
	session.setState(Session.State.disconnected);
	assertEquals(Conversation.State.locked, conversation.getState());
	session.login(XmppURI.uri(MYSELF.getNode(), MYSELF.getHost(), "other-resource"), null);
	assertEquals(Conversation.State.ready, conversation.getState());
    }

    protected abstract ChatManagerImpl createChatManager();
}
