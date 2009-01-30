/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emiteuimodule.client.status;

import org.ourproject.kune.platf.client.View;

import com.calclab.emiteuimodule.client.SubscriptionMode;
import com.calclab.emiteuimodule.client.UserChatOptions;
import com.calclab.suco.client.events.Listener;

public interface StatusUI {

    void addButtonItem(View item);

    void addChatMenuItem(View item);

    void addOptionsSubMenuItem(View item);

    void addToolbarItem(View item);

    void confirmCloseAll();

    View getView();

    void onAfterLogin(Listener<StatusUI> listener);

    void onAfterLogout(Listener<StatusUI> listener);

    void onCloseAllConfirmed(Listener<StatusUI> listener);

    void onUserColorChanged(Listener<String> listener);

    void onUserSubscriptionModeChanged(Listener<SubscriptionMode> listener);

    void setCloseAllOptionEnabled(boolean enabled);

    void setCurrentUserChatOptions(UserChatOptions userChatOptions);

    void setEnable(boolean enable);

    void setOwnPresence(OwnPresence ownPresence);

}
