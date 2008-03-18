package com.calclab.emite.client.connector;

public interface ConnectorCallback {
	void onError(Throwable throwable);

	void onResponseReceived(int statusCode, String content);
}
