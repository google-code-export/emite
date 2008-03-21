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

package com.calclab.examplechat.client.chatuiplugin;

import org.ourproject.kune.platf.client.View;

import com.calclab.examplechat.client.chatuiplugin.RoomUser.UserType;

public interface Room {

    String getName();

    void setSubject(String subject);

    // FIXME: creo que sería más logico cambiarlo por addUser(RoomUser user);
    void addUser(String userAlias, UserType moderador);

    void addMessage(String userAlias, String body);

    void addInfoMessage(String message);

    void addDelimiter(String date);

    // void setHandler(XmppRoom handler);

    // XmppRoom getHandler();

    String getSessionAlias();

    void clearSavedInput();

    void saveInput(String inputText);

    String getSavedInput();

    String getSubject();

    RoomUserListView getUsersListView();

    View getView();

    boolean isReady();

    void removeUser(String alias);

    void activate();

    UserType getUserType();

    void setUserType(UserType userType);

    void setUserAlias(String userAlias);

    void setRoomName(String roomName);

}
