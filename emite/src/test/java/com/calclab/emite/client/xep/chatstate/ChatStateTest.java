package com.calclab.emite.client.xep.chatstate;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.calclab.emite.client.im.chat.ChatDefault;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public class ChatStateTest {
    private static final XmppURI MYSELF = uri("self@domain/res");
    private static final XmppURI OTHER = uri("other@domain/otherRes");
    private ChatStateListener stateListener;
    private ChatDefault chat;
    private ChatState chatState;

    @Before
    public void aaCreate() {
        chat = Mockito.mock(ChatDefault.class);
        stateListener = Mockito.mock(ChatStateListener.class);
        chatState = new ChatState(chat);
        chatState.addOtherStateListener(stateListener);
    }

    @Test
    public void shouldFireGone() {
        Message message = new Message(MYSELF, OTHER, null);
        message.addChild("gone", ChatState.XMLNS);
        chatState.onMessageReceived(chat, message);
        Mockito.verify(stateListener).onGone();
    }

    @Test
    public void shouldFireOtherCompossing() {
        Message message = new Message(MYSELF, OTHER, null);
        message.addChild("composing", ChatState.XMLNS);
        chatState.onMessageReceived(chat, message);
        Mockito.verify(stateListener).onComposing();
    }

    @Test
    public void shouldFireOtherCompossingAsGmailDo() {
        Message message = new Message(MYSELF, OTHER, null);
        message.addChild("cha:composing", ChatState.XMLNS);
        chatState.onMessageReceived(chat, message);
        Mockito.verify(stateListener).onComposing();
        Mockito.verify(stateListener).onComposing();
    }

    @Test
    public void shouldFireOtherCompossingToWithoutResource() {
        Message message = new Message(MYSELF, OTHER.getJID(), null);
        message.addChild("cha:composing", ChatState.XMLNS);
        chatState.onMessageReceived(chat, message);
        Mockito.verify(stateListener).onComposing();
    }

}
