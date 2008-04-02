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

package com.calclab.examplechat.client.chatuiplugin.abstractchat;

import com.calclab.emite.client.xmpp.stanzas.XmppJID;

public class AbstractChatUser {
    private final String iconUrl;
    private final XmppJID jid;
    private String alias;
    private String color;

    public AbstractChatUser(final String iconUrl, final XmppJID jid, final String alias, final String color) {
        this.iconUrl = iconUrl;
        this.jid = jid;
        this.alias = alias;
        this.color = color;
    }

    public XmppJID getJid() {
        return jid;
    }

    public String getColor() {
        return color;
    }

    public String getAlias() {
        return alias;
    }

    public void setColor(final String color) {
        this.color = color;
    }

    public void setAlias(final String alias) {
        this.alias = alias;
    }

    public String getIconUrl() {
        return iconUrl;
    }

}
