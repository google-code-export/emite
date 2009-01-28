/**
 *
 */
package com.calclab.emite.j2se.swing.roster;

import com.calclab.emite.im.client.roster.RosterItem;

public class RosterItemWrapper {
    final RosterItem item;
    final String name;

    public RosterItemWrapper(final String name, final RosterItem item) {
	this.item = item;
	this.name = name;
    }

    @Override
    public String toString() {
	String value = item.getJID() + "(name: " + name + ")";
	value += item.isAvailable() ? "online" : "offline";
	value += " - show: " + item.getShow() + " - status: " + item.getStatus();
	value += " - state: " + item.getSubscriptionState();
	value += " - ask: " + item.getAsk();
	return value;
    }
}
