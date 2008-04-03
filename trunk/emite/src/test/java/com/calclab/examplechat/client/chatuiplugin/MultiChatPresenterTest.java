package com.calclab.examplechat.client.chatuiplugin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatListener;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatView;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChat;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;

public class MultiChatPresenterTest {

    private ChatDialogFactory factory;
    private MultiChatPresenter multiChat;
    private PairChatUser otherUser;
    private PairChat pairChat;
    private PairChatUser sessionUser;

    @Before
    public void begin() {
        factory = Mockito.mock(ChatDialogFactory.class);

        final MultiChatListener multiChatlistener = Mockito.mock(MultiChatListener.class);
        sessionUser = new PairChatUser("", XmppURI.parse("lutherb@example.com"), "lutherb", "red", new Presence());
        otherUser = new PairChatUser("", XmppURI.parse("matt@example.com"), "matt", "blue", new Presence());
        multiChat = new MultiChatPresenter(factory, sessionUser, multiChatlistener);
        final MultiChatView panel = Mockito.mock(MultiChatView.class);
        multiChat.init(panel);
        // final Chat chat = new Chat(otherUser.getJid(), sessionUser.getJid(),
        // "", );
        pairChat = Mockito.mock(PairChat.class);
        // Mockito.stub(factory.createPairChat(chat, multiChat, sessionUser,
        // otherUser)).toReturn(pairChat);
        // Mockito.stub(pairChat.getChat()).toReturn(new
        // Chat(otherUser.getJid()));
        // multiChat.createPairChat(otherUser);
    }

    @Test
    public void testOnSendMessage() {
        // final String message = "hello world";
        // multiChat.onCurrentUserSend(message);
        // Mockito.verify(pairChat).addMessage(sessionUser.getJid(), message);
    }

}
