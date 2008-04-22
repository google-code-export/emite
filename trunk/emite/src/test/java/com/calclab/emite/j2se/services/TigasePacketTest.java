package com.calclab.emite.j2se.services;

import tigase.xml.Element;

import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.PacketAbstractTest;

public class TigasePacketTest extends PacketAbstractTest {
    @Override
    protected IPacket createPacket(final String name) {
	return new TigasePacket(new Element(name));
    }
}
