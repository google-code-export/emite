/*
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
package com.calclab.emiteuiplugin.client;

import org.ourproject.kune.platf.client.services.I18nTranslationService;

import com.calclab.emite.client.Xmpp;
import com.calclab.emiteuiplugin.client.chat.ChatUI;
import com.calclab.emiteuiplugin.client.chat.ChatUIListener;
import com.calclab.emiteuiplugin.client.chat.ChatUIPanel;
import com.calclab.emiteuiplugin.client.chat.ChatUIPresenter;
import com.calclab.emiteuiplugin.client.dialog.MultiChat;
import com.calclab.emiteuiplugin.client.dialog.MultiChatListener;
import com.calclab.emiteuiplugin.client.dialog.MultiChatPanel;
import com.calclab.emiteuiplugin.client.dialog.MultiChatPresenter;
import com.calclab.emiteuiplugin.client.params.MultiChatCreationParam;
import com.calclab.emiteuiplugin.client.room.RoomUI;
import com.calclab.emiteuiplugin.client.room.RoomUIListener;
import com.calclab.emiteuiplugin.client.room.RoomUIPanel;
import com.calclab.emiteuiplugin.client.room.RoomUIPresenter;
import com.calclab.emiteuiplugin.client.room.RoomUserListUIPanel;
import com.calclab.emiteuiplugin.client.roster.RosterUI;
import com.calclab.emiteuiplugin.client.roster.RosterUIPanel;
import com.calclab.emiteuiplugin.client.roster.RosterUIPresenter;

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

    public ChatUI createChatUI(final String chatTitle, final String currentUserAlias, final String currentUserColor,
            final ChatUIListener listener) {
        ChatUIPresenter presenter = new ChatUIPresenter(chatTitle, currentUserAlias, currentUserColor, listener);
        ChatUIPanel panel = new ChatUIPanel(presenter);
        presenter.init(panel);
        return presenter;
    }

    public MultiChat createMultiChat(final Xmpp xmpp, final MultiChatCreationParam param,
            final MultiChatListener listener) {
        I18nTranslationService i18n = param.getI18nService();
        final MultiChatPresenter presenter = new MultiChatPresenter(xmpp, i18n, App.getInstance(), param, listener);
        RosterUI rosterUI = createrRosterUI(xmpp, i18n);
        final MultiChatPanel panel = new MultiChatPanel(rosterUI, i18n, presenter);
        presenter.init(panel);
        return presenter;
    }

    public RoomUI createRoomUI(final String chatTitle, final String currentUserAlias, final String currentUserColor,
            final I18nTranslationService i18n, final RoomUIListener listener) {
        RoomUIPresenter presenter = new RoomUIPresenter(i18n, chatTitle, currentUserAlias, currentUserColor, listener);
        RoomUserListUIPanel roomUserListUIPanel = new RoomUserListUIPanel(i18n, presenter);
        RoomUIPanel panel = new RoomUIPanel(presenter);
        presenter.init(panel, roomUserListUIPanel);
        return presenter;
    }

    public RosterUI createrRosterUI(final Xmpp xmpp, final I18nTranslationService i18n) {
        RosterUIPresenter presenter = new RosterUIPresenter(xmpp, i18n);
        RosterUIPanel panel = new RosterUIPanel(i18n, presenter);
        presenter.init(panel);
        return presenter;
    }

}
