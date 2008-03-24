package com.calclab.emite.client.plugin.dsl;

import com.calclab.emite.client.dispatcher.Action;
import com.calclab.emite.client.packet.Packet;

public class LogicAction implements Action {

	private final PacketProducer logic;

	public LogicAction(final PacketProducer logic) {
		this.logic = logic;
	}

	public void handle(final Packet stanza) {
		logic.logic(stanza);
	}

}
