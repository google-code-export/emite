package com.calclab.emite.client.core.services;

public interface ConnectorCallback {
	void onError(Throwable throwable);

	void onResponseReceived(int statusCode, String content);
}
