package com.calclab.emite.client.x.im.roster;

import java.util.List;

import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.packet.stanza.IQ;
import com.calclab.emite.client.plugin.dsl.Answer;

public class RosterManager {
	final Packet requestRoster;
	final Answer setRosterItems;

	public RosterManager(final Roster roster) {

		requestRoster = new IQ("roster", IQ.Type.get).WithQuery("jabber:iq:roster");

		setRosterItems = new Answer() {
			public Packet respondTo(final Packet received) {
				setRosterItems(roster, received);
				return Roster.Events.received;
			}

		};
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
