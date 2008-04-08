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
package com.calclab.examplechat.client.chatuiplugin.pairchat;

import com.calclab.emite.client.im.roster.RosterItem;
import com.calclab.emite.client.im.roster.RosterItem.Subscription;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.examplechat.client.chatuiplugin.abstractchat.AbstractChatUser;
import com.calclab.examplechat.client.chatuiplugin.dialog.MultiChatView;

public class PairChatUser extends AbstractChatUser {

    private final RosterItem rosterItem;

    public PairChatUser(final String iconUrl, final RosterItem rosterItem, final String color) {
        super(iconUrl, rosterItem.getXmppURI(), rosterItem.getName(), color);
        this.rosterItem = rosterItem;
    }

    public PairChatUser(final String string, final RosterItem item) {
        this(string, item, MultiChatView.DEF_PAIR_USER_COLOR);
    }

    public Presence getPresence() {
        return rosterItem.getPresence();
    }

    public Subscription getSubscription() {
        return rosterItem.getSubscription();
    }

}
