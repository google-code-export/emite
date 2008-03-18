package com.calclab.emite.client.connector;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Timer;

public class GWTConnector implements Connector {
	public void schedule(final Delayed delayed, final int msecs) {
		new Timer() {
			@Override
			public void run() {
				delayed.run();
			}
		}.schedule(msecs);
	}

	public void send(final String httpBase, final String request, final ConnectorCallback callback)
			throws ConnectorException {
		final RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, httpBase);
		try {
			builder.sendRequest(request, new RequestCallback() {
				public void onError(final Request arg0, final Throwable throwable) {
					callback.onError(throwable);
				}

				public void onResponseReceived(final Request req, final Response res) {
					callback.onResponseReceived(res.getStatusCode(), res.getText());
				}
			});
		} catch (final RequestException e) {
			throw new ConnectorException(e.getMessage());
		}
	}

}
