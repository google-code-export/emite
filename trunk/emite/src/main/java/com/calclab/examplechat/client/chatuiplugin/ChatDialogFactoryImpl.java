package com.calclab.examplechat.client.chatuiplugin;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.emite.client.im.chat.ChatDefault;
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

    public MultiChat createMultiChat(final PairChatUser currentSessionUser, final I18nTranslationService i18n,
            final MultiChatListener listener) {
        MultiChatPresenter presenter = new MultiChatPresenter(App.getInstance(), currentSessionUser, listener);
        MultiChatPanel panel = new MultiChatPanel(i18n, presenter);
        presenter.init(panel);
        return presenter;
    }

    public GroupChatUserList createGroupChatUserList() {
        GroupChatUserListPresenter userListPresenter = new GroupChatUserListPresenter();
        GroupChatUserListPanel panel = new GroupChatUserListPanel();
        userListPresenter.init(panel);
        return userListPresenter;
    }

    public GroupChat createGroupChat(final ChatDefault chatDefault, final GroupChatListener listener,
            final GroupChatUser currentSessionUser) {
        GroupChatUserList userList = createGroupChatUserList();
        GroupChatPresenter presenter = new GroupChatPresenter(chatDefault, listener, currentSessionUser);
        presenter.setUserList(userList);
        GroupChatPanel panel = new GroupChatPanel(presenter);
        presenter.init(panel);
        return presenter;
    }

    public PairChat createPairChat(final ChatDefault chatDefault, final PairChatListener listener,
            final PairChatUser currentSessionUser, final PairChatUser otherUser) {
        PairChatPresenter presenter = new PairChatPresenter(chatDefault, listener, currentSessionUser, otherUser);
        PairChatPanel panel = new PairChatPanel(presenter);
        presenter.init(panel);
        return presenter;
    }

}
