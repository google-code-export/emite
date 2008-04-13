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
package com.calclab.emite.client.extra.muc;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public class RoomUser {
    // FIXME: (Dani) Echa un vistazo a GroupChatUser extends AbstractUser (igual
    // te vale para
    // algo)

    // FIXEM: (Dani) user Alias?

    private final XmppURI uri;
    private final String affiliation;
    private final String role;

    public RoomUser(final XmppURI uri, final String affiliation, final String role) {
        this.uri = uri;
        this.affiliation = affiliation;
        this.role = role;
    }

    public XmppURI getUri() {
        return uri;
    }

    @Override
    public String toString() {
        return uri.toString() + "(" + affiliation + "," + role + ")";
    }
}
