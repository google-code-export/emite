package com.calclab.emite.client.x.im.roster;

import java.util.List;

import com.calclab.emite.client.IContainer;
import com.calclab.emite.client.action.BussinessLogic;
import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.packet.stanza.IQ;
import com.calclab.emite.client.plugin.FilterBuilder;
import com.calclab.emite.client.plugin.Plugin2;
import com.calclab.emite.client.x.im.session.SessionPlugin;

public class RosterPlugin implements Plugin2 {

	public static Roster getRoster(final IContainer components) {
		return (Roster) components.get("roster");
	}

	final IQ requestRoster;
	final Roster roster;

	final BussinessLogic setRosterItems;

	public RosterPlugin() {
		roster = new Roster();

		requestRoster = new IQ("roster", IQ.Type.get).WithQuery("jabber:iq:roster");

		setRosterItems = new BussinessLogic() {
			public Packet logic(final Packet packet) {
				roster.clear();
				for (final Packet item : getItems(packet)) {
					roster.add(convert(item));
				}
				return null;
			}

			private RosterItem convert(final Packet item) {
				return new RosterItem(item.getAttribute("jid"), item.getAttribute("subscription"), item
						.getAttribute("name"));
			}

			private List<Packet> getItems(final Packet packet) {
				final List<Packet> items = packet.getFirstChildren("query").getChildren("item");
				return items;
			}
		};
	}

	public void start(final FilterBuilder when, final IContainer components) {
		components.register("roster", roster);

		when.Event(SessionPlugin.Events.started).send(requestRoster);

		when.IQ("roster").Do(setRosterItems);
	}

}
