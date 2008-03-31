package com.calclab.emite.client.core.services;

public interface Connector {
    void send(String httpBase, String request, ConnectorCallback callback) throws ConnectorException;
}
