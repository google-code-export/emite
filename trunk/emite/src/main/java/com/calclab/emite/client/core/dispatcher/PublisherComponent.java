package com.calclab.emite.client.core.dispatcher;

import com.calclab.emite.client.components.Startable;
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

	public PublisherActionsBuilder when(final Packet packet) {
		return new PublisherActionsBuilder(new PacketMatcher(packet), dispatcher);
	}

	public PublisherActionsBuilder when(final String packetName) {
		return new PublisherActionsBuilder(new PacketMatcher(packetName), dispatcher);
	}
}
