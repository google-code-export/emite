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
package com.calclab.emiteuiplugin.client.dialog;

import com.calclab.emiteuiplugin.client.chat.ChatUI;

public interface MultiChatView {

    void addChat(ChatUI chat);

    void center();

    void clearInputText();

    void confirmCloseAll();

    void destroy();

    void focusInput();

    String getInputText();

    void hide();

    void highLight();

    void removeChat(ChatUI chatUI);

    void setAddRosterItemButtonVisible(boolean visible);

    void setCloseAllOptionEnabled(boolean enabled);

    void setEmoticonButtonEnabled(boolean enabled);

    void setInfoPanelVisible(boolean visible);

    void setInputEditable(boolean editable);

    void setInputText(String savedInput);

    void setJoinRoomEnabled(boolean b);

    void setLoadingVisible(boolean visible);

    void setOfflineInfo();

    void setOnlineInfo();

    void setOwnPresence(OwnPresence ownPresence);

    void setRosterVisible(boolean visible);

    void setSendEnabled(boolean enabled);

    void show();

    void showAlert(String message);

    void unHighLight();

}
