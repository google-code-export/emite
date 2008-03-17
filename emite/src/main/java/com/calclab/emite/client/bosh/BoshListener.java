package com.calclab.emite.client.bosh;

public interface BoshListener {

	void onError(Throwable error);

	void onRequest(String request);

	void onResponse(String response);
}
