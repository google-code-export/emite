package com.calclab.emite.client.bosh;

import java.util.ArrayList;

class BoshListenerCollection extends ArrayList<BoshListener> implements BoshListener {
	private static final long serialVersionUID = 1L;

	public void onError(final Throwable error) {
		for (final BoshListener listener : this) {
			listener.onError(error);
		}
	}

	public void onRequest(final String request) {
		for (final BoshListener listener : this) {
			listener.onRequest(request);
		}
	}

	public void onResponse(final String response) {
		for (final BoshListener listener : this) {
			listener.onResponse(response);
		}
	}

}
