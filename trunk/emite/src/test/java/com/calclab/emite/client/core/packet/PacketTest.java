package com.calclab.emite.client.core.packet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.List;

import org.junit.Test;

public class PacketTest {

    @Test
    public void getChildrenNeverReturnsNull() {
	final IPacket packet = createPacket("root");
	final List<? extends IPacket> children = packet.getChildren();
	assertNotNull(children);
	assertEquals(0, children.size());
    }

    @Test
    public void getFirstChildNeverReturnsNull() {
	final IPacket packet = createPacket("root");
	final IPacket child = packet.getFirstChild("child");
	assertNotNull(child);
	assertSame(NoPacket.INSTANCE, child);
    }

    protected IPacket createPacket(final String name) {
	return new Packet(name);
    }
}
