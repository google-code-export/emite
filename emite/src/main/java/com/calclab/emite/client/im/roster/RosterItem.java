package com.calclab.emite.client.im.roster;

public class RosterItem {
	private final String jid;
	private final String name;
	private final String subscription;

	public RosterItem(final String jid, final String subscription, final String name) {
		this.jid = jid;
		this.subscription = subscription;
		this.name = name;
	}

	public String getJid() {
		return jid;
	}

	public String getName() {
		return name;
	}

	public String getSubscription() {
		return subscription;
	}

}
