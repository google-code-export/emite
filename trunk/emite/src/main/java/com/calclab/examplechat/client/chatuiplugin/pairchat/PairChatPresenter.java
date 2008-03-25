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

import com.calclab.examplechat.client.chatuiplugin.AbstractChatPresenter;

public class PairChatPresenter extends AbstractChatPresenter implements PairChat {

    final PairChatListener listener;
    private final PairChatUser otherUser;

    public PairChatPresenter(final PairChatListener listener, final PairChatUser currentSessionUser,
            final PairChatUser otherUser) {
        super(currentSessionUser, TYPE_PAIR_CHAT);
        this.otherUser = otherUser;
        this.input = "";
        this.listener = listener;
        // Currently simple colors:
        currentSessionUser.setColor("blue");
        otherUser.setColor("green");
    }

    public void init(final PairChatView view) {
        this.view = view;
        closeConfirmed = false;
    }

    public void addMessage(final String userAlias, final String message) {
        String userColor;

        if (sessionUser.equals(userAlias)) {
            userColor = sessionUser.getColor();
        } else {
            userColor = otherUser.getColor();
        }
        view.showMessage(userAlias, userColor, message);
        listener.onMessageReceived(this);
    }

    public PairChatUser getOtherUser() {
        return otherUser;
    }

    public void onActivated() {
        listener.onActivate(this);
    }

}
