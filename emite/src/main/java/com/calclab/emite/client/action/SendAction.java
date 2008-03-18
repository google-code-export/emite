package com.calclab.emite.client.action;

import com.calclab.emite.client.bosh.IConnection;
import com.calclab.emite.client.packet.Packet;

public class SendAction implements Action {
	private final IConnection connection;
	private final BussinessLogic logic;
	private final Packet packet;

	public SendAction(final IConnection connection, final BussinessLogic logic) {
		this(connection, logic, null);
	}

	public SendAction(final IConnection connection, final Packet packet) {
		this(connection, null, packet);
	}

	SendAction(final IConnection connection, final BussinessLogic logic, final Packet packet) {
		this.connection = connection;
		this.logic = logic;
		this.packet = packet;
	}

	public void handle(final Packet stanza) {
		final Packet toBeSend = packet != null ? packet : logic.logic(stanza);
		connection.send(toBeSend);
	}

}
