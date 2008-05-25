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
import com.calclab.emite.client.xep.chatstate.ChatState;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emiteuiplugin.client.chat.ChatStatePresenter;
import com.calclab.emiteuiplugin.client.chat.ChatUI;
import com.calclab.emiteuiplugin.client.chat.ChatUIListener;
import com.calclab.emiteuiplugin.client.chat.ChatUIPanel;
import com.calclab.emiteuiplugin.client.chat.ChatUIPresenter;
import com.calclab.emiteuiplugin.client.dialog.MultiChatListener;
import com.calclab.emiteuiplugin.client.dialog.MultiChatPanel;
import com.calclab.emiteuiplugin.client.dialog.MultiChatPresenter;
import com.calclab.emiteuiplugin.client.params.AvatarProvider;
import com.calclab.emiteuiplugin.client.params.MultiChatCreationParam;
import com.calclab.emiteuiplugin.client.room.RoomUI;
import com.calclab.emiteuiplugin.client.room.RoomUIListener;
import com.calclab.emiteuiplugin.client.room.RoomUIPanel;
import com.calclab.emiteuiplugin.client.room.RoomUIPresenter;
import com.calclab.emiteuiplugin.client.room.RoomUserListUIPanel;
import com.calclab.emiteuiplugin.client.roster.RosterPanel;
import com.calclab.emiteuiplugin.client.roster.RosterPresenter;
import com.calclab.emiteuiplugin.client.status.StatusPanel;

public class EmiteUIFactory {
    private final I18nTranslationService i18n;
    private final Xmpp xmpp;

    public EmiteUIFactory(final Xmpp xmpp, final I18nTranslationService i18n) {
        this.xmpp = xmpp;
        this.i18n = i18n;
    }

    public ChatUI createChatUI(final XmppURI otherURI, final String currentUserAlias, final String currentUserColor,
            final ChatState chatState, final ChatUIListener listener) {
        final ChatUIPresenter presenter = new ChatUIPresenter(otherURI, currentUserAlias, currentUserColor, listener);
        new ChatStatePresenter(i18n, chatState, presenter);
        final ChatUIPanel panel = new ChatUIPanel(presenter);
        presenter.init(panel);
        return presenter;
    }

    public MultiChatPresenter createMultiChat(final MultiChatCreationParam param, final MultiChatListener listener) {
        final RosterPresenter roster = createRosterUI(param.getAvatarProvider());
        final MultiChatPresenter presenter = new MultiChatPresenter(xmpp, i18n, this, param, listener, roster);
        final StatusPanel statusPanel = new StatusPanel(i18n);
        final MultiChatPanel panel = new MultiChatPanel(param.getChatDialogTitle(), roster.getView(), statusPanel,
                i18n, presenter);
        presenter.initStatusPanel(statusPanel);
        presenter.init(panel);
        return presenter;
    }

    public RoomUI createRoomUI(final XmppURI otherURI, final String currentUserAlias, final String currentUserColor,
            final I18nTranslationService i18n, final RoomUIListener listener) {
        final RoomUIPresenter presenter = new RoomUIPresenter(i18n, otherURI, currentUserAlias, currentUserColor,
                listener);
        // FIXME: create list presenter
        final RoomUserListUIPanel roomUserListUIPanel = new RoomUserListUIPanel(i18n, presenter);
        final RoomUIPanel panel = new RoomUIPanel(i18n, roomUserListUIPanel, presenter);
        presenter.init(panel, roomUserListUIPanel);
        return presenter;
    }

    public RosterPresenter createRosterUI(final AvatarProvider provider) {
        final RosterPresenter roster = new RosterPresenter(xmpp, i18n, provider);
        final RosterPanel rosterPanel = new RosterPanel(i18n, roster);
        roster.init(rosterPanel);
        return roster;
    }

}
