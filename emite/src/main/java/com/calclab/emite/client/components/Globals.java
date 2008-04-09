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
package com.calclab.emite.client.components;

import com.calclab.emite.client.xmpp.stanzas.XmppURI;

// FIXME: Dani (de dani)
/**
 * creo que se podrían eliminar globals si, por ejemplo, con el evento
 * SessionManager.Events.loggedIn llega el ownURI... y así para cada parámetro
 * 
 * @author dani
 * 
 */
public interface Globals {
    String getDomain();

    XmppURI getOwnURI();

    String getPassword();

    String getResourceName();

    String getUserName();

    void setDomain(String domain);

    void setOwnURI(XmppURI uri);

    void setPassword(String password);

    void setResourceName(String resource);

    void setUserName(String userName);
}
