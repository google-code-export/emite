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

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;

public class AbstractChatUser {
    private String alias;
    private String color;
    private final String iconUrl;
    private final XmppURI uri;

    public AbstractChatUser(final String iconUrl, final XmppURI uri, final String alias, final String color) {
        this.iconUrl = iconUrl;
        this.uri = uri;
        this.alias = alias;
        this.color = color;
    }

    public String getAlias() {
        return alias;
    }

    public String getColor() {
        return color;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public XmppURI getURI() {
        return uri;
    }

    public void setAlias(final String alias) {
        this.alias = alias;
    }

    public void setColor(final String color) {
        this.color = color;
    }

}
