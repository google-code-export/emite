package com.calclab.emite.client.im.roster;

import java.util.List;

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.bosh.EmiteComponent;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.stanzas.IQ;

public class RosterManager extends EmiteComponent {

	private final Roster roster;

	public RosterManager(final Emite emite, final Roster roster) {
		super(emite);
		this.roster = roster;
	}

	/**
	 * Upon connecting to the server and becoming an active resource, a client
	 * SHOULD request the roster BEFORE! sending initial presence
	 */
	@Override
	public void attach() {
		when(Session.Events.loggedIn, new PacketListener() {
			public void handle(final Packet received) {
				emite.send(new IQ("roster", IQ.Type.get).WithQuery("jabber:iq:roster"));
			}
		});

		when(new IQ("roster", IQ.Type.result, null), new PacketListener() {
			public void handle(final Packet received) {
				setRosterItems(roster, received);
				emite.publish(Roster.Events.ready);
			}
		});

	}

	private RosterItem convert(final Packet item) {
		return new RosterItem(item.getAttribute("jid"), item.getAttribute("subscription"), item.getAttribute("name"));
	}

	private List<? extends Packet> getItems(final Packet packet) {
		final List<? extends Packet> items = packet.getFirstChild("query").getChildren();
		return items;
	}

	private void setRosterItems(final Roster roster, final Packet received) {
		roster.clear();
		for (final Packet item : getItems(received)) {
			roster.add(convert(item));
		}
		roster.fireRosterInitialized();
	}
}
