package com.calclab.emite.client.packet;

public abstract class AbstractPacket implements Packet {

    public Packet With(final String name, final long value) {
        return With(name, String.valueOf(value));
    }

    public Packet With(final String name, final String value) {
        setAttribute(name, value);
        return this;
    }
}
