package com.calclab.emite.client.plugin.dsl;

import com.calclab.emite.client.bosh.Connection;
import com.calclab.emite.client.dispatcher.Action;
import com.calclab.emite.client.packet.Packet;

public class SendAction implements Action {
	private final Connection connection;
	private final BussinessLogic logic;
	private final Packet packet;

	public SendAction(final Connection connection, final BussinessLogic logic) {
		this(connection, logic, null);
	}

	public SendAction(final Connection connection, final Packet packet) {
		this(connection, null, packet);
	}

	SendAction(final Connection connection, final BussinessLogic logic, final Packet packet) {
		this.connection = connection;
		this.logic = logic;
		this.packet = packet;
	}

	public void handle(final Packet stanza) {
		final Packet toBeSend = packet != null ? packet : logic.logic(stanza);
		connection.send(toBeSend);
	}

}
