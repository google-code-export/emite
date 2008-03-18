package com.calclab.emite.client.x.im.roster;

import java.util.ArrayList;

public class Roster {
	private final ArrayList<RosterItem> items;
	private final ArrayList<RosterListener> listeners;

	public Roster() {
		listeners = new ArrayList<RosterListener>();
		items = new ArrayList<RosterItem>();
	}

	public void add(final RosterItem item) {
		items.add(item);
	}

	public void addListener(final RosterListener listener) {
		listeners.add(listener);
	}

	public void clear() {
		items.clear();
	}

	void fireRosterChanged() {
		for (final RosterListener listener : listeners) {
			listener.onRosterChanged(items);
		}
	}

}
