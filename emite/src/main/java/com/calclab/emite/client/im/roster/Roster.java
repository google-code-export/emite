package com.calclab.emite.client.im.roster;

import java.util.ArrayList;
import java.util.List;

import com.calclab.emite.client.Engine;
import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.packet.stanza.IQ;
import com.calclab.emite.client.packet.stanza.IQType;

public class Roster {
	private final Engine engine;
	private final ArrayList<RosterListener> listeners;
	private final ArrayList<RosterItem> roster;

	public Roster(final Engine engine) {
		this.engine = engine;
		listeners = new ArrayList<RosterListener>();
		roster = new ArrayList<RosterItem>();
	}

	public void addListener(final RosterListener listener) {
		listeners.add(listener);
	}

	public void onRosterReceived(final IQ iq) {
		roster.clear();
		final List<Packet> items = iq.getFirstChildren("query").getChildren("item");
		for (final Packet item : items) {
			roster.add(new RosterItem(item.getAttribute("jid"), item.getAttribute("subscription"), item
					.getAttribute("name")));
		}
		for (final RosterListener listener : listeners) {
			listener.onRosterChanged(roster);
		}
	}

	void onPresence() {
	}

	void onSessionStarted() {
		final IQ iq = new IQ("roster", IQType.get);
		iq.setQuery("jabber:iq:roster");
		engine.send(iq);
	}
}
