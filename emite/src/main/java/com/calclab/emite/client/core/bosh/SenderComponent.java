package com.calclab.emite.client.core.bosh;

import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.PublisherComponent;
import com.calclab.emite.client.core.dispatcher.matcher.PacketMatcher;
import com.calclab.emite.client.core.packet.Packet;

public abstract class SenderComponent extends PublisherComponent {
	private final Bosh bosh;

	public SenderComponent(final Dispatcher dispatcher, final Bosh bosh) {
		super(dispatcher);
		this.bosh = bosh;
	}

	@Override
	public SenderActionsBuilder when(final Packet packet) {
		return new SenderActionsBuilder(new PacketMatcher(packet), dispatcher, bosh);
	}
}
