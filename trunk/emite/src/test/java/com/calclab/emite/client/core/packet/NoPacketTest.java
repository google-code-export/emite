package com.calclab.emite.client.core.packet;

import static org.junit.Assert.*;

import org.junit.Test;

public class NoPacketTest {

    @Test
    public void testNoPacket() {
	final IPacket noPacket = NoPacket.INSTANCE;
	assertSame(noPacket, noPacket.addChild("node", "xmlns"));
	assertNull(noPacket.getText());
	assertSame(noPacket, noPacket.getFirstChild("anyChildren"));
	assertEquals(0, noPacket.getChildren().size());
	assertEquals(0, noPacket.getChildren("anyChildren").size());
	assertFalse(noPacket.removeChild(new Packet("some")));
    }
}
