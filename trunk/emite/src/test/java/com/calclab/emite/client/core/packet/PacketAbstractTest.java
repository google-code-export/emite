package com.calclab.emite.client.core.packet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.List;

import org.junit.Test;

public abstract class PacketAbstractTest {

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

    @Test
    public void shouldSetText() {
	final IPacket packet = createPacket("packet");
	packet.setText("text1");
	assertEquals("text1", packet.getText());
	packet.setText("text2");
	assertEquals("text2", packet.getText());
    }

    protected abstract IPacket createPacket(final String name);
}
