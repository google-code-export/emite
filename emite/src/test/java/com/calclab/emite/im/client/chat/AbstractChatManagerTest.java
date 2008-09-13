package com.calclab.emite.im.client.chat;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.Chat;
import com.calclab.emite.im.client.chat.ChatManagerImpl;
import com.calclab.emite.im.client.chat.Chat.Status;
import com.calclab.emite.testing.MockedSession;
import com.calclab.suco.testing.signal.MockSlot;

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
	final MockSlot<Status> listener = new MockSlot<Status>();
	chat.onStateChanged(listener);
	session.logout();
	MockSlot.verifyCalledWith(listener, Status.locked);
    }

    @Test
    public void shouldSignalWhenAChatIsClosed() {
	final Chat chat = manager.openChat(uri("other@domain/resource"), null, null);
	final MockSlot<Chat> listener = new MockSlot<Chat>();
	manager.onChatClosed(listener);
	manager.close(chat);
	MockSlot.verifyCalled(listener);
    }

    @Test
    public void shouldSignalWhenChatCreated() {
	final MockSlot<Chat> listener = new MockSlot<Chat>();
	manager.onChatCreated(listener);
	manager.openChat(uri("other@domain"), null, null);
	MockSlot.verifyCalled(listener);
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
