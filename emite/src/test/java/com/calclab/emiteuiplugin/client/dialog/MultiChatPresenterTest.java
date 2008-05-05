package com.calclab.emiteuiplugin.client.dialog;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.ourproject.kune.platf.client.services.I18nTranslationServiceMocked;

import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.im.roster.RosterItem;
import com.calclab.emite.client.im.roster.RosterManager;
import com.calclab.emite.client.im.roster.RosterItem.Subscription;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emiteui.client.MockitoXmpp;
import com.calclab.emiteuiplugin.client.ChatDialogFactory;
import com.calclab.emiteuiplugin.client.UserChatOptions;
import com.calclab.emiteuiplugin.client.chat.ChatUI;
import com.calclab.emiteuiplugin.client.chat.ChatUIListener;
import com.calclab.emiteuiplugin.client.chat.ChatUIPresenter;
import com.calclab.emiteuiplugin.client.chat.ChatUIView;
import com.calclab.emiteuiplugin.client.params.AvatarProvider;
import com.calclab.emiteuiplugin.client.params.MultiChatCreationParam;
import com.calclab.emiteuiplugin.client.roster.RosterUI;
import com.calclab.emiteuiplugin.client.roster.RosterUIView;
import com.calclab.emiteuiplugin.client.users.ChatUserUI;

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
	final XmppURI otherUri = uri("matt@example.com");
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
	final RosterUI roster = Mockito.mock(RosterUI.class);
	chat = Mockito.mock(Chat.class);

	final ChatUIPresenter presenter = new ChatUIPresenter(otherUri, "lutherb", "black", chatListener);
	chatUIView = Mockito.mock(ChatUIView.class);
	presenter.init(chatUIView);
	chatUI = presenter;

	// Stubs
	Mockito.stub(rosterUI.getView()).toReturn(rosterUIView);
	Mockito.stub(rosterUI.getUserByJid(otherUri)).toReturn(otherUser);
	Mockito.stub(chat.getOtherURI()).toReturn(otherUri);
	Mockito.stub(
		factory.createChatUI((XmppURI) Mockito.anyObject(), (String) Mockito.anyObject(), (String) Mockito
			.anyObject(), (ChatUIListener) Mockito.anyObject())).toReturn(chatUI);

	final AvatarProvider avatarProvider = new AvatarProvider() {
	    public String getAvatarURL(XmppURI userURI) {
		return "images/person-def.gif";
	    }
	};

	final MultiChatCreationParam param = new MultiChatCreationParam("Chat title", null, "rooms.localhost", i18n,
		avatarProvider, new UserChatOptions(sessionUserJid, "passwdofuser", "resource", "blue",
			RosterManager.DEF_SUBSCRIPTION_MODE));

	multiChat = new MultiChatPresenter(xmpp, i18n, factory, param, multiChatlistener, roster);
	multiChat.init(multiChatPanel);

	// Basic chat creation
	multiChat.createChat(chat);
    }

    @Test
    public void broadCastMessageNotEmptyFrom() {
	final Message message = new Message(XmppURI.jid("emitedemo.ourproject.org"), uri(sessionUserJid), messageBody);
	multiChat.messageReceived(chat, message);
	Mockito.verify(chatUIView).addMessage("emitedemo.ourproject.org", "green", messageBody);
    }

    @Test
    public void closeAllChatsWithoutConfirmation() {
	multiChat.closeAllChats(true);
	multiChat.doClose(chat, chatUI);
	Mockito.verify(multiChatPanel).setCloseAllOptionEnabled(true);
	// TODO check order
	Mockito.verify(multiChatPanel).setInfoPanelVisible(false);
	Mockito.verify(multiChatPanel, Mockito.times(2)).setCloseAllOptionEnabled(false);
	Mockito.verify(multiChatPanel, Mockito.times(2)).setInfoPanelVisible(true);
	Mockito.verify(multiChatPanel, Mockito.times(2)).setSendEnabled(false);
	Mockito.verify(multiChatPanel, Mockito.times(2)).setInputEditable(false);
	Mockito.verify(multiChatPanel, Mockito.times(2)).setEmoticonButtonEnabled(false);
	Mockito.verify(chatUIView).destroy();
    }

    @Test
    public void removeAndCreateChat() {
	multiChat.onCurrentUserSendWithEnter(messageBody);
	multiChat.closeAllChats(false);
	multiChat.createChat(chat);
	multiChat.onCurrentUserSendWithButton(messageBody);
	Mockito.verify(chatListener, Mockito.times(2)).onCurrentUserSend(messageBody);
    }

    @Test
    public void removeAndCreateChat2() {
	multiChat.onCurrentUserSendWithEnter(messageBody);
	// FIXME multiChat.closePairChat(chatUI);
	multiChat.createChat(chat);
	multiChat.onCurrentUserSendWithButton(messageBody);
	Mockito.verify(chatListener, Mockito.times(2)).onCurrentUserSend(messageBody);
    }

    @Test
    public void testReceiveMessage() {
	sendMessageFromOther();
    }

    private void sendMessageFromOther() {
	final Message message = new Message(uri(otherUser.getURI().toString()), uri(sessionUserJid), messageBody);
	multiChat.messageReceived(chat, message);
	Mockito.verify(chatUIView).addMessage(otherUser.getURI().getNode(), "green", message.getBody());
    }

}
