package com.calclab.emite.client.connector;

public interface Connector {
	void schedule(Delayed delayed, int msecs);

	void send(String httpBase, String request, ConnectorCallback callback) throws ConnectorException;
}
