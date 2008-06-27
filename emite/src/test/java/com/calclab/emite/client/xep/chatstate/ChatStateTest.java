package com.calclab.emite.client.xep.chatstate;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.calclab.emite.client.im.chat.ChatDefault;
import com.calclab.emite.client.xep.chatstate.ChatStateManager.ChatState;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.testing.MockSlot;

public class ChatStateTest {
    private static final XmppURI MYSELF = uri("self@domain/res");
    private static final XmppURI OTHER = uri("other@domain/otherRes");
    private MockSlot<ChatState> stateListener;
    private ChatDefault chat;
    private ChatStateManager chatStateManager;

    @Before
    public void aaCreate() {
	chat = Mockito.mock(ChatDefault.class);
	chatStateManager = new ChatStateManager(chat);
	stateListener = new MockSlot<ChatState>();
	chatStateManager.onChatStateChanged(stateListener);
    }

    @Test
    public void shouldFireGone() {
	final Message message = new Message(MYSELF, OTHER, null);
	message.addChild("gone", ChatStateManager.XMLNS);
	chatStateManager.onMessageReceived(chat, message);
	MockSlot.verifyCalledWith(stateListener, ChatState.gone);
    }

    @Test
    public void shouldFireOtherCompossing() {
	final Message message = new Message(MYSELF, OTHER, null);
	message.addChild("composing", ChatStateManager.XMLNS);
	chatStateManager.onMessageReceived(chat, message);
	MockSlot.verifyCalledWith(stateListener, ChatState.composing);
    }

    @Test
    public void shouldFireOtherCompossingAsGmailDo() {
	final Message message = new Message(MYSELF, OTHER, null);
	message.addChild("cha:composing", ChatStateManager.XMLNS);
	chatStateManager.onMessageReceived(chat, message);
	MockSlot.verifyCalledWith(stateListener, ChatState.composing);
    }

    @Test
    public void shouldFireOtherCompossingToWithoutResource() {
	final Message message = new Message(MYSELF, OTHER.getJID(), null);
	message.addChild("cha:composing", ChatStateManager.XMLNS);
	chatStateManager.onMessageReceived(chat, message);
	MockSlot.verifyCalledWith(stateListener, ChatState.composing);
    }

}
