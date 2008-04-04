/**
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
