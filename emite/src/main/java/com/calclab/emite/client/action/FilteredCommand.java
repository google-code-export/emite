package com.calclab.emite.client.action;

import com.calclab.emite.client.matcher.Matcher;
import com.calclab.emite.client.packet.Packet;

public class FilteredCommand implements Action {
	private final Action command;
	private final Matcher matcher;

	public FilteredCommand(final Matcher matcher, final Action command) {
		this.matcher = matcher;
		this.command = command;
	}

	public void handle(final Packet stanza) {
		if (matcher.matches(stanza)) {
			command.handle(stanza);
		}
	}

}
