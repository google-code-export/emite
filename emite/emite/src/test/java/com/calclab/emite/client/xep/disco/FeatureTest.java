package com.calclab.emite.client.xep.disco;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.Packet;

public class FeatureTest {

    @Test
    public void shouldParsePacket() {
	final IPacket packet = new Packet("feature").With("var", "protocol");
	final Feature feature = Feature.fromPacket(packet);
	assertEquals("protocol", feature.var);
    }
}
