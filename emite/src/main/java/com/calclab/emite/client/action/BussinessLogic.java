package com.calclab.emite.client.action;

import com.calclab.emite.client.packet.Packet;

public interface BussinessLogic {
	public abstract Packet logic(final Packet received);
}
