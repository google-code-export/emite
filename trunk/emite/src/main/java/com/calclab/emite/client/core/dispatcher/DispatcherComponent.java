package com.calclab.emite.client.core.dispatcher;

import com.calclab.emite.client.components.Startable;
import com.calclab.emite.client.core.dispatcher.matcher.PacketMatcher;
import com.calclab.emite.client.core.packet.Packet;

public abstract class DispatcherComponent implements Startable {

	protected Dispatcher dispatcher;

	public DispatcherComponent(final Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public abstract void attach();

	public void start() {
		attach();
	}

	public void stop() {
	}

	public void when(final Packet packet, final PacketListener packetListener) {
		when(new PacketMatcher(packet), packetListener);
	}

	public void when(final String packetName, final PacketListener packetListener) {
		when(new PacketMatcher(packetName), packetListener);
	}

	private void when(final PacketMatcher matcher, final PacketListener packetListener) {
		dispatcher.subscribe(matcher, packetListener);
	}
}
