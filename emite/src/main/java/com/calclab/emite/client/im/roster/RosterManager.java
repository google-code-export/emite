package com.calclab.emite.client.im.roster;

import java.util.List;

import com.calclab.emite.client.components.Answer;
import com.calclab.emite.client.components.SenderComponent;
import com.calclab.emite.client.core.bosh.Connection;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.im.session.Session;
import com.calclab.emite.client.xmpp.stanzas.IQ;

public class RosterManager extends SenderComponent {

	private final Roster roster;

	public RosterManager(final Dispatcher dispatcher, final Connection connection, final Roster roster) {
		super(dispatcher, connection);
		this.roster = roster;
	}

	/**
	 * Upon connecting to the server and becoming an active resource, a client
	 * SHOULD request the roster BEFORE! sending initial presence
	 */
	@Override
	public void attach() {
		when(Session.Events.login).Send(new Answer() {
			public Packet respondTo(final Packet received) {
				return new IQ("roster", IQ.Type.get).WithQuery("jabber:iq:roster");
			}
		});
		when(new IQ("roster", IQ.Type.result, null)).Publish(new Answer() {
			public Packet respondTo(final Packet received) {
				setRosterItems(roster, received);
				return Roster.Events.ready;
			}

		});
	}

	private RosterItem convert(final Packet item) {
		return new RosterItem(item.getAttribute("jid"), item.getAttribute("subscription"), item.getAttribute("name"));
	}

	private List<Packet> getItems(final Packet packet) {
		final List<Packet> items = packet.getFirstChild("query").getChildren("item");
		return items;
	}

	private void setRosterItems(final Roster roster, final Packet received) {
		roster.clear();
		for (final Packet item : getItems(received)) {
			roster.add(convert(item));
		}
	}
}
