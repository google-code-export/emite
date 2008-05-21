package com.calclab.emite.j2se.services;

import org.junit.Test;

import com.calclab.emite.client.core.packet.AbstractHelper;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.PacketTestSuite;

public class TigasePacketTest {

    @Test
    public void testPacket() {
	PacketTestSuite.runPacketTests(new AbstractHelper() {
	    public IPacket createPacket(final String name) {
		return new TigasePacket(name);
	    }
	});
    }

}
