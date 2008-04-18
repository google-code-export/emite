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
package com.calclab.emiteui.client.emiteuiplugin.dialog;

import com.calclab.emite.client.im.chat.Chat;
import com.calclab.emiteui.client.emiteuiplugin.UserChatOptions;

public interface MultiChat {

    public void activateChat(Chat chat);

    public void closeAllChats(final boolean withConfirmation);

    public void destroy();

    public void groupChatSubjectChanged(final Chat groupChat, String newSubject);

    public void hide();

    public void setOwnPresence(OwnPresence ownPresence);

    public void setUserChatOptions(UserChatOptions param);

    public void show();

}
