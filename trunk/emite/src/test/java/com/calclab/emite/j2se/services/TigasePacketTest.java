package com.calclab.emite.j2se.services;

import tigase.xml.Element;

import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.IPacketAbstractTest;

public class TigasePacketTest extends IPacketAbstractTest {
    @Override
    protected IPacket createPacket(final String name) {
	return new TigasePacket(new Element(name));
    }
}
