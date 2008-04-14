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

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.bosh.EmiteComponent;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.xmpp.session.SessionManager;
import com.calclab.emite.client.xmpp.session.SessionManager.Events;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public class SASLManager extends EmiteComponent {
    private static final String SEP = new String(new char[] { 0 });

    private static final String XMLNS = "urn:ietf:params:xml:ns:xmpp-sasl";

    protected String password;
    protected XmppURI uri;

    public SASLManager(final Emite emite) {
	super(emite);

    }

    @Override
    public void attach() {
	when(SessionManager.Events.logIn, new PacketListener() {
	    public void handle(final IPacket received) {
		uri = XmppURI.parse(received.getAttribute("uri"));
		password = received.getAttribute("password");
	    }
	});

	when(new Packet("stream:features"), new PacketListener() {
	    public void handle(final IPacket received) {
		startAuthorizationRequest();
	    }

	});

	when(new Packet("success", XMLNS), new PacketListener() {
	    public void handle(final IPacket received) {
		uri = null;
		password = null;
		emite.publish(Events.authorized);
	    }
	});

    }

    protected String encode(final String domain, final String userName, final String password) {
	final String auth = userName + "@" + domain + SEP + userName + SEP + password;
	return Base64Coder.encodeString(auth);
    }

    private IPacket createAnonymousAuthorization() {
	final IPacket auth = new Packet("auth", XMLNS).With("mechanism", "ANONYMOUS");
	return auth;
    }

    private IPacket createPlainAuthorization() {
	final IPacket auth = new Packet("auth", XMLNS).With("mechanism", "PLAIN");
	final String encoded = encode(uri.getHost(), uri.getNode(), password);
	auth.addText(encoded);
	return auth;
    }

    private void startAuthorizationRequest() {
	final String userName = uri.getNode();
	final boolean hasUserName = userName == null || userName.trim().length() > 0;
	final IPacket response = hasUserName ? createPlainAuthorization() : createAnonymousAuthorization();
	emite.send(response);
    }
}
