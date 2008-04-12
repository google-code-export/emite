package com.calclab.examplechat.client.chatuiplugin.dialog;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.ourproject.kune.platf.client.services.I18nTranslationServiceMocked;

import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.roster.Roster;
import com.calclab.emite.client.im.roster.RosterItem;
import com.calclab.emite.client.im.roster.RosterItem.Subscription;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.examplechat.client.MockitoXmpp;
import com.calclab.examplechat.client.chatuiplugin.ChatDialogFactory;
import com.calclab.examplechat.client.chatuiplugin.UserChatOptions;
import com.calclab.examplechat.client.chatuiplugin.chat.ChatUI;
import com.calclab.examplechat.client.chatuiplugin.chat.ChatUIListener;
import com.calclab.examplechat.client.chatuiplugin.chat.ChatUIPresenter;
import com.calclab.examplechat.client.chatuiplugin.chat.ChatUIView;
import com.calclab.examplechat.client.chatuiplugin.params.MultiChatCreationParam;
import com.calclab.examplechat.client.chatuiplugin.roster.RosterUI;
import com.calclab.examplechat.client.chatuiplugin.roster.RosterUIView;
import com.calclab.examplechat.client.chatuiplugin.users.PairChatUser;
import com.calclab.examplechat.client.chatuiplugin.utils.XmppJID;

public class MultiChatPresenterTest {

    private Chat chat;
    private ChatUIListener chatListener;
    private ChatDialogFactory factory;
    private String messageBody;
    private MultiChatPresenter multiChat;
    private MultiChatView multiChatPanel;
    private PairChatUser otherUser;
    private ChatUI chatUI;
    private String sessionUserJid;
    private MockitoXmpp xmpp;
    private ChatUIView chatUIView;

    @Before
    public void begin() {
        factory = Mockito.mock(ChatDialogFactory.class);

        final MultiChatListener multiChatlistener = Mockito.mock(MultiChatListener.class);
        final XmppURI otherUri = XmppURI.parse("matt@example.com");
        final RosterItem rosterItem = new RosterItem(otherUri, Subscription.both, "matt");

        sessionUserJid = "lutherb@example.com";
        otherUser = new PairChatUser("", rosterItem, "blue");
        messageBody = "hello world :)";

        // Mocks creation
        xmpp = new MockitoXmpp();
        final RosterUI rosterUI = Mockito.mock(RosterUI.class);
        final RosterUIView rosterUIView = Mockito.mock(RosterUIView.class);
        final I18nTranslationServiceMocked i18n = new I18nTranslationServiceMocked();
        multiChatPanel = Mockito.mock(MultiChatView.class);
        chatListener = Mockito.mock(ChatUIListener.class);
        chat = Mockito.mock(Chat.class);

        ChatUIPresenter presenter = new ChatUIPresenter(chatListener);
        chatUIView = Mockito.mock(ChatUIView.class);
        presenter.init(chatUIView);
        chatUI = presenter;

        // Stubs
        Mockito.stub(factory.createrRosterUI(xmpp, i18n)).toReturn(rosterUI);
        Mockito.stub(rosterUI.getView()).toReturn(rosterUIView);
        Mockito.stub(rosterUI.getUserByJid(new XmppJID(otherUri))).toReturn(otherUser);
        Mockito.stub(chat.getOtherURI()).toReturn(otherUri);
        Mockito.stub(factory.createChatUI((ChatUIListener) Mockito.anyObject())).toReturn(chatUI);
        final MultiChatCreationParam param = new MultiChatCreationParam(null, sessionUserJid, "passwdofuser",
                new UserChatOptions("blue", Roster.DEF_SUBSCRIPTION_MODE));

        multiChat = new MultiChatPresenter(xmpp, i18n, factory, param, multiChatlistener);
        multiChat.init(multiChatPanel);

        // Basic chat creation
        multiChat.createPairChat(chat);
    }

    @Test
    public void removeAndCreateChat() {
        multiChat.onCurrentUserSend(messageBody);
        multiChat.closeAllChats(false);
        multiChat.createPairChat(chat);
        multiChat.onCurrentUserSend(messageBody);
        Mockito.verify(chatListener, Mockito.times(2)).onCurrentUserSend(messageBody);
    }

    @Test
    public void removeAndCreateChat2() {
        multiChat.onCurrentUserSend(messageBody);
        // FIXME multiChat.closePairChat(chatUI);
        multiChat.createPairChat(chat);
        multiChat.onCurrentUserSend(messageBody);
        Mockito.verify(chatListener, Mockito.times(2)).onCurrentUserSend(messageBody);
    }

    @Test
    public void testReceiveMessage() {
        sendMessageFromOther();
    }

    private void sendMessageFromOther() {
        final Message message = new Message(XmppURI.parse(otherUser.getJid().toString()),
                XmppURI.parse(sessionUserJid), messageBody);
        multiChat.messageReceived(chat, message);
        Mockito.verify(chatUIView).addMessage(otherUser.getJid().getNode(), message.getBody());
    }

}
