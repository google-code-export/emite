package com.calclab.emite.client.plugin.dsl;

import com.calclab.emite.client.packet.Packet;

public interface PacketProducer {
	public abstract Packet logic(final Packet received);
}
