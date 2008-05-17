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
/**
 * 
 */
package com.calclab.emiteuiplugin.client.status;

import java.util.ArrayList;

import com.calclab.emite.client.im.roster.RosterManager.SubscriptionMode;

@SuppressWarnings("serial") class StatusPanelListenerCollection extends ArrayList<StatusPanelListener> implements
        StatusPanelListener {

    public void onCloseAllConfirmed() {
        for (final StatusPanelListener listener : this) {
    	listener.onCloseAllConfirmed();
        }
    }

    public void onJoinRoom() {
        for (final StatusPanelListener listener : this) {
    	listener.onJoinRoom();
        }
    }

    public void onUserColorChanged(final String color) {
        for (final StatusPanelListener listener : this) {
    	listener.onUserColorChanged(color);
        }
    }

    public void onUserSubscriptionModeChanged(final SubscriptionMode mode) {
        for (final StatusPanelListener listener : this) {
    	listener.onUserSubscriptionModeChanged(mode);
        }
    }

    public void setOwnPresence(final OwnPresence ownPresence) {
        for (final StatusPanelListener listener : this) {
    	listener.setOwnPresence(ownPresence);
        }
    }

}