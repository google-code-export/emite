package com.calclab.emite.client.xep.chatstate;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.chat.ChatManagerDefault;
import com.calclab.emite.client.xep.chatstate.ChatState;
import com.calclab.emite.client.xep.chatstate.ChatStateListener;
import com.calclab.emite.client.xep.chatstate.ChatStateManager;
import com.calclab.emite.client.xep.chatstate.ChatState.Type;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.testing.EmiteTestHelper;

public class ChatStateManagerTest {
    private static final XmppURI MYSELF = uri("self@domain/res");
    private static final XmppURI OTHER = uri("other@domain/otherRes");

    private EmiteTestHelper emite;
    private ChatManagerDefault chatManager;
    private ChatStateManager chatStateManager;
    private ChatStateListener stateListener;

    private Chat chat;

    private ChatState chatState;

    @Before
    public void aaCreate() {
        emite = new EmiteTestHelper();
        chatManager = new ChatManagerDefault(emite);
        chatStateManager = new ChatStateManager(emite, chatManager);
        chatManager.setUserURI(MYSELF.toString());
        stateListener = Mockito.mock(ChatStateListener.class);
        chat = chatManager.openChat(OTHER, null, null);
        chatState = chatStateManager.getChatState(chat);
        chatState.addOtherStateListener(stateListener);
    }

    @Test
    public void shouldFireGone() {
        emite.receives("<message from='other@domain/otherRes' to='self@domain/res' type='chat'>" + "<thread>"
                + chat.getThread() + "</thread>" + "<gone xmlns='http://jabber.org/protocol/chatstates'/></message>");
        Mockito.verify(stateListener).onGone();
    }

    @Test
    public void shouldFireOtherCompossing() {
        emite.receives("<message from='other@domain/otherRes' to='self@domain/res' type='chat'><thread>"
                + chat.getThread() + "</thread><composing xmlns='http://jabber.org/protocol/chatstates'/></message>");
        Mockito.verify(stateListener).onComposing();
    }

    @Test
    public void shouldFireOtherCompossingAsGmailDo() {
        emite
                .receives("<message to='self@domain/res' type='chat' from='other@domain/otherRes'><cha:composing xmlns:cha='http://jabber.org/protocol/chatstates'></cha:composing><nos:x xmlns:nos='google:nosave' value='disabled'></nos:x><arc:record xmlns:arc='http://jabber.org/protocol/archive' otr='false'></arc:record></message>");
        Mockito.verify(stateListener).onComposing();
    }

    @Test
    public void shouldFireOtherCompossingToWithoutResource() {
        emite.receives("<message from='other@domain/otherRes' to='self@domain' type='chat'>" + "<thread>"
                + chat.getThread() + "</thread>"
                + "<composing xmlns='http://jabber.org/protocol/chatstates'/></message>");
        Mockito.verify(stateListener).onComposing();
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
        chat.send("test message");
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
        chat.send("test message");
        // emite.verifySent("<message from='self@domain/res'
        // to='other@domain/otherRes' type='chat'>"
        // + "<body>test message</body>" + "<thread>" + chat.getThread() +
        // "</thread>"
        // + "<active
        // xmlns='http://jabber.org/protocol/chatstates'/></message>");
        chat.send("test message");
        emite.verifySent("<message><active xmlns='http://jabber.org/protocol/chatstates' /></message>");
    }

    @Test
    public void shouldStartStateNegotiationOnce() {
        chat.send("message1");
        chat.send("message2");
        emite.verifySent("<message><body>message1</body><active /></message>");
        emite.verifySent("<message><body>message2</body></message>");
        emite.verifyNotSent("<message><body>message2</body><active /></message>");
    }

}
