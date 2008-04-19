package com.calclab.emiteui.client.emiteuiplugin.dialog;

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
import com.calclab.emiteui.client.MockitoXmpp;
import com.calclab.emiteui.client.emiteuiplugin.ChatDialogFactory;
import com.calclab.emiteui.client.emiteuiplugin.UserChatOptions;
import com.calclab.emiteui.client.emiteuiplugin.chat.ChatUI;
import com.calclab.emiteui.client.emiteuiplugin.chat.ChatUIListener;
import com.calclab.emiteui.client.emiteuiplugin.chat.ChatUIPresenter;
import com.calclab.emiteui.client.emiteuiplugin.chat.ChatUIView;
import com.calclab.emiteui.client.emiteuiplugin.params.MultiChatCreationParam;
import com.calclab.emiteui.client.emiteuiplugin.roster.RosterUI;
import com.calclab.emiteui.client.emiteuiplugin.roster.RosterUIView;
import com.calclab.emiteui.client.emiteuiplugin.users.ChatUserUI;

public class MultiChatPresenterTest {

    private Chat chat;
    private ChatUIListener chatListener;
    private ChatDialogFactory factory;
    private String messageBody;
    private MultiChatPresenter multiChat;
    private MultiChatView multiChatPanel;
    private ChatUserUI otherUser;
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

        // User and message
        sessionUserJid = "lutherb@example.com";
        otherUser = new ChatUserUI("", rosterItem, "blue");
        messageBody = "hello world :)";

        // Mocks creation
        xmpp = new MockitoXmpp();
        final RosterUI rosterUI = Mockito.mock(RosterUI.class);
        final RosterUIView rosterUIView = Mockito.mock(RosterUIView.class);
        final I18nTranslationServiceMocked i18n = new I18nTranslationServiceMocked();
        multiChatPanel = Mockito.mock(MultiChatView.class);
        chatListener = Mockito.mock(ChatUIListener.class);
        chat = Mockito.mock(Chat.class);

        ChatUIPresenter presenter = new ChatUIPresenter("lutherb", "black", chatListener);
        chatUIView = Mockito.mock(ChatUIView.class);
        presenter.init(chatUIView);
        chatUI = presenter;

        // Stubs
        Mockito.stub(factory.createrRosterUI(xmpp, i18n)).toReturn(rosterUI);
        Mockito.stub(rosterUI.getView()).toReturn(rosterUIView);
        Mockito.stub(rosterUI.getUserByJid(otherUri)).toReturn(otherUser);
        Mockito.stub(chat.getOtherURI()).toReturn(otherUri);
        Mockito.stub(
                factory.createChatUI((String) Mockito.anyObject(), (String) Mockito.anyObject(),
                        (ChatUIListener) Mockito.anyObject())).toReturn(chatUI);
        final MultiChatCreationParam param = new MultiChatCreationParam(null, new UserChatOptions(sessionUserJid,
                "passwdofuser", "blue", Roster.DEF_SUBSCRIPTION_MODE));

        multiChat = new MultiChatPresenter(xmpp, i18n, factory, param, multiChatlistener);
        multiChat.init(multiChatPanel);

        // Basic chat creation
        multiChat.createChat(chat);
    }

    // @Test
    public void closeAllChatsWithoutConfirmation() {
        multiChat.closeAllChats(true);
        multiChat.doAfterCloseConfirmed(chat, chatUI);
        Mockito.verify(multiChatPanel).setCloseAllOptionEnabled(true);
        // TODO check order
        Mockito.verify(multiChatPanel).setInfoPanelVisible(false);
        Mockito.verify(multiChatPanel, Mockito.times(2)).setCloseAllOptionEnabled(false);
        Mockito.verify(multiChatPanel, Mockito.times(2)).setInfoPanelVisible(true);
        Mockito.verify(multiChatPanel, Mockito.times(2)).setSubjectEditable(false);
        Mockito.verify(multiChatPanel, Mockito.times(2)).setSendEnabled(false);
        Mockito.verify(multiChatPanel, Mockito.times(2)).setInputEditable(false);
        Mockito.verify(multiChatPanel, Mockito.times(2)).setEmoticonButtonEnabled(false);
        Mockito.verify(chatUIView).destroy();
    }

    @Test
    public void removeAndCreateChat() {
        multiChat.onCurrentUserSend(messageBody);
        multiChat.closeAllChats(false);
        multiChat.createChat(chat);
        multiChat.onCurrentUserSend(messageBody);
        Mockito.verify(chatListener, Mockito.times(2)).onCurrentUserSend(messageBody);
    }

    @Test
    public void removeAndCreateChat2() {
        multiChat.onCurrentUserSend(messageBody);
        // FIXME multiChat.closePairChat(chatUI);
        multiChat.createChat(chat);
        multiChat.onCurrentUserSend(messageBody);
        Mockito.verify(chatListener, Mockito.times(2)).onCurrentUserSend(messageBody);
    }

    @Test
    public void testReceiveMessage() {
        sendMessageFromOther();
    }

    private void sendMessageFromOther() {
        final Message message = new Message(XmppURI.parse(otherUser.getURI().toString()),
                XmppURI.parse(sessionUserJid), messageBody);
        multiChat.messageReceived(chat, message);
        Mockito.verify(chatUIView).addMessage(otherUser.getURI().getNode(), "green", message.getBody());
    }

}
