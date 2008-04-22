package com.calclab.emite.client.core.packet;

public class PacketTest extends PacketAbstractTest {

    /**
     * Template method
     * 
     * @return
     */
    @Override
    protected IPacket createPacket(final String name) {
	return new Packet(name);
    }
}
