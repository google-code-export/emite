package com.calclab.emite.xep.chatstate.client;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.chat.Conversation;
import com.calclab.emite.im.client.chat.ChatManagerImpl;
import com.calclab.emite.testing.MockedSession;
import com.calclab.emite.xep.chatstate.client.ChatStateManager.ChatState;
import com.calclab.suco.testing.listener.MockListener;

public class ChatStateManagerTest {
    private static final XmppURI MYSELF = uri("self@domain/res");
    private static final XmppURI OTHER = uri("other@domain/other");

    private ChatManagerImpl chatManager;
    private MockListener<ChatState> stateListener;
    private Conversation conversation;
    private ChatStateManager chatStateManager;
    private MockedSession session;

    @Before
    public void beforeTests() {
	session = new MockedSession();
	chatManager = new ChatManagerImpl(session);
	session.setLoggedIn(MYSELF);
	final StateManager stateManager = new StateManager(chatManager);
	conversation = chatManager.openChat(OTHER, null, null);
	chatStateManager = stateManager.getChatState(conversation);
	stateListener = new MockListener<ChatState>();
	chatStateManager.onChatStateChanged(stateListener);
    }

    @Test
    public void closeChatWithoutStartConversationMustNotThrowNPE() {
	// This was throwing a NPE:
	chatManager.close(conversation);
    }

    @Test
    public void shouldNotRepiteState() {
	session.receives("<message from='other@domain/other' to='self@domain/res' type='chat'>" + "<thread>"
		+ conversation.getThread() + "</thread>" + "<active xmlns='http://jabber.org/protocol/chatstates'/></message>");
	chatStateManager.setOwnState(ChatState.composing);
	chatStateManager.setOwnState(ChatState.composing);
	chatStateManager.setOwnState(ChatState.composing);
	session.verifySent("<message from='self@domain/res' to='other@domain/other' type='chat'><thread>"
		+ conversation.getThread() + "</thread>"
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
		+ conversation.getThread() + "</thread>" + "<active xmlns='http://jabber.org/protocol/chatstates'/></message>");
	session.verifySent("<message from='self@domain/res' to='other@domain/other' type='chat'>" + "<thread>"
		+ conversation.getThread() + "</thread>" + "<active xmlns='http://jabber.org/protocol/chatstates'/></message>");
    }

    @Test
    public void shouldSendStateIfNegotiationAccepted() {
	session.receives("<message from='other@domain/other' to='self@domain/res' type='chat'>" + "<thread>"
		+ conversation.getThread() + "</thread>" + "<active xmlns='http://jabber.org/protocol/chatstates'/></message>");
	chatStateManager.setOwnState(ChatState.composing);
	session.verifySent("<message from='self@domain/res' to='other@domain/other' type='chat'>" + "<thread>"
		+ conversation.getThread() + "</thread>"
		+ "<composing xmlns='http://jabber.org/protocol/chatstates'/></message>");
    }

    @Test
    public void shouldSendTwoStateIfDiferent() {
	session.receives("<message from='other@domain/other' to='self@domain/res' type='chat'>" + "<thread>"
		+ conversation.getThread() + "</thread>" + "<active xmlns='http://jabber.org/protocol/chatstates'/></message>");
	chatStateManager.setOwnState(ChatState.composing);
	chatStateManager.setOwnState(ChatState.pause);
	session.verifySent("<message from='self@domain/res' to='other@domain/other' type='chat'>" + "<thread>"
		+ conversation.getThread() + "</thread>"
		+ "<composing xmlns='http://jabber.org/protocol/chatstates'/></message>"
		+ "<message from='self@domain/res' to='other@domain/other' type='chat'>" + "<thread>"
		+ conversation.getThread() + "</thread>" + "<pause xmlns='http://jabber.org/protocol/chatstates'/></message>");
    }

    @Test
    public void shouldStartStateAfterNegotiation() {
	conversation.send(new Message("test message"));
	session.receives("<message from='other@domain/other' to='self@domain/res' type='chat'>" + "<thread>"
		+ conversation.getThread() + "</thread>" + "<active xmlns='http://jabber.org/protocol/chatstates'/></message>");
	final Message message = new Message(MYSELF, OTHER, "test message").Thread(conversation.getThread());
	message.addChild(ChatStateManager.ChatState.active.toString(), ChatStateManager.XMLNS);
	chatStateManager.setOwnState(ChatStateManager.ChatState.composing);
	session.verifySent(message.toString() + "<message from='self@domain/res' to='other@domain/other' type='chat'>"
		+ "<thread>" + conversation.getThread() + "</thread>"
		+ "<composing xmlns='http://jabber.org/protocol/chatstates'/></message>");
    }

    @Test
    public void shouldStartStateNegotiation() {
	conversation.send(new Message("test message"));
	conversation.send(new Message("test message"));
	session.verifySent("<message><active xmlns='http://jabber.org/protocol/chatstates' /></message>");
    }

    @Test
    public void shouldStartStateNegotiationOnce() {
	conversation.send(new Message("message1"));
	conversation.send(new Message("message2"));
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
