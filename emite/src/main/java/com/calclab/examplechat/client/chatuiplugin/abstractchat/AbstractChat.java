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
package com.calclab.examplechat.client.chatuiplugin.abstractchat;

import org.ourproject.kune.platf.client.View;

import com.calclab.emite.client.im.chat.Chat;

public interface AbstractChat {

    final int TYPE_GROUP_CHAT = 2;

    final int TYPE_PAIR_CHAT = 1;

    void activate();

    void addDelimiter(String date);

    void addInfoMessage(String message);

    void clearSavedInput();

    void doClose();

    Chat getChat();

    String getChatTitle();

    String getSavedInput();

    String getSessionUserAlias();

    int getType();

    View getView();

    void saveInput(String inputText);

    void saveOtherProperties();

    void setChatTitle(String chatTitle);

    void setSessionUserColor(String color);

}
