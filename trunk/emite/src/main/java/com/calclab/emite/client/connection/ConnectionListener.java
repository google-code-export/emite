package com.calclab.emite.client.connection;

public interface ConnectionListener {
	void onConnected();

	void onConnecting();

	void onDisconnected();
}
