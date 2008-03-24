package com.calclab.emite.client.packet;


public interface XMLService {
    String toString(Packet packet);

    Packet toXML(String xml);
}
