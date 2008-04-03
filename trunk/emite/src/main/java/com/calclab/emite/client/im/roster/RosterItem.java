package com.calclab.emite.client.im.roster;

import java.util.ArrayList;
import java.util.List;

public class RosterItem {

	private final ArrayList<String> groups;
	private final String jid;
	private final String name;
	private final String subscription;

	public RosterItem(final String jid, final String subscription, final String name) {
		this.jid = jid;
		this.subscription = subscription;
		this.name = name;
		this.groups = new ArrayList<String>();
	}

	public List<String> getGroups() {
		return groups;
	}

	public String getXmppURI() {
		return jid;
	}

	public String getName() {
		return name;
	}

	public String getSubscription() {
		return subscription;
	}

}
