package com.calclab.emite.client.xep.chatstate;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.chat.ChatManagerDefault;
import com.calclab.emite.client.xep.chatstate.ChatStateManager.ChatState;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.testing.MockedSession;
import com.calclab.suco.testing.signal.MockSlot;

public class ChatStateManagerTest {
    private static final XmppURI MYSELF = uri("self@domain/res");
    private static final XmppURI OTHER = uri("other@domain/other");

    private ChatManagerDefault chatManager;
    private MockSlot<ChatState> stateListener;
    private Chat chat;
    private ChatStateManager chatStateManager;
    private MockedSession session;

    @Before
    public void beforeTests() {
	session = new MockedSession();
	chatManager = new ChatManagerDefault(session);
	session.setLoggedIn(MYSELF);
	final StateManager stateManager = new StateManager(chatManager);
	chat = chatManager.openChat(OTHER, null, null);
	chatStateManager = stateManager.getChatState(chat);
	stateListener = new MockSlot<ChatState>();
	chatStateManager.onChatStateChanged(stateListener);
    }

    @Test
    public void closeChatWithoutStartConversationMustNotThrowNPE() {
	// This was throwing a NPE:
	chatManager.close(chat);
    }

    @Test
    public void shouldNotRepiteState() {
	session.receives("<message from='other@domain/other' to='self@domain/res' type='chat'>" + "<thread>"
		+ chat.getThread() + "</thread>" + "<active xmlns='http://jabber.org/protocol/chatstates'/></message>");
	chatStateManager.setOwnState(ChatState.composing);
	chatStateManager.setOwnState(ChatState.composing);
	chatStateManager.setOwnState(ChatState.composing);
	session.verifySent("<message from='self@domain/res' to='other@domain/other' type='chat'><thread>"
		+ chat.getThread() + "</thread>"
		+ "<composing xmlns='http://jabber.org/protocol/chatstates'/></message>");
	session.verifyNotSent("<message><composing/><active/></message>");
    }

    @Test
    public void shouldNotSendStateIfNegotiationNotAccepted() {
	chatStateManager.setOwnState(ChatState.composing);
	session.verifySentNothing();
    }

    @Test
    public void shouldSendActiveIfTheOtherStartNegotiation() {
	session.receives("<message from='other@domain/other' to='self@domain/res' type='chat'>" + "<thread>"
		+ chat.getThread() + "</thread>" + "<active xmlns='http://jabber.org/protocol/chatstates'/></message>");
	session.verifySent("<message from='self@domain/res' to='other@domain/other' type='chat'>" + "<thread>"
		+ chat.getThread() + "</thread>" + "<active xmlns='http://jabber.org/protocol/chatstates'/></message>");
    }

    @Test
    public void shouldSendStateIfNegotiationAccepted() {
	session.receives("<message from='other@domain/other' to='self@domain/res' type='chat'>" + "<thread>"
		+ chat.getThread() + "</thread>" + "<active xmlns='http://jabber.org/protocol/chatstates'/></message>");
	chatStateManager.setOwnState(ChatState.composing);
	session.verifySent("<message from='self@domain/res' to='other@domain/other' type='chat'>" + "<thread>"
		+ chat.getThread() + "</thread>"
		+ "<composing xmlns='http://jabber.org/protocol/chatstates'/></message>");
    }

    @Test
    public void shouldSendTwoStateIfDiferent() {
	session.receives("<message from='other@domain/other' to='self@domain/res' type='chat'>" + "<thread>"
		+ chat.getThread() + "</thread>" + "<active xmlns='http://jabber.org/protocol/chatstates'/></message>");
	chatStateManager.setOwnState(ChatState.composing);
	chatStateManager.setOwnState(ChatState.pause);
	session.verifySent("<message from='self@domain/res' to='other@domain/other' type='chat'>" + "<thread>"
		+ chat.getThread() + "</thread>"
		+ "<composing xmlns='http://jabber.org/protocol/chatstates'/></message>"
		+ "<message from='self@domain/res' to='other@domain/other' type='chat'>" + "<thread>"
		+ chat.getThread() + "</thread>" + "<pause xmlns='http://jabber.org/protocol/chatstates'/></message>");
    }

    @Test
    public void shouldStartStateAfterNegotiation() {
	chat.send(new Message("test message"));
	session.receives("<message from='other@domain/other' to='self@domain/res' type='chat'>" + "<thread>"
		+ chat.getThread() + "</thread>" + "<active xmlns='http://jabber.org/protocol/chatstates'/></message>");
	final Message message = new Message(MYSELF, OTHER, "test message").Thread(chat.getThread());
	message.addChild(ChatStateManager.ChatState.active.toString(), ChatStateManager.XMLNS);
	chatStateManager.setOwnState(ChatStateManager.ChatState.composing);
	session.verifySent(message.toString() + "<message from='self@domain/res' to='other@domain/other' type='chat'>"
		+ "<thread>" + chat.getThread() + "</thread>"
		+ "<composing xmlns='http://jabber.org/protocol/chatstates'/></message>");
    }

    @Test
    public void shouldStartStateNegotiation() {
	chat.send(new Message("test message"));
	chat.send(new Message("test message"));
	session.verifySent("<message><active xmlns='http://jabber.org/protocol/chatstates' /></message>");
    }

    @Test
    public void shouldStartStateNegotiationOnce() {
	chat.send(new Message("message1"));
	chat.send(new Message("message2"));
	session.verifySent("<message><body>message1</body><active /></message>");
	session.verifySent("<message><body>message2</body></message>");
	session.verifyNotSent("<message><body>message2</body><active /></message>");
    }

    @Test
    public void shouldStopAfterGone() {
	session.receives("<message from='other@domain/other' to='self@domain/res' type='chat'><active /></message>");
	session.receives("<message from='other@domain/other' to='self@domain/res' type='chat'><gone /></message>");
	chatStateManager.setOwnState(ChatStateManager.ChatState.composing);
	chatStateManager.setOwnState(ChatStateManager.ChatState.pause);
	session.verifySent("<message><active /></message>");
	session.verifyNotSent("<message><composing /></message>");
	session.verifyNotSent("<message><pause /></message>");
	session.verifyNotSent("<message><gone /></message>");
    }

}
