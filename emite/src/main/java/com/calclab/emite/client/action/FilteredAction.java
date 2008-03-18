package com.calclab.emite.client.action;

import com.calclab.emite.client.matcher.Matcher;
import com.calclab.emite.client.packet.Packet;

public class FilteredAction implements Action {
	private final Action command;
	private final Matcher matcher;

	public FilteredAction(final Matcher matcher, final Action command) {
		this.matcher = matcher;
		this.command = command;
	}

	public void handle(final Packet stanza) {
		// System.out.println("filter: " + matcher.toString() + " : " + stanza);
		if (matcher.matches(stanza)) {
			// System.out.println("!!!");
			command.handle(stanza);
		}
	}

}
