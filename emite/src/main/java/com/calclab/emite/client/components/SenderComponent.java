package com.calclab.emite.client.components;

import com.calclab.emite.client.core.bosh.Connection;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.matcher.PacketMatcher;
import com.calclab.emite.client.packet.Packet;

public abstract class SenderComponent extends AbstractComponent {
	private final Connection connection;

	public SenderComponent(final Dispatcher dispatcher, final Connection connection) {
		super(dispatcher);
		this.connection = connection;
	}

	@Override
	public SenderActionsBuilder when(final Packet packet) {
		return new SenderActionsBuilder(new PacketMatcher(packet), dispatcher, connection);
	}
}
