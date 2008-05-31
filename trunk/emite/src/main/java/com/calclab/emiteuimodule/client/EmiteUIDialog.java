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
package com.calclab.emiteuimodule.client;

import java.util.Date;

import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.core.bosh.BoshOptions;
import com.calclab.emite.client.core.signal.Listener;
import com.calclab.emite.client.im.roster.RosterManager;
import com.calclab.emite.client.im.roster.RosterManager.SubscriptionMode;
import com.calclab.emite.client.xep.muc.RoomManager;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emiteuimodule.client.chat.ChatUIStartedByMe;
import com.calclab.emiteuimodule.client.dialog.MultiChatPresenter;
import com.calclab.emiteuimodule.client.params.AvatarProvider;
import com.calclab.emiteuimodule.client.params.MultiChatCreationParam;
import com.calclab.emiteuimodule.client.status.OwnPresence;
import com.calclab.emiteuimodule.client.status.OwnPresence.OwnStatus;
import com.google.gwt.user.client.Window;

public class EmiteUIDialog {
    private static final String EMITE_DEF_TITLE = "Emite Chat";
    private MultiChatPresenter multiChatDialog;
    private final Xmpp xmpp;
    private final EmiteUIFactory factory;

    public EmiteUIDialog(final Xmpp xmpp, final EmiteUIFactory factory) {
        this.xmpp = xmpp;
        this.factory = factory;
    }

    public void chat(final XmppURI otherUserURI) {
        xmpp.getChatManager().openChat(otherUserURI, ChatUIStartedByMe.class, new ChatUIStartedByMe(true));
    }

    public void closeAllChats(final boolean withConfirmation) {
        multiChatDialog.closeAllChats(withConfirmation);
    }

    public void getChatDialog(final MultiChatCreationParam param) {
        if (multiChatDialog == null) {
            multiChatDialog = createChatDialog(param);
            ImagesHelper.preFetchImages();
        }
    }

    public void hide() {
        multiChatDialog.hide();
    }

    public boolean isVisible() {
        return multiChatDialog.isVisible();
    }

    public void joinRoom(final XmppURI roomURI) {
        xmpp.getInstance(RoomManager.class).openChat(roomURI, ChatUIStartedByMe.class, new ChatUIStartedByMe(true));
    }

    public void onConversationAttended(final Listener<String> listener) {
        multiChatDialog.onConversationAttended(listener);
    }

    public void onConversationUnattended(final Listener<String> listener) {
        multiChatDialog.onConversationUnattended(listener);
    }

    public void onShowUnavailableRosterItemsChanged(final Listener<Boolean> listener) {
        multiChatDialog.onShowUnavailableRosterItemsChanged(listener);
    }

    public void onUserColorChanged(final Listener<String> listener) {
        multiChatDialog.onUserColorChanged(listener);
    }

    public void onUserSubscriptionModeChanged(final Listener<SubscriptionMode> listener) {
        multiChatDialog.onUserSubscriptionModeChanged(listener);
    }

    public void refreshUserInfo(final UserChatOptions userChatOptions) {
        multiChatDialog.setUserChatOptions(userChatOptions);
    }

    public void setOwnPresence(final OwnStatus status) {
        multiChatDialog.setOwnPresence(new OwnPresence(status));
    }

    public void show() {
        multiChatDialog.show();
    }

    public void show(final OwnStatus status) {
        show();
        setOwnPresence(status);
    }

    public void start(final String userJid, final String userPasswd, final String httpBase, final String roomHost) {
        start(new UserChatOptions(userJid, userPasswd, ("emiteui-" + new Date().getTime()), "blue",
                RosterManager.DEF_SUBSCRIPTION_MODE, true), httpBase, roomHost);
    }

    public void start(final String emiteDialogTitle, final UserChatOptions userChatOptions, final String httpBase,
            final String roomHost, final AvatarProvider avatarProvider) {
        xmpp.setBoshOptions(new BoshOptions(httpBase));
        getChatDialog(new MultiChatCreationParam(emiteDialogTitle, roomHost, avatarProvider, userChatOptions));
    }

    public void start(final UserChatOptions userChatOptions, final String httpBase, final String roomHost) {
        // We define, default AvatarProvider and MultiChaListener for simple
        // facade
        start(EMITE_DEF_TITLE, userChatOptions, httpBase, roomHost, new AvatarProvider() {
            public String getAvatarURL(final XmppURI userURI) {
                return "images/person-def.gif";
            }
        });
        final String initialWindowTitle = Window.getTitle();
        onConversationAttended(new Listener<String>() {
            public void onEvent(final String parameter) {
                Window.setTitle(initialWindowTitle);
            }
        });
        onConversationUnattended(new Listener<String>() {
            public void onEvent(final String chatTitle) {
                Window.setTitle("(* " + chatTitle + ") " + initialWindowTitle);
            }
        });
    }

    private MultiChatPresenter createChatDialog(final MultiChatCreationParam param) {
        final MultiChatPresenter dialog = factory.createMultiChat(param);
        return dialog;
    }

}
