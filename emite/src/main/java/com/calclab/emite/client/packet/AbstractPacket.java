package com.calclab.emite.client.packet;

public abstract class AbstractPacket implements Packet {

	public Packet With(final String name, final String value) {
		setAttribute(name, value);
		return this;
	}
}
