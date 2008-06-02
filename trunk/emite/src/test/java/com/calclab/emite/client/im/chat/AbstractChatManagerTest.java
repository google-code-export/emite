package com.calclab.emite.client.im.chat;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;
import static com.calclab.emite.testing.SlotTester.verifyCalled;
import static com.calclab.emite.testing.SlotTester.verifyCalledWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.im.chat.Chat.Status;
import com.calclab.emite.client.xmpp.session.SessionManager;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.testing.EmiteTestHelper;
import com.calclab.emite.testing.SlotTester;

public abstract class AbstractChatManagerTest {
    protected static final XmppURI MYSELF = uri("self@domain");
    protected EmiteTestHelper emite;
    protected ChatManagerDefault manager;

    @Before
    public void aaCreate() {
	emite = new EmiteTestHelper();
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
	final SlotTester<Status> listener = new SlotTester<Status>();
	chat.onStateChanged(listener);
	emite.receives(SessionManager.Events.onLoggedOut);
	verifyCalledWith(listener, Status.locked);
    }

    @Test
    public void shouldSignalWhenAChatIsClosed() {
	final Chat chat = manager.openChat(uri("other@domain/resource"), null, null);
	final SlotTester<Chat> listener = new SlotTester<Chat>();
	manager.onChatClosed(listener);
	manager.close(chat);
	verifyCalled(listener);
    }

    @Test
    public void shouldSignalWhenChatCreated() {
	final SlotTester<Chat> listener = new SlotTester<Chat>();
	manager.onChatCreated(listener);
	manager.openChat(uri("other@domain"), null, null);
	verifyCalled(listener);
    }

    @Test
    public void shouldUnlockChatsIfLoggedWithSameUserEvenWithDifferentResource() {
	final Chat chat = manager.openChat(uri("other@domain"), null, null);
	manager.logOut();
	assertEquals(Chat.Status.locked, chat.getState());
	manager.logIn(new XmppURI(MYSELF.getNode(), MYSELF.getHost(), "other-resource"));
	assertEquals(Chat.Status.ready, chat.getState());
    }

    protected abstract ChatManagerDefault createChatManager();
}
