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

import java.util.Collection;
import java.util.Date;

import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.core.bosh.BoshOptions;
import com.calclab.emite.client.im.roster.RosterItem;
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
import com.calclab.modular.client.signal.Listener;
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
        checkIfDialogIsStarted();
        multiChatDialog.closeAllChats(withConfirmation);
    }

    public void hide() {
        checkIfDialogIsStarted();
        multiChatDialog.hide();
    }

    public boolean isDialogNotStarted() {
        return multiChatDialog == null;
    }

    public boolean isVisible() {
        checkIfDialogIsStarted();
        return multiChatDialog.isVisible();
    }

    public void joinRoom(final XmppURI roomURI) {
        xmpp.getInstance(RoomManager.class).openChat(roomURI, ChatUIStartedByMe.class, new ChatUIStartedByMe(true));
    }

    public void onChatAttended(final Listener<String> listener) {
        checkIfDialogIsStarted();
        multiChatDialog.onChatAttended(listener);
    }

    public void onChatUnattendedWithActivity(final Listener<String> listener) {
        checkIfDialogIsStarted();
        multiChatDialog.onChatUnattendedWithActivity(listener);
    }

    public void onRosterChanged(final Listener<Collection<RosterItem>> listener) {
        xmpp.getRoster().onRosterChanged(listener);
    }

    public void onRosterItemChanged(final Listener<RosterItem> listener) {
        xmpp.getRoster().onItemChanged(listener);
    }

    public void onShowUnavailableRosterItemsChanged(final Listener<Boolean> listener) {
        checkIfDialogIsStarted();
        multiChatDialog.onShowUnavailableRosterItemsChanged(listener);
    }

    public void onUserColorChanged(final Listener<String> listener) {
        checkIfDialogIsStarted();
        multiChatDialog.onUserColorChanged(listener);
    }

    public void onUserSubscriptionModeChanged(final Listener<SubscriptionMode> listener) {
        checkIfDialogIsStarted();
        multiChatDialog.onUserSubscriptionModeChanged(listener);
    }

    public void refreshUserInfo(final UserChatOptions userChatOptions) {
        checkIfDialogIsStarted();
        multiChatDialog.setUserChatOptions(userChatOptions);
    }

    public void setOwnPresence(final OwnStatus status) {
        checkIfDialogIsStarted();
        multiChatDialog.setOwnPresence(new OwnPresence(status));
    }

    public void show() {
        checkIfDialogIsStarted();
        multiChatDialog.show();
    }

    public void show(final OwnStatus status) {
        checkIfDialogIsStarted();
        show();
        setOwnPresence(status);
    }

    public void start(final String userJid, final String userPasswd, final String httpBase, final String roomHost) {
        start(new UserChatOptions(userJid, userPasswd, ("emiteui-" + new Date().getTime()), "blue",
                RosterManager.DEF_SUBSCRIPTION_MODE, true), httpBase, roomHost);
    }

    public void start(final UserChatOptions userChatOptions, final String httpBase, final String roomHost) {
        // We define, default AvatarProvider and MultiChaListener for simple
        // facade
        start(userChatOptions, httpBase, roomHost, new AvatarProvider() {
            public String getAvatarURL(final XmppURI userURI) {
                return "images/person-def.gif";
            }
        }, EMITE_DEF_TITLE);
        final String initialWindowTitle = Window.getTitle();
        onChatAttended(new Listener<String>() {
            public void onEvent(final String parameter) {
                Window.setTitle(initialWindowTitle);
            }
        });
        onChatUnattendedWithActivity(new Listener<String>() {
            public void onEvent(final String chatTitle) {
                Window.setTitle("(* " + chatTitle + ") " + initialWindowTitle);
            }
        });
    }

    public void start(final UserChatOptions userChatOptions, final String httpBase, final String roomHost,
            final AvatarProvider avatarProvider, final String emiteDialogTitle) {
        xmpp.setBoshOptions(new BoshOptions(httpBase));
        multiChatDialog = createChatDialog(new MultiChatCreationParam(emiteDialogTitle, roomHost, avatarProvider,
                userChatOptions));
        ImagesHelper.preFetchImages();
    }

    protected void checkIfDialogIsStarted() {
        if (isDialogNotStarted()) {
            new RuntimeException("Emite UI dialog is not created (use 'start' method before)");
        }
    }

    private MultiChatPresenter createChatDialog(final MultiChatCreationParam param) {
        final MultiChatPresenter dialog = factory.createMultiChat(param);
        return dialog;
    }

}
