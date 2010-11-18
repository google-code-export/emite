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
package com.calclab.emiteuimodule.client.roster;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emiteuimodule.client.users.ChatUserUI;
import com.calclab.emiteuimodule.client.users.UserGridMenuItemList;

public interface RosterUIView {

    public void addRosterItem(final ChatUserUI user, final UserGridMenuItemList menuItemList);

    public void clearRoster();

    public void informRosterItemRemoved(final ChatUserUI user);

    public void removeRosterItem(final ChatUserUI user);

    public void showMessageAboutUnsuscription(final XmppURI userUnsubscribed);

    public void updateRosterItem(final ChatUserUI user, final UserGridMenuItemList menuItemList);

}
