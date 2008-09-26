package com.calclab.emite.im.client.chat;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.Chat.State;
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
	final Chat chat = manager.openChat(uri("other@domain/resource"), null, null);
	assertNotNull(chat.getThread());
    }

    @Test
    public void shouldEventWhenAChatIsClosed() {
	final Chat chat = manager.openChat(uri("other@domain/resource"), null, null);
	final MockListener<Chat> listener = new MockListener<Chat>();
	manager.onChatClosed(listener);
	manager.close(chat);
	assertTrue(listener.isCalledOnce());
    }

    @Test
    public void shouldEventWhenChatCreated() {
	final MockListener<Chat> listener = new MockListener<Chat>();
	manager.onChatCreated(listener);
	manager.openChat(uri("other@domain"), null, null);
	assertTrue(listener.isCalledOnce());
    }

    @Test
    public void shouldLockChatsWhenLoggedOut() {
	final Chat chat = manager.openChat(uri("other@domain"), null, null);
	final MockListener<State> listener = new MockListener<State>();
	chat.onStateChanged(listener);
	session.logout();
	assertTrue(listener.isCalledWithEquals(State.locked));
    }

    @Test
    public void shouldUnlockChatsIfLoggedWithSameUserEvenWithDifferentResource() {
	final Chat chat = manager.openChat(uri("other@domain"), null, null);
	session.setState(Session.State.disconnected);
	assertEquals(Chat.State.locked, chat.getState());
	session.login(XmppURI.uri(MYSELF.getNode(), MYSELF.getHost(), "other-resource"), null);
	assertEquals(Chat.State.ready, chat.getState());
    }

    protected abstract ChatManagerImpl createChatManager();
}
