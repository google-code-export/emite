package com.calclab.examplechat.client.chatuiplugin;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.ourproject.kune.platf.client.services.I18nTranslationServiceMocked;

import com.calclab.emite.client.AbstractXmpp;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.chat.ChatListener;
import com.calclab.emite.client.im.chat.ChatManagerDefault;
import com.calclab.emite.client.im.presence.PresenceManager;
import com.calclab.emite.client.im.roster.Roster;
import com.calclab.emite.client.im.roster.RosterItem;
import com.calclab.emite.client.im.roster.RosterItem.Subscription;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatListener;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatView;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;
import com.calclab.examplechat.client.chatuiplugin.params.MultiChatCreationParam;
import com.calclab.examplechat.client.chatuiplugin.roster.RosterUI;
import com.calclab.examplechat.client.chatuiplugin.roster.RosterUIView;

public class MultiChatPresenterTest {

    private ChatDialogFactory factory;
    private MultiChatPresenter multiChat;
    private PairChatUser otherUser;
    private PairChatPresenter pairChat;
    private Chat chat;
    private ChatListener chatListener;
    private MultiChatView multiChatPanel;
    private String messageBody;
    private AbstractXmpp xmpp;
    private Session session;
    private Roster roster;
    private ChatManagerDefault chatManager;
    private PresenceManager presenceManager;
    private String sessionUserJid;

    @Before
    public void begin() {
        factory = Mockito.mock(ChatDialogFactory.class);

        final MultiChatListener multiChatlistener = Mockito.mock(MultiChatListener.class);
        XmppURI otherUri = XmppURI.parse("matt@example.com");
        RosterItem rosterItem = new RosterItem(otherUri, Subscription.both, "matt");

        sessionUserJid = "lutherb@example.com";
        otherUser = new PairChatUser("", rosterItem, "blue");

        // Mocks creation
        xmpp = Mockito.mock(AbstractXmpp.class);
        session = Mockito.mock(Session.class);
        roster = Mockito.mock(Roster.class);
        chatManager = Mockito.mock(ChatManagerDefault.class);
        presenceManager = Mockito.mock(PresenceManager.class);
        RosterUI rosterUI = Mockito.mock(RosterUI.class);
        RosterUIView rosterUIView = Mockito.mock(RosterUIView.class);
        I18nTranslationServiceMocked i18n = new I18nTranslationServiceMocked();
        multiChatPanel = Mockito.mock(MultiChatView.class);
        chat = Mockito.mock(Chat.class);
        chatListener = Mockito.mock(ChatListener.class);
        pairChat = Mockito.mock(PairChatPresenter.class);

        // Stubs
        Mockito.stub(xmpp.getSession()).toReturn(session);
        Mockito.stub(xmpp.getRoster()).toReturn(roster);
        Mockito.stub(xmpp.getChat()).toReturn(chatManager);
        Mockito.stub(xmpp.getPresenceManager()).toReturn(presenceManager);
        Mockito.stub(factory.createrRosterUI(xmpp, i18n)).toReturn(rosterUI);
        Mockito.stub(rosterUI.getView()).toReturn(rosterUIView);
        Mockito.stub(rosterUI.getUserByJid(otherUri.getJid())).toReturn(otherUser);
        Mockito.stub(pairChat.getChat()).toReturn(chat);
        Mockito.stub(pairChat.getChat().getOtherURI()).toReturn(otherUri);
        Mockito.stub(factory.createPairChat(chat, multiChat, sessionUserJid, otherUser)).toReturn(pairChat);

        MultiChatCreationParam param = new MultiChatCreationParam(null, sessionUserJid, "passwdofuser",
                new UserChatOptions("blue", Roster.DEF_SUBSCRIPTION_MODE));

        multiChat = new MultiChatPresenter(xmpp, i18n, factory, param, multiChatlistener);
        multiChat.init(multiChatPanel);

        // Basic chat creation
        chat.addListener(chatListener);
        // TODO multiChat.addRosterItem(otherUser);
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
        final Message message = new Message(otherUser.getUri(), XmppURI.parse(sessionUserJid), messageBody);
        multiChat.messageReceived(chat, message);
        Mockito.verify(pairChat).addMessage(otherUser.getUri(), messageBody);
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
    public void removeAndCreateChat() {
        multiChat.onCurrentUserSend(messageBody);
        multiChat.closeAllChats(false);
        multiChat.createPairChat(chat);
        multiChat.onCurrentUserSend(messageBody);
        Mockito.verify(chat, Mockito.times(2)).send(messageBody);
    }

}

// src/test/java/com/calclab/examplechat/client/chatuiplugin/MultiChatPresenterTest.java
