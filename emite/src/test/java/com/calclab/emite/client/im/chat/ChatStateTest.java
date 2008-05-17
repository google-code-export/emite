package com.calclab.emite.client.im.chat;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.extra.chatstate.ChatState;
import com.calclab.emite.testing.EmiteTestHelper;

public class ChatStateTest {
    private EmiteTestHelper emite;
    private Chat chat;
    private ChatListener listener;
    private ChatState chatState;

    @Before
    public void aaCreate() {
        this.emite = new EmiteTestHelper();
        chat = new ChatDefault(uri("other@domain/otherRes"), uri("self@domain/res"), "theThread", emite);
        chatState = new ChatState(chat, emite);
        listener = mock(ChatListener.class);
        chat.addListener(listener);
    }

    @Test
    public void shouldSendActive() {
        chatState.setOwnState(ChatState.Type.active);
        emite.verifySent("<message from='self@domain/res' to='other@domain/otherRes' type='chat'>"
                + "<thread>theThread</thread>" + "<active xmlns='http://jabber.org/protocol/chatstates'/></message>");
    }

    @Test
    public void shouldSendComposing() {
        chatState.setOwnState(ChatState.Type.composing);
        emite
                .verifySent("<message from='self@domain/res' to='other@domain/otherRes' type='chat'>"
                        + "<thread>theThread</thread>"
                        + "<composing xmlns='http://jabber.org/protocol/chatstates'/></message>");
    }

    @Test
    public void shouldSendComposingAndPause() {
        chatState.setOwnState(ChatState.Type.composing);
        chatState.setOwnState(ChatState.Type.pause);
        emite.verifySent("<message from='self@domain/res' to='other@domain/otherRes' type='chat'>"
                + "<thread>theThread</thread>" + "<composing xmlns='http://jabber.org/protocol/chatstates'/></message>"
                + "<message from='self@domain/res' to='other@domain/otherRes' type='chat'>"
                + "<thread>theThread</thread>" + "<pause xmlns='http://jabber.org/protocol/chatstates'/></message>");
    }

    @Test
    public void shouldSendComposingOnce() {
        chatState.setOwnState(ChatState.Type.composing);
        chatState.setOwnState(ChatState.Type.composing);
        chatState.setOwnState(ChatState.Type.composing);
        emite
                .verifySent("<message from='self@domain/res' to='other@domain/otherRes' type='chat'>"
                        + "<thread>theThread</thread>"
                        + "<composing xmlns='http://jabber.org/protocol/chatstates'/></message>");
    }

    @Test
    public void shouldSendComposingWithoutThread() {
        ChatDefault chatWithoutThread = new ChatDefault(uri("other@domain/otherRes"), uri("self@domain/res"), null,
                emite);
        chatState = new ChatState(chatWithoutThread, emite);
        chatState.setOwnState(ChatState.Type.composing);
        emite.verifySent("<message from='self@domain/res' to='other@domain/otherRes' type='chat'>"
                + "<composing xmlns='http://jabber.org/protocol/chatstates'/></message>");
    }

    @Test
    public void shouldSendGone() {
        chatState.setOwnState(ChatState.Type.gone);
        emite.verifySent("<message from='self@domain/res' to='other@domain/otherRes' type='chat'>"
                + "<thread>theThread</thread>" + "<gone xmlns='http://jabber.org/protocol/chatstates'/></message>");
    }

    @Test
    public void shouldSendInactive() {
        chatState.setOwnState(ChatState.Type.inactive);
        emite.verifySent("<message from='self@domain/res' to='other@domain/otherRes' type='chat'>"
                + "<thread>theThread</thread>" + "<inactive xmlns='http://jabber.org/protocol/chatstates'/></message>");
    }

    @Test
    public void shouldSendPause() {
        chatState.setOwnState(ChatState.Type.pause);
        emite.verifySent("<message from='self@domain/res' to='other@domain/otherRes' type='chat'>"
                + "<thread>theThread</thread>" + "<pause xmlns='http://jabber.org/protocol/chatstates'/></message>");
    }

}
