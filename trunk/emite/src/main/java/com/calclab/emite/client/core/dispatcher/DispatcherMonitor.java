package com.calclab.emite.client.core.dispatcher;

import com.calclab.emite.client.core.packet.IPacket;

public interface DispatcherMonitor {
    void publishing(IPacket packet);
}
