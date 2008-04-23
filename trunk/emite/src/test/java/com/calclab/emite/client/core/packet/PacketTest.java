package com.calclab.emite.client.core.packet;

public class PacketTest extends IPacketAbstractTest {

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
