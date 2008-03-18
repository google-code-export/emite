package com.calclab.emite.client.action;

import com.calclab.emite.client.IDispatcher;
import com.calclab.emite.client.packet.Packet;

public class PublishAction implements Action {
	private final IDispatcher dispatcher;
	private final BussinessLogic logic;
	private final Packet packet;

	public PublishAction(final IDispatcher dispatcher, final BussinessLogic logic) {
		this(dispatcher, logic, null);
	}

	public PublishAction(final IDispatcher dispatcher, final Packet packet) {
		this(dispatcher, null, packet);
	}

	PublishAction(final IDispatcher dispatcher, final BussinessLogic logic, final Packet packet) {
		this.dispatcher = dispatcher;
		this.logic = logic;
		this.packet = packet;
	}

	public void handle(final Packet stanza) {
		final Packet toBePublished = packet != null ? packet : logic.logic(stanza);
		dispatcher.publish(toBePublished);
	}
}
