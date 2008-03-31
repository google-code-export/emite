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

package com.calclab.examplechat.client.chatuiplugin.abstractchat;

import org.ourproject.kune.platf.client.View;

public interface AbstractChat {

    final int TYPE_PAIR_CHAT = 1;

    final int TYPE_GROUP_CHAT = 2;

    void activate();

    void addDelimiter(String date);

    void addInfoMessage(String message);

    void clearSavedInput();

    String getSavedInput();

    View getView();

    void saveInput(String inputText);

    void setChatTitle(String chatTitle);

    String getChatTitle();

    int getType();

    String getSessionUserAlias();

    void saveOtherProperties();

    void setSessionUserColor(String color);

    void doClose();

    String getId();

}
