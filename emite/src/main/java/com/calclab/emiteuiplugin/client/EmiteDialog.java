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

import java.util.Date;

import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.core.bosh.BoshOptions;
import com.calclab.emite.client.im.roster.RosterManager;
import com.calclab.emite.client.im.roster.RosterManager.SubscriptionMode;
import com.calclab.emite.client.xep.muc.RoomManager;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emiteuiplugin.client.chat.ChatUIStartedByMe;
import com.calclab.emiteuiplugin.client.dialog.MultiChatListener;
import com.calclab.emiteuiplugin.client.dialog.MultiChatPresenter;
import com.calclab.emiteuiplugin.client.params.AvatarProvider;
import com.calclab.emiteuiplugin.client.params.MultiChatCreationParam;
import com.calclab.emiteuiplugin.client.status.OwnPresence;
import com.calclab.emiteuiplugin.client.status.OwnPresence.OwnStatus;
import com.google.gwt.user.client.Window;

public class EmiteDialog {
    private static final String EMITE_DEF_TITLE = "Emite Chat";
    private MultiChatPresenter multiChatDialog;
    private final Xmpp xmpp;
    private final EmiteUIFactory factory;

    public EmiteDialog(final Xmpp xmpp, final EmiteUIFactory factory) {
        this.xmpp = xmpp;
        this.factory = factory;
    }

    public void chat(final XmppURI otherUserURI) {
        xmpp.getChatManager().openChat(otherUserURI, ChatUIStartedByMe.class, new ChatUIStartedByMe(true));
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

    public void refreshUserInfo(final UserChatOptions userChatOptions) {
        multiChatDialog.setUserChatOptions(userChatOptions);
    }

    public void show() {
        multiChatDialog.show();
    }

    public void show(final OwnStatus status) {
        show();
        multiChatDialog.setOwnPresence(new OwnPresence(status));
    }

    public void start(final String userJid, final String userPasswd, final String httpBase, final String roomHost) {
        start(new UserChatOptions(userJid, userPasswd, ("emiteui-" + new Date().getTime()), "blue",
                RosterManager.DEF_SUBSCRIPTION_MODE, true), httpBase, roomHost);
    }

    public void start(final String emiteDialogTitle, final UserChatOptions userChatOptions, final String httpBase,
            final String roomHost, final AvatarProvider avatarProvider, final MultiChatListener multiChatListener) {
        xmpp.setBoshOptions(new BoshOptions(httpBase));
        getChatDialog(new MultiChatCreationParam(emiteDialogTitle, roomHost, avatarProvider, userChatOptions,
                multiChatListener));
    }

    public void start(final UserChatOptions userChatOptions, final String httpBase, final String roomHost) {
        // We define, default AvatarProvider and MultiChaListener for simple
        // facade
        start(EMITE_DEF_TITLE, userChatOptions, httpBase, roomHost, new AvatarProvider() {
            public String getAvatarURL(final XmppURI userURI) {
                return "images/person-def.gif";
            }
        }, new MultiChatListener() {
            String initialWindowTitle = Window.getTitle();

            public void onConversationAttended(final String chatTitle) {
                Window.setTitle(initialWindowTitle);
            }

            public void onConversationUnnatended(final String chatTitle) {
                Window.setTitle("(* " + chatTitle + ") " + initialWindowTitle);
            }

            public void onShowUnavailableRosterItems(final boolean show) {
            }

            public void onUserColorChanged(final String color) {
            }

            public void onUserSubscriptionModeChanged(final SubscriptionMode subscriptionMode) {
            }

        });
    }

    private MultiChatPresenter createChatDialog(final MultiChatCreationParam param) {
        final MultiChatPresenter dialog = factory.createMultiChat(param);
        return dialog;
    }

}
