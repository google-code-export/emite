package com.calclab.emite.client.components;

import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.matcher.PacketMatcher;
import com.calclab.emite.client.packet.Packet;

public abstract class AbstractComponent implements Component {

	protected Dispatcher dispatcher;

	public abstract void attach();

	public void install(final Dispatcher dispatcher) {
		this.dispatcher = dispatcher;
		attach();
		this.dispatcher = null;
	}

	public BasicActionsBuilder when(final Packet packet) {
		return new BasicActionsBuilder(new PacketMatcher(packet), dispatcher);
	}

	public BasicActionsBuilder when(final String packetName) {
		return new BasicActionsBuilder(new PacketMatcher(packetName), dispatcher);
	}
}
