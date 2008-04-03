package com.calclab.examplechat.client.chatuiplugin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.examplechat.client.chatuiplugin.abstractchat.ChatId;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatListener;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatView;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChat;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;

public class MultiChatPresenterTest {

    private PairChatUser sessionUser;
    private MultiChatPresenter multiChat;
    private PairChatUser otherUser;
    private ChatDialogFactory factory;
    private PairChat pairChat;

    @Before
    public void begin() {
        factory = Mockito.mock(ChatDialogFactory.class);
        MultiChatListener multiChatlistener = Mockito.mock(MultiChatListener.class);
        sessionUser = new PairChatUser("", XmppURI.parseURI("lutherb@example.com"), "lutherb", "red", new Presence());
        otherUser = new PairChatUser("", XmppURI.parseURI("matt@example.com"), "matt", "blue", new Presence());
        multiChat = new MultiChatPresenter(factory, sessionUser, multiChatlistener);
        MultiChatView panel = Mockito.mock(MultiChatView.class);
        multiChat.init(panel);
        ChatId chatId = new ChatId(otherUser.getJid());
        pairChat = Mockito.mock(PairChat.class);
        Mockito.stub(factory.createPairChat(chatId, multiChat, sessionUser, otherUser)).toReturn(pairChat);
        multiChat.createPairChat(otherUser);
    }

    @Test
    public void testOnSendMessage() {
        String message = "hello world";
        Mockito.stub(pairChat.getChatId()).toReturn(new ChatId(otherUser.getJid()));
        multiChat.onCurrentUserSend(message);
        Mockito.verify(pairChat).addMessage(sessionUser.getJid(), message);
    }

}
