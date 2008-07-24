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
package com.calclab.emite.client.xep.disco;

import java.util.ArrayList;
import java.util.List;

import com.calclab.emite.client.core.packet.Filters;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.PacketFilter;
import com.calclab.emite.client.xmpp.session.ISession;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.IQ.Type;
import com.calclab.suco.client.signal.Signal;
import com.calclab.suco.client.signal.Slot;

public class DiscoveryManager {
    private final Signal<DiscoveryManager> onReady;
    private final PacketFilter filterQuery;
    private ArrayList<Feature> features;
    private ArrayList<Identity> identities;
    private final ISession sessionImpl;

    public DiscoveryManager(final ISession sessionImpl) {
	this.sessionImpl = sessionImpl;
	this.onReady = new Signal<DiscoveryManager>("onReady");
	this.filterQuery = Filters.byNameAndXMLNS("query", "http://jabber.org/protocol/disco#info");
	sessionImpl.onLoggedIn(new Slot<XmppURI>() {
	    public void onEvent(final XmppURI uri) {
		sendDiscoQuery(uri);
	    }

	});
    }

    public void onReady(final Slot<DiscoveryManager> listener) {
	onReady.add(listener);
    }

    public void sendDiscoQuery(final XmppURI uri) {
	final IQ iq = new IQ(Type.get, uri, uri.getHostURI());
	iq.addQuery("http://jabber.org/protocol/disco#info");
	sessionImpl.sendIQ("disco", iq, new Slot<IPacket>() {
	    public void onEvent(final IPacket response) {
		final IPacket query = response.getFirstChild(filterQuery);
		processIdentity(query.getChildren(Filters.byName("identity")));
		processFeatures(query.getChildren(Filters.byName("features")));
	    }
	});
    }

    private void processFeatures(final List<? extends IPacket> children) {
	this.features = new ArrayList<Feature>();
	for (final IPacket child : children) {
	    features.add(Feature.fromPacket(child));
	}
    }

    private void processIdentity(final List<? extends IPacket> children) {
	this.identities = new ArrayList<Identity>();
	for (final IPacket child : children) {
	    identities.add(Identity.fromPacket(child));
	}
    }

}
