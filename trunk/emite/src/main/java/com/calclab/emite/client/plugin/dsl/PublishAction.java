package com.calclab.emite.client.plugin.dsl;

import com.calclab.emite.client.core.dispatcher.Action;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.packet.Packet;

public class PublishAction implements Action {
	private final Dispatcher dispatcher;
	private final Answer logic;
	private final Packet packet;

	public PublishAction(final Dispatcher dispatcher, final Answer logic) {
		this(dispatcher, logic, null);
	}

	public PublishAction(final Dispatcher dispatcher, final Packet packet) {
		this(dispatcher, null, packet);
	}

	PublishAction(final Dispatcher dispatcher, final Answer logic, final Packet packet) {
		this.dispatcher = dispatcher;
		this.logic = logic;
		this.packet = packet;
	}

	public void handle(final Packet stanza) {
		final Packet toBePublished = packet != null ? packet : logic.respondTo(stanza);
		dispatcher.publish(toBePublished);
	}
}
