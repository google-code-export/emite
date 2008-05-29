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
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.core.signal.Listener;
import com.calclab.emite.client.core.signal.Signal;
import com.calclab.emite.client.xmpp.sasl.AuthorizationTicket.State;

public class SASLManager {
    private static final String SEP = new String(new char[] { 0 });
    private static final String XMLNS = "urn:ietf:params:xml:ns:xmpp-sasl";

    private final Emite emite;
    private final Signal<AuthorizationTicket> onAuthorized;
    private AuthorizationTicket inProgressTicket;

    public SASLManager(final Emite emite) {
	this.emite = emite;
	this.onAuthorized = new Signal<AuthorizationTicket>();
	install();
    }

    public void onAuthorized(final Listener<AuthorizationTicket> listener) {
	onAuthorized.add(listener);
    }

    public void sendAuthorizationRequest(final AuthorizationTicket authorizationTicket) {
	this.inProgressTicket = authorizationTicket;
	final IPacket response = authorizationTicket.uri.hasNode() ? createPlainAuthorization(authorizationTicket)
		: createAnonymousAuthorization();
	emite.send(response);
	inProgressTicket.setState(State.waitingForAuthorization);
    }

    private IPacket createAnonymousAuthorization() {
	final IPacket auth = new Packet("auth", XMLNS).With("mechanism", "ANONYMOUS");
	return auth;
    }

    private IPacket createPlainAuthorization(final AuthorizationTicket authorizationTicket) {
	final IPacket auth = new Packet("auth", XMLNS).With("mechanism", "PLAIN");
	final String encoded = encode(authorizationTicket.uri.getHost(), authorizationTicket.uri.getNode(),
		authorizationTicket.getPassword());
	auth.setText(encoded);
	return auth;
    }

    private String encode(final String domain, final String userName, final String password) {
	final String auth = userName + "@" + domain + SEP + userName + SEP + password;
	return Base64Coder.encodeString(auth);
    }

    private void install() {
	emite.subscribe(when(new Packet("failure", XMLNS)), new PacketListener() {
	    public void handle(final IPacket received) {
		inProgressTicket.setState(State.failed);
		onAuthorized.fire(inProgressTicket);
		inProgressTicket = null;
	    }
	});

	emite.subscribe(when(new Packet("success")), new PacketListener() {
	    public void handle(final IPacket received) {
		inProgressTicket.setState(State.succeed);
		onAuthorized.fire(inProgressTicket);
		inProgressTicket = null;
	    }
	});
    }
}
