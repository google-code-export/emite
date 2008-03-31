package com.calclab.emite.client.core.services;

import com.calclab.emite.client.core.packet.Packet;


public interface XMLService {
    String toString(Packet packet);

    Packet toXML(String xml);
}
