package com.calclab.emite.client.core.services;

import com.calclab.emite.client.components.Component;
import com.calclab.emite.client.core.packet.IPacket;

public interface Services extends Component {
    long getCurrentTime();

    void schedule(int msecs, ScheduledAction action);

    void send(String httpBase, String request, ConnectorCallback callback) throws ConnectorException;

    String toString(IPacket iPacket);

    IPacket toXML(String xml);

}
