package com.calclab.examplechat.client.chatuiplugin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.ourproject.kune.platf.client.services.I18nTranslationServiceMocked;

import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.chat.ChatListener;
import com.calclab.emite.client.im.roster.Roster;
import com.calclab.emite.client.im.roster.RosterItem;
import com.calclab.emite.client.im.roster.RosterManager;
import com.calclab.emite.client.im.roster.RosterItem.Subscription;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.examplechat.client.MockitoXmpp;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatListener;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatView;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;
import com.calclab.examplechat.client.chatuiplugin.params.MultiChatCreationParam;
import com.calclab.examplechat.client.chatuiplugin.roster.RosterUI;
import com.calclab.examplechat.client.chatuiplugin.roster.RosterUIView;

public class MultiChatPresenterTest {

    private Chat chat;
    private ChatListener chatListener;
    private ChatDialogFactory factory;
    private String messageBody;
    private MultiChatPresenter multiChat;
    private MultiChatView multiChatPanel;
    private PairChatUser otherUser;
    private PairChatPresenter pairChat;
    private String sessionUserJid;
    private MockitoXmpp xmpp;
    private RosterManager rosterManager;

    @Before
    public void begin() {
        factory = Mockito.mock(ChatDialogFactory.class);

        final MultiChatListener multiChatlistener = Mockito.mock(MultiChatListener.class);
        final XmppURI otherUri = XmppURI.parse("matt@example.com");
        final RosterItem rosterItem = new RosterItem(otherUri, Subscription.both, "matt");

        sessionUserJid = "lutherb@example.com";
        otherUser = new PairChatUser("", rosterItem, "blue");

        // Mocks creation
        xmpp = new MockitoXmpp();
        final RosterUI rosterUI = Mockito.mock(RosterUI.class);
        final RosterUIView rosterUIView = Mockito.mock(RosterUIView.class);
        final I18nTranslationServiceMocked i18n = new I18nTranslationServiceMocked();
        multiChatPanel = Mockito.mock(MultiChatView.class);
        chatListener = Mockito.mock(ChatListener.class);
        pairChat = Mockito.mock(PairChatPresenter.class);
        chat = Mockito.mock(Chat.class);
        rosterManager = xmpp.getRosterManager();

        // Stubs
        Mockito.stub(factory.createrRosterUI(xmpp, i18n)).toReturn(rosterUI);
        Mockito.stub(rosterUI.getView()).toReturn(rosterUIView);
        Mockito.stub(rosterUI.getUserByJid(otherUri.getJID())).toReturn(otherUser);
        Mockito.stub(pairChat.getChat()).toReturn(chat);
        Mockito.stub(pairChat.getChat().getOtherURI()).toReturn(otherUri);
        Mockito.stub(
                factory.createPairChat((Chat) Mockito.anyObject(), (MultiChatPresenter) Mockito.anyObject(),
                        (String) Mockito.anyObject(), (PairChatUser) Mockito.anyObject())).toReturn(pairChat);
        final MultiChatCreationParam param = new MultiChatCreationParam(null, sessionUserJid, "passwdofuser",
                new UserChatOptions("blue", Roster.DEF_SUBSCRIPTION_MODE));

        multiChat = new MultiChatPresenter(xmpp, i18n, factory, param, multiChatlistener);
        multiChat.init(multiChatPanel);

        // Basic chat creation
        chat.addListener(chatListener);
        multiChat.createPairChat(chat);
        messageBody = "hello world :)";
    }

    @Test
    public void removeAndCreateChat() {
        multiChat.onCurrentUserSend(messageBody);
        multiChat.closeAllChats(false);
        multiChat.createPairChat(chat);
        multiChat.onCurrentUserSend(messageBody);
        Mockito.verify(chat, Mockito.times(2)).send(messageBody);
    }

    @Test
    public void removeAndCreateChat2() {
        multiChat.onCurrentUserSend(messageBody);
        multiChat.closePairChat(pairChat);
        multiChat.createPairChat(chat);
        multiChat.onCurrentUserSend(messageBody);
        Mockito.verify(chat, Mockito.times(2)).send(messageBody);
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
        final Message message = new Message(otherUser.getUri(), XmppURI.parse(sessionUserJid), messageBody);
        multiChat.messageReceived(chat, message);
        Mockito.verify(pairChat).addMessage(otherUser.getUri(), messageBody);
    }

}
