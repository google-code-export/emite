package com.calclab.emite.client.connector;

public interface Connector {
    void send(String httpBase, String request, ConnectorCallback callback) throws ConnectorException;
}
