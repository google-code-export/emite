package com.calclab.emite.client.im.roster;

import java.util.ArrayList;

import com.calclab.emite.client.Engine;
import com.calclab.emite.client.packet.stanza.IQ;
import com.calclab.emite.client.packet.stanza.IQType;

public class Roster {
	private final Engine engine;
	private final ArrayList<RosterListener> listeners;

	public Roster(final Engine engine) {
		this.engine = engine;
		listeners = new ArrayList<RosterListener>();
	}

	public void addListener(final RosterListener listener) {
		listeners.add(listener);
	}

	void onPresence() {
	}

	void onSessionStarted() {
		final IQ iq = new IQ("roster_1", IQType.get);
		iq.setQuery("jabber:iq:roster");
		engine.send(iq);
	}
}
