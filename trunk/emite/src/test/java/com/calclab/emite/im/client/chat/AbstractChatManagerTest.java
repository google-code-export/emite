package com.calclab.emite.im.client.chat;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.Chat.Status;
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
	manager.logIn(MYSELF);
    }

    @Test
    public void everyChatOpenedByUserShouldHaveThread() {
	final Chat chat = manager.openChat(uri("other@domain/resource"), null, null);
	assertNotNull(chat.getThread());
    }

    @Test
    public void shouldLockChatsWhenLoggedOut() {
	final Chat chat = manager.openChat(uri("other@domain"), null, null);
	final MockListener<Status> listener = new MockListener<Status>();
	chat.onStateChanged(listener);
	session.logout();
	MockListener.verifyCalledWith(listener, Status.locked);
    }

    @Test
    public void shouldEventWhenAChatIsClosed() {
	final Chat chat = manager.openChat(uri("other@domain/resource"), null, null);
	final MockListener<Chat> listener = new MockListener<Chat>();
	manager.onChatClosed(listener);
	manager.close(chat);
	MockListener.verifyCalled(listener);
    }

    @Test
    public void shouldEventWhenChatCreated() {
	final MockListener<Chat> listener = new MockListener<Chat>();
	manager.onChatCreated(listener);
	manager.openChat(uri("other@domain"), null, null);
	MockListener.verifyCalled(listener);
    }

    @Test
    public void shouldUnlockChatsIfLoggedWithSameUserEvenWithDifferentResource() {
	final Chat chat = manager.openChat(uri("other@domain"), null, null);
	manager.logOut();
	assertEquals(Chat.Status.locked, chat.getState());
	manager.logIn(XmppURI.uri(MYSELF.getNode(), MYSELF.getHost(), "other-resource"));
	assertEquals(Chat.Status.ready, chat.getState());
    }

    protected abstract ChatManagerImpl createChatManager();
}
