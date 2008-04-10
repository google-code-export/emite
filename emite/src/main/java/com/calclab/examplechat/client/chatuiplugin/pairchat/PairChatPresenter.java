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
package com.calclab.examplechat.client.chatuiplugin.pairchat;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.examplechat.client.chatuiplugin.abstractchat.AbstractChat;
import com.calclab.examplechat.client.chatuiplugin.abstractchat.AbstractChatPresenter;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatView;
import com.calclab.examplechat.client.chatuiplugin.utils.XmppJID;

public class PairChatPresenter extends AbstractChatPresenter implements PairChat {

    private final PairChatUser otherUser;
    final PairChatListener listener;
    private String currenUserColor;
    private final XmppJID currentSessionJid;

    public PairChatPresenter(final Chat chat, final PairChatListener listener, final XmppJID currentSessionJid,
            final PairChatUser otherUser) {
        super(chat, AbstractChat.Type.pairchat);
        this.currentSessionJid = currentSessionJid;
        this.currenUserColor = MultiChatView.DEF_USER_COLOR;
        this.otherUser = otherUser;
        this.input = "";
        this.listener = listener;
    }

    public void addMessage(final XmppJID userJid, final String message) {
        String userColor;

        String userAlias;
        if (currentSessionJid.equals(userJid)) {
            userColor = currenUserColor;
            userAlias = currentSessionJid.getNode();
        } else if (otherUser.getJid().equals(userJid)) {
            userColor = otherUser.getColor();
            // FIXME: Duplicate code in UserGridPanel
            userAlias = otherUser.getAlias() != null ? otherUser.getAlias() : otherUser.getJid().getNode();
        } else {
            final String error = "Unexpected message from user '" + userJid + "' in " + "chat '" + otherUser.getJid();
            Log.error(error);
            throw new RuntimeException(error);
        }
        view.addMessage(userAlias, userColor, message);
        listener.onMessageAdded(this);
    }

    public PairChatUser getOtherUser() {
        return otherUser;
    }

    public void init(final PairChatView view) {
        this.view = view;
        closeConfirmed = false;
    }

    public void onActivated() {
        listener.onActivate(this);
    }

    public void onDeactivate() {
        listener.onDeactivate(this);
    }

    public void setSessionUserColor(final String color) {
        this.currenUserColor = color;
    }

}
