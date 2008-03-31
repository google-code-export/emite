package com.calclab.emite.client.components;

import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.matcher.PacketMatcher;
import com.calclab.emite.client.core.packet.Packet;

public abstract class PublisherComponent implements Startable {

	protected Dispatcher dispatcher;

	public PublisherComponent(final Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public abstract void attach();

	public void start() {
		attach();
	}

	public void stop() {
	}

	public BasicActionsBuilder when(final Packet packet) {
		return new BasicActionsBuilder(new PacketMatcher(packet), dispatcher);
	}

	public BasicActionsBuilder when(final String packetName) {
		return new BasicActionsBuilder(new PacketMatcher(packetName), dispatcher);
	}
}
