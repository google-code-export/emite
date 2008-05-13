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
package com.calclab.emite.client.xmpp.sasl;

import static com.calclab.emite.client.core.dispatcher.matcher.Matchers.when;
import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.xmpp.session.SessionManager;
import com.calclab.emite.client.xmpp.session.SessionManager.Events;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public class SASLManager {
    private static final String SEP = new String(new char[] { 0 });

    private static final String XMLNS = "urn:ietf:params:xml:ns:xmpp-sasl";

    protected String password;
    protected XmppURI uri;

    private boolean waitingForAuthorization;

    private final Emite emite;

    public SASLManager(final Emite emite) {
	this.emite = emite;
	install();
	clearState();
    }

    protected String encode(final String domain, final String userName, final String password) {
	final String auth = userName + "@" + domain + SEP + userName + SEP + password;
	return Base64Coder.encodeString(auth);
    }

    private void clearState() {
	uri = null;
	password = null;
	waitingForAuthorization = false;
    }

    private IPacket createAnonymousAuthorization() {
	final IPacket auth = new Packet("auth", XMLNS).With("mechanism", "ANONYMOUS");
	return auth;
    }

    private IPacket createPlainAuthorization() {
	final IPacket auth = new Packet("auth", XMLNS).With("mechanism", "PLAIN");
	final String encoded = encode(uri.getHost(), uri.getNode(), password);
	auth.setText(encoded);
	return auth;
    }

    private void install() {
	SessionManager.Signals.onDoLogin(emite, new PacketListener() {
	    public void handle(final IPacket received) {
		uri = uri(received.getAttribute("uri"));
		password = received.getAttribute("password");
	    }
	});

	SessionManager.Signals.onLoggedOut(emite, new PacketListener() {
	    public void handle(final IPacket received) {
		clearState();
	    }
	});

	emite.subscribe(when(SessionManager.Events.onLoggedOut), new PacketListener() {
	    public void handle(final IPacket received) {
		clearState();
	    }
	});

	emite.subscribe(when(SessionManager.Events.onDoAuthorization), new PacketListener() {
	    public void handle(final IPacket received) {
		startAuthorizationRequest();
		waitingForAuthorization = true;
	    }
	});

	emite.subscribe(when(new Packet("failure", XMLNS)), new PacketListener() {
	    public void handle(final IPacket received) {
		emite.publish(SessionManager.Events.onAuthorizationFailed);
	    }
	});

	emite.subscribe(when(new Packet("success")), new PacketListener() {
	    public void handle(final IPacket received) {
		if (waitingForAuthorization == true) {
		    clearState();
		    emite.publish(Events.onAuthorized);
		}
	    }
	});

    }

    private void startAuthorizationRequest() {
	final IPacket response = uri.hasNode() ? createPlainAuthorization() : createAnonymousAuthorization();
	emite.send(response);
    }
}
