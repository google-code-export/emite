package com.calclab.emite.client.im.chat;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;
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
    protected ChatManager manager;

    @Before
    public void aaCreate() {
	emite = new EmiteTestHelper();
	manager = createChatManager();
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

    protected abstract ChatManager createChatManager();
}
