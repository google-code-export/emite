package com.calclab.emite.client.plugin.dsl;

import com.calclab.emite.client.core.dispatcher.Action;
import com.calclab.emite.client.packet.Packet;

public class LogicAction implements Action {

	private final Answer logic;

	public LogicAction(final Answer logic) {
		this.logic = logic;
	}

	public void handle(final Packet stanza) {
		logic.respondTo(stanza);
	}

}
