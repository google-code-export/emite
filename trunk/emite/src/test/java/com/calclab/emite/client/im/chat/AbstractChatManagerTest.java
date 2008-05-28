package com.calclab.emite.client.im.chat;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.im.chat.Chat.State;
import com.calclab.emite.client.xmpp.session.SessionManager;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.testing.EmiteTestHelper;
import com.calclab.emite.testing.TestingListener;

public abstract class AbstractChatManagerTest {
    protected static final XmppURI MYSELF = uri("self@domain");
    protected EmiteTestHelper emite;
    protected ChatManagerDefault manager;

    @Before
    public void aaCreate() {
	emite = new EmiteTestHelper();
	manager = createChatManager();
	manager.setUserURI(MYSELF);
    }

    @Test
    public void everyChatOpenedByUserShouldHaveThread() {
	final Chat chat = manager.openChat(uri("other@domain/resource"), null, null);
	assertNotNull(chat.getThread());
    }

    @Test
    public void shouldLockChatsWhenLoggedOut() {
	final Chat chat = manager.openChat(uri("other@domain"), null, null);
	final TestingListener<State> listener = new TestingListener<State>();
	chat.onStateChanged(listener);
	emite.receives(SessionManager.Events.onLoggedOut);
	listener.verify(State.locked);
    }

    @Test
    public void shouldSignalWhenChatCreated() {
	final TestingListener<Chat> listener = new TestingListener<Chat>();
	manager.onChatCreated(listener);
	manager.openChat(uri("other@domain"), null, null);
	listener.verify();
    }

    @Test
    public void shouldUnlockChatsIfLoggedWithSameUser() {
	final Chat chat = manager.openChat(uri("other@domain"), null, null);
	manager.logOut();
	assertEquals(Chat.State.locked, chat.getState());
	manager.logIn(MYSELF);
	assertEquals(Chat.State.ready, chat.getState());
    }

    protected abstract ChatManagerDefault createChatManager();
}
