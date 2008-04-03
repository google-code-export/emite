/*
 * Copyright (C) 2007 The kune development team (see CREDITS for details)
 * This file is part of kune.
 *
 * Kune is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * Kune is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.calclab.examplechat.client.chatuiplugin.pairchat;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.examplechat.client.chatuiplugin.abstractchat.AbstractChatPresenter;

public class PairChatPresenter extends AbstractChatPresenter implements PairChat {

    private final PairChatUser otherUser;
    final PairChatListener listener;

    public PairChatPresenter(final Chat chat, final PairChatListener listener, final PairChatUser currentSessionUser,
	    final PairChatUser otherUser) {
	super(chat, currentSessionUser, TYPE_PAIR_CHAT);
	this.otherUser = otherUser;
	this.input = "";
	this.listener = listener;
    }

    public void addMessage(final XmppURI userJid, final String message) {
	String userColor;

	if (sessionUser.getJid().equals(userJid)) {
	    userColor = sessionUser.getColor();
	} else if (otherUser.getJid().equals(userJid)) {
	    userColor = otherUser.getColor();
	} else {
	    final String error = "Unexpected message from user '" + userJid + "' in " + "chat '" + otherUser.getJid();
	    Log.error(error);
	    throw new RuntimeException(error);
	}
	view.addMessage(userJid.toString(), userColor, message);
	listener.onMessageReceived(this);
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

}
