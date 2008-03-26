package com.calclab.emite.client.connector;

public interface HttpConnectorListener {
	void onError(String id, String cause);

	void onFinish(String id, long duration);

	void onResponse(String id, String response);

	void onSend(String id, String xml);

	void onStart(String id);

}
