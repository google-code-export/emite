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
package com.calclab.emite.client.xmpp.session;

import static com.calclab.emite.client.core.dispatcher.matcher.Matchers.when;
import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;

/**
 * An base class to extend if you need components with the current user uri as
 * state information.
 * 
 * @author dani
 * 
 */
public class SessionComponent {
    protected final Emite emite;
    protected XmppURI userURI;

    public SessionComponent(final Emite emite) {
	this.emite = emite;
	this.userURI = null;
	install();
    }

    public boolean isLoggedIn() {
	return this.userURI != null;
    }

    public void logIn(final XmppURI uri) {
	this.userURI = uri;
    }

    public void logOut() {
	this.userURI = null;
    }

    private void install() {
	emite.subscribe(when(SessionManager.Events.onLoggedOut), new PacketListener() {
	    public void handle(final IPacket received) {
		logOut();
	    }
	});
	emite.subscribe(when(SessionManager.Events.onLoggedIn), new PacketListener() {
	    public void handle(final IPacket received) {
		logIn(uri(received.getAttribute("uri")));
	    }
	});
    }
}
