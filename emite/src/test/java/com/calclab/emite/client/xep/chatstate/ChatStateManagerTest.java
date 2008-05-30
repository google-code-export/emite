package com.calclab.emite.client.xep.chatstate;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.chat.ChatManagerDefault;
import com.calclab.emite.client.xep.chatstate.ChatState.Type;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.testing.EmiteTestHelper;

public class ChatStateManagerTest {
    private static final XmppURI MYSELF = uri("self@domain/res");
    private static final XmppURI OTHER = uri("other@domain/otherRes");

    private EmiteTestHelper emite;
    private ChatManagerDefault chatManager;
    private ChatStateListener stateListener;
    private Chat chat;
    private ChatState chatState;

    @Before
    public void aaCreate() {

        emite = new EmiteTestHelper();
        chatManager = new ChatManagerDefault(emite);
        chatManager.logIn(MYSELF);
        final ChatStateManager chatStateManager = new ChatStateManager(chatManager);
        stateListener = Mockito.mock(ChatStateListener.class);
        chat = chatManager.openChat(OTHER, null, null);
        chatState = chatStateManager.getChatState(chat);
        chatState.addOtherStateListener(stateListener);
    }

    @Test
    public void closeChatWithoutStartConversationMustNotThrowNPE() {
        // This was throwing a NPE:
        chatManager.close(chat);
    }

    @Test
    public void shouldNotRepiteState() {
        emite.receives("<message from='other@domain/otherRes' to='self@domain/res' type='chat'>" + "<thread>"
                + chat.getThread() + "</thread>" + "<active xmlns='http://jabber.org/protocol/chatstates'/></message>");
        chatState.setOwnState(Type.composing);
        chatState.setOwnState(Type.composing);
        chatState.setOwnState(Type.composing);
        emite.verifySent("<message from='self@domain/res' to='other@domain/otherRes' type='chat'>" + "<thread>"
                + chat.getThread() + "</thread>"
                + "<composing xmlns='http://jabber.org/protocol/chatstates'/></message>");
        emite.verifyNotSent("<message><composing/><active/></message>");
    }

    @Test
    public void shouldNotSendStateIfNegotiationNotAccepted() {
        chatState.setOwnState(Type.composing);
        emite.verifyNothingSent();
    }

    @Test
    public void shouldSendActiveIfTheOtherStartNegotiation() {
        emite.receives("<message from='other@domain/otherRes' to='self@domain/res' type='chat'>" + "<thread>"
                + chat.getThread() + "</thread>" + "<active xmlns='http://jabber.org/protocol/chatstates'/></message>");
        emite.verifySent("<message from='self@domain/res' to='other@domain/otherRes' type='chat'>" + "<thread>"
                + chat.getThread() + "</thread>" + "<active xmlns='http://jabber.org/protocol/chatstates'/></message>");
    }

    @Test
    public void shouldSendStateIfNegotiationAccepted() {
        emite.receives("<message from='other@domain/otherRes' to='self@domain/res' type='chat'>" + "<thread>"
                + chat.getThread() + "</thread>" + "<active xmlns='http://jabber.org/protocol/chatstates'/></message>");
        chatState.setOwnState(Type.composing);
        emite.verifySent("<message from='self@domain/res' to='other@domain/otherRes' type='chat'>" + "<thread>"
                + chat.getThread() + "</thread>"
                + "<composing xmlns='http://jabber.org/protocol/chatstates'/></message>");
    }

    @Test
    public void shouldSendTwoStateIfDiferent() {
        emite.receives("<message from='other@domain/otherRes' to='self@domain/res' type='chat'>" + "<thread>"
                + chat.getThread() + "</thread>" + "<active xmlns='http://jabber.org/protocol/chatstates'/></message>");
        chatState.setOwnState(Type.composing);
        chatState.setOwnState(Type.pause);
        emite.verifySent("<message from='self@domain/res' to='other@domain/otherRes' type='chat'>" + "<thread>"
                + chat.getThread() + "</thread>"
                + "<composing xmlns='http://jabber.org/protocol/chatstates'/></message>"
                + "<message from='self@domain/res' to='other@domain/otherRes' type='chat'>" + "<thread>"
                + chat.getThread() + "</thread>" + "<pause xmlns='http://jabber.org/protocol/chatstates'/></message>");
    }

    @Test
    public void shouldStartStateAfterNegotiation() {
        chat.send(new Message("test message"));
        emite.receives("<message from='other@domain/otherRes' to='self@domain/res' type='chat'>" + "<thread>"
                + chat.getThread() + "</thread>" + "<active xmlns='http://jabber.org/protocol/chatstates'/></message>");
        final Message message = new Message(MYSELF, OTHER, "test message").Thread(chat.getThread());
        message.addChild(ChatState.Type.active.toString(), ChatState.XMLNS);
        chatState.setOwnState(ChatState.Type.composing);
        emite.verifySent(message.toString() + "<message from='self@domain/res' to='other@domain/otherRes' type='chat'>"
                + "<thread>" + chat.getThread() + "</thread>"
                + "<composing xmlns='http://jabber.org/protocol/chatstates'/></message>");
    }

    @Test
    public void shouldStartStateNegotiation() {
        chat.send(new Message("test message"));
        chat.send(new Message("test message"));
        emite.verifySent("<message><active xmlns='http://jabber.org/protocol/chatstates' /></message>");
    }

    @Test
    public void shouldStartStateNegotiationOnce() {
        chat.send(new Message("message1"));
        chat.send(new Message("message2"));
        emite.verifySent("<message><body>message1</body><active /></message>");
        emite.verifySent("<message><body>message2</body></message>");
        emite.verifyNotSent("<message><body>message2</body><active /></message>");
    }

    @Test
    public void shouldStopAfterGone() {
        emite.receives("<message from='other@domain/otherRes' to='self@domain/res' type='chat'><active /></message>");
        emite.receives("<message from='other@domain/otherRes' to='self@domain/res' type='chat'><gone /></message>");
        chatState.setOwnState(ChatState.Type.composing);
        chatState.setOwnState(ChatState.Type.pause);
        emite.verifySent("<message><active /></message>");
        emite.verifyNotSent("<message><composing /></message>");
        emite.verifyNotSent("<message><pause /></message>");
        emite.verifyNotSent("<message><gone /></message>");
    }

}
