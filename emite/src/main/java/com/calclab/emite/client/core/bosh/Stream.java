package com.calclab.emite.client.core.bosh;

import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.Packet;

public interface Stream {
    void addResponse(IPacket response);

    Packet clearBody();

    boolean isEmpty();

    void newRequest(String sid);

    void setRestart(String domain);

    void setSID(String sid);

    void setTerminate();

    void start(String domain);
}
