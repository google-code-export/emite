package com.calclab.examplechat.client.chatuiplugin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.chat.ChatListener;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatListener;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatView;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChat;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;
import com.calclab.examplechat.client.chatuiplugin.params.ChatMessageParam;

public class MultiChatPresenterTest {

    private ChatDialogFactory factory;
    private MultiChatPresenter multiChat;
    private PairChatUser otherUser;
    private PairChat pairChat;
    private PairChatUser sessionUser;
    private Chat chat;
    private ChatListener chatListener;
    private MultiChatView multiChatPanel;
    private String messageBody;

    @Before
    public void begin() {
        factory = Mockito.mock(ChatDialogFactory.class);

        final MultiChatListener multiChatlistener = Mockito.mock(MultiChatListener.class);
        XmppURI meUri = XmppURI.parse("lutherb@example.com");
        sessionUser = new PairChatUser("", meUri, "lutherb", "red", new Presence());
        XmppURI otherUri = XmppURI.parse("matt@example.com");
        otherUser = new PairChatUser("", otherUri, "matt", "blue", new Presence());
        multiChat = new MultiChatPresenter(factory, sessionUser, multiChatlistener);
        multiChatPanel = Mockito.mock(MultiChatView.class);
        multiChat.init(multiChatPanel);
        chat = Mockito.mock(Chat.class);
        chatListener = Mockito.mock(ChatListener.class);
        chat.addListener(chatListener);
        pairChat = Mockito.mock(PairChat.class);
        Mockito.stub(factory.createPairChat(chat, multiChat, sessionUser, otherUser)).toReturn(pairChat);
        Mockito.stub(pairChat.getChat()).toReturn(chat);
        Mockito.stub(pairChat.getChat().getOtherURI()).toReturn(otherUri);
        multiChat.addPresenceBuddy(otherUser);
        multiChat.createPairChat(chat);
        messageBody = "hello world :)";
    }

    @Test
    public void testOnSendMessage() {
        multiChat.onCurrentUserSend(messageBody);
        Mockito.verify(chat).send(messageBody);
    }

    @Test
    public void testReceiveMessage() {
        sendMessageFromOther();
    }

    private void sendMessageFromOther() {
        final Message message = new Message(otherUser.getUri(), sessionUser.getUri(), messageBody);
        ChatMessageParam param = new ChatMessageParam(chat, message);
        multiChat.messageReceived(param);
        Mockito.verify(pairChat).addMessage(otherUser.getUri(), messageBody);
    }

    @Test
    public void removeAndAddPresenceAndSend() {
        multiChat.removePresenceBuddy(otherUser);
        multiChat.addPresenceBuddy(otherUser);
        sendMessageFromOther();
    }

    // Probar a cerrar room y volver a escribir...

    @Test
    public void removeAndCreateChat() {
        multiChat.onCurrentUserSend(messageBody);
        multiChat.closeAllChats(false);
        multiChat.createPairChat(chat);
        multiChat.onCurrentUserSend(messageBody);
        Mockito.verify(chat, Mockito.times(2)).send(messageBody);
    }

}
