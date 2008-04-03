package com.calclab.examplechat.client.chatuiplugin;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.emite.client.im.chat.Chat;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChat;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatListener;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatPanel;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChat;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChatListener;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChatPanel;
import com.calclab.examplechat.client.chatuiplugin.groupchat.GroupChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChat;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatListener;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatPanel;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.pairchat.PairChatUser;
import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUser;
import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUserList;
import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUserListPanel;
import com.calclab.examplechat.client.chatuiplugin.users.GroupChatUserListPresenter;

public class ChatDialogFactoryImpl implements ChatDialogFactory {
    public static class App {
	private static ChatDialogFactoryImpl ourInstance = null;

	public static synchronized ChatDialogFactoryImpl getInstance() {
	    if (ourInstance == null) {
		ourInstance = new ChatDialogFactoryImpl();
	    }
	    return ourInstance;
	}
    }

    public GroupChat createGroupChat(final Chat chat, final GroupChatListener listener,
	    final GroupChatUser currentSessionUser) {
	final GroupChatUserList userList = createGroupChatUserList();
	final GroupChatPresenter presenter = new GroupChatPresenter(chat, listener, currentSessionUser);
	presenter.setUserList(userList);
	final GroupChatPanel panel = new GroupChatPanel(presenter);
	presenter.init(panel);
	return presenter;
    }

    public GroupChatUserList createGroupChatUserList() {
	final GroupChatUserListPresenter userListPresenter = new GroupChatUserListPresenter();
	final GroupChatUserListPanel panel = new GroupChatUserListPanel();
	userListPresenter.init(panel);
	return userListPresenter;
    }

    public MultiChat createMultiChat(final PairChatUser currentSessionUser, final I18nTranslationService i18n,
	    final MultiChatListener listener) {
	final MultiChatPresenter presenter = new MultiChatPresenter(App.getInstance(), currentSessionUser, listener);
	final MultiChatPanel panel = new MultiChatPanel(i18n, presenter);
	presenter.init(panel);
	return presenter;
    }

    public PairChat createPairChat(final Chat chat, final PairChatListener listener,
	    final PairChatUser currentSessionUser, final PairChatUser otherUser) {
	final PairChatPresenter presenter = new PairChatPresenter(chat, listener, currentSessionUser, otherUser);
	final PairChatPanel panel = new PairChatPanel(presenter);
	presenter.init(panel);
	return presenter;
    }

}
