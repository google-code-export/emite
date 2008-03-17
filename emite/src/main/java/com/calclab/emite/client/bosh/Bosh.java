package com.calclab.emite.client.bosh;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Timer;

public class Bosh {
	protected int inactivity;
	private int concurrent;
	private final RequestCallback defaultCallback;
	private boolean isRunning;
	private final BoshListener listener;
	private final BoshOptions options;
	// TODO: problemas con long y GWT
	private long rid;
	private String sid;

	public Bosh(final BoshOptions options, final BoshListener listener) {
		this.options = options;
		this.listener = listener;
		// TODO: mejorar
		this.rid = (long) (Math.random() * 1573741820);
		this.sid = null;
		this.concurrent = 0;
		this.isRunning = false;

		this.defaultCallback = new RequestCallback() {
			public void onError(final Request req, final Throwable error) {
				isRunning = false;
				concurrent--;
				listener.onError(error);
			}

			public void onResponseReceived(final Request req, final Response res) {
				concurrent--;
				listener.onResponse(res.getText());
				new Timer() {
					@Override
					public void run() {
						if (isRunning && concurrent == 0) {
							sendEmpty();
						}
					}
				}.schedule(1000);
			}
		};
	}

	public void pause() {

	}

	public void restart() {
		rid++;
		final String request = XMLHelper.restart(rid, sid, options.getDomain());
		sendRequest(request, defaultCallback);
	}

	public void resume() {

	}

	public void send(final String stanza) {
		rid++;
		final String request = XMLHelper.wrap(stanza, rid, sid);
		sendRequest(request, defaultCallback);
	}

	public void start() {
		final String sessionRequest = XMLHelper.buildSessionCreationRequest(options, rid);
		sendRequest(sessionRequest, new RequestCallback() {

			public void onError(final Request req, final Throwable error) {
				defaultCallback.onError(req, error);
			}

			public void onResponseReceived(final Request req, final Response res) {
				isRunning = true;
				final String responseText = res.getText();
				sid = XMLHelper.extractAttribute("sid", responseText);
				inactivity = XMLHelper.extractIntegerAttribute("inactivity", responseText);
				defaultCallback.onResponseReceived(req, res);
			}
		});
	}

	public void stop() {
		// TODO: send gracefully logout
		isRunning = false;
	}

	private void sendEmpty() {
		rid++;
		final String request = XMLHelper.empty(rid, sid, inactivity);
		sendRequest(request, defaultCallback);
	}

	private void sendRequest(final String request, final RequestCallback callback) {
		final RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, options.getHttpBase());
		try {
			concurrent++;
			builder.sendRequest(request, callback);
		} catch (final RequestException e) {
			listener.onError(e);
		}
		listener.onRequest(request);
	}

}
