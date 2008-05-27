package com.calclab.emite.client.xep.disco;

import java.util.ArrayList;
import java.util.List;

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.Filters;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.PacketFilter;
import com.calclab.emite.client.core.signals.Signal;
import com.calclab.emite.client.xmpp.session.SessionComponent;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.IQ.Type;

public class DiscoveryManager extends SessionComponent {
    public final Signal<DiscoveryManager> onReady;
    private final PacketFilter filterQuery;
    private ArrayList<Feature> features;
    private ArrayList<Identity> identities;

    public DiscoveryManager(final Emite emite) {
	super(emite);
	this.onReady = new Signal<DiscoveryManager>();
	this.filterQuery = Filters.byNameAndXMLNS("query", "http://jabber.org/protocol/disco#info");
    }

    @Override
    public void logIn(final XmppURI uri) {
	super.logIn(uri);
	sendDiscoQuery(uri);
    }

    public void sendDiscoQuery(final XmppURI uri) {
	final IQ iq = new IQ(Type.get, uri, uri.getHostURI());
	iq.addQuery("http://jabber.org/protocol/disco#info");
	emite.sendIQ("disco", iq, new PacketListener() {
	    public void handle(final IPacket received) {
		final IPacket query = received.getFirstChild(filterQuery);
		processIdentity(query.getChildren(Filters.byName("identity")));
		processFeatures(query.getChildren(Filters.byName("features")));
	    }
	});
    }

    private void processFeatures(final List<? extends IPacket> children) {
	this.features = new ArrayList<Feature>();
	for (final IPacket child : children)
	    features.add(Feature.fromPacket(child));
    }

    private void processIdentity(final List<? extends IPacket> children) {
	this.identities = new ArrayList<Identity>();
	for (final IPacket child : children)
	    identities.add(Identity.fromPacket(child));
    }

}
