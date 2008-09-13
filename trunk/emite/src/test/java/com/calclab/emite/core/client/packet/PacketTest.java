package com.calclab.emite.core.client.packet;

import org.junit.Test;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.Packet;
import com.calclab.emite.core.client.packet.PacketTestSuite;

public class PacketTest {

    @Test
    public void testPacket() {
	PacketTestSuite.runPacketTests(new AbstractHelper() {
	    public IPacket createPacket(final String name) {
		return new Packet(name);
	    }
	});
    }

}
