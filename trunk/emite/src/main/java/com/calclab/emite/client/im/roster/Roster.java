package com.calclab.emite.client.im.roster;

import java.util.ArrayList;

import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.packet.BasicPacket;
import com.calclab.emite.client.core.packet.Packet;

public class Roster {
    private final ArrayList<RosterItem> items;
    private final ArrayList<RosterListener> listeners;
    private final Dispatcher dispatcher;

    public Roster(final Dispatcher dispatcher) {
	this.dispatcher = dispatcher;
	listeners = new ArrayList<RosterListener>();
	items = new ArrayList<RosterItem>();
    }

    public void addListener(final RosterListener listener) {
	listeners.add(listener);
    }

    public void clear() {
	items.clear();
    }

    public RosterItem getItem(final int index) {
	return items.get(index);
    }

    public int getSize() {
	return items.size();
    }

    public void requestAddItem(final String uri, final String name, final String group) {
	final Packet item = new BasicPacket("item").With("jid", uri).With("name", name);
	if (group != null) {
	    item.addChild(new BasicPacket("group").WithText(group));
	}
	dispatcher.publish(RosterManager.Events.addItem.With(item));
    }

    void add(final RosterItem item) {
	items.add(item);
    }

    void fireRosterInitialized() {
	for (final RosterListener listener : listeners) {
	    listener.onRosterInitialized(items);
	}
    }

}
