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
package com.calclab.emiteuimodule.client.users;

import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.emiteuimodule.client.utils.ChatIconDescriptor;

public class ChatUserUI extends AbstractChatUser {

    private ChatIconDescriptor statusIcon;

    private String statusText;

    private boolean visible;

    public ChatUserUI(final String iconUrl, final RosterItem rosterItem, final String color) {
        super(iconUrl, rosterItem.getJID(), rosterItem.getName(), color);
    }

    public ChatIconDescriptor getStatusIcon() {
        return statusIcon;
    }

    public String getStatusText() {
        return statusText;
    }

    public boolean getVisible() {
        return visible;
    }

    public void setStatusIcon(final ChatIconDescriptor statusIcon) {
        this.statusIcon = statusIcon;
    }

    public void setStatusText(final String statusText) {
        this.statusText = statusText;
    }

    public void setVisible(final boolean visible) {
        this.visible = visible;
    }

}
