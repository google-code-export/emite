package com.calclab.emite.client.action;

import com.calclab.emite.client.packet.Packet;

public class LogicAction implements Action {

	private final BussinessLogic logic;

	public LogicAction(final BussinessLogic logic) {
		this.logic = logic;
	}

	public void handle(final Packet stanza) {
		logic.logic(stanza);
	}

}
