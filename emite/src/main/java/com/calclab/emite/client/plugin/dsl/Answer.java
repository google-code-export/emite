package com.calclab.emite.client.plugin.dsl;

import com.calclab.emite.client.packet.Packet;

public interface Answer {
	public abstract Packet respondTo(final Packet received);
}
