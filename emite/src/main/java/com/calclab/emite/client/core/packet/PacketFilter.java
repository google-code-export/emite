package com.calclab.emite.client.core.packet;

public interface PacketFilter {
    boolean isValid(IPacket packet);
}
