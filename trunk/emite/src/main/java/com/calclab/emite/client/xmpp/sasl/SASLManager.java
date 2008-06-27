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
import com.calclab.emite.client.xmpp.sasl.AuthorizationTransaction.State;
import com.calclab.suco.client.signal.Signal;
import com.calclab.suco.client.signal.Slot;

public class SASLManager {
    private static final String SEP = new String(new char[] { 0 });
    private static final String XMLNS = "urn:ietf:params:xml:ns:xmpp-sasl";

    private final Emite emite;
    private final Signal<AuthorizationTransaction> onAuthorized;
    private AuthorizationTransaction currentTransaction;

    public SASLManager(final Emite emite) {
	this.emite = emite;
	this.onAuthorized = new Signal<AuthorizationTransaction>("onAuthorized");
	install();
    }

    public void onAuthorized(final Slot<AuthorizationTransaction> listener) {
	onAuthorized.add(listener);
    }

    public void sendAuthorizationRequest(final AuthorizationTransaction authorizationTransaction) {
	this.currentTransaction = authorizationTransaction;
	final IPacket response = isAnonymous(authorizationTransaction) ? createAnonymousAuthorization()
		: createPlainAuthorization(authorizationTransaction);
	emite.send(response);
	currentTransaction.setState(State.waitingForAuthorization);
    }

    private IPacket createAnonymousAuthorization() {
	final IPacket auth = new Packet("auth", XMLNS).With("mechanism", "ANONYMOUS");
	return auth;
    }

    private IPacket createPlainAuthorization(final AuthorizationTransaction authorizationTransaction) {
	final IPacket auth = new Packet("auth", XMLNS).With("mechanism", "PLAIN");
	final String encoded = encode(authorizationTransaction.uri.getHost(), authorizationTransaction.uri.getNode(),
		authorizationTransaction.getPassword());
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
		currentTransaction.setState(State.failed);
		onAuthorized.fire(currentTransaction);
		currentTransaction = null;
	    }
	});

	emite.subscribe(when(new Packet("success")), new PacketListener() {
	    public void handle(final IPacket received) {
		currentTransaction.setState(State.succeed);
		onAuthorized.fire(currentTransaction);
		currentTransaction = null;
	    }
	});
    }

    private boolean isAnonymous(final AuthorizationTransaction authorizationTransaction) {
	return "anonymous".equals(authorizationTransaction.uri.toString());
    }
}
