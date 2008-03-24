package com.calclab.emite.client.bosh;

import com.calclab.emite.client.connector.Connector;
import com.calclab.emite.client.connector.ConnectorCallback;
import com.calclab.emite.client.connector.Delayed;
import com.calclab.emite.client.log.Logger;
import com.calclab.emite.client.packet.Packet;

@Deprecated
public class OldBosh {
	protected int inactivity;
	private int concurrent;
	private final Connector connector;
	private final ConnectorCallback defaultCallback;
	private boolean isRunning;
	private final BoshListenerCollection listeners;
	private final Logger logger;
	private final BoshOptions options;

	// TODO: problemas con long y GWT
	private long rid;
	private boolean shouldRestart;
	private String sid;

	OldBosh(final Connector connector, final BoshOptions options,
			final Logger logger) {
		this.connector = connector;
		this.options = options;
		this.listeners = new BoshListenerCollection();
		this.logger = logger;
		// TODO: mejorar
		this.rid = (long) (Math.random() * 1573741820);
		this.sid = null;
		this.concurrent = 0;
		shouldRestart = false;

		this.setRunning(false);

		this.defaultCallback = new ConnectorCallback() {
			public void onError(final Throwable error) {
				setRunning(false);
				concurrent--;
				listeners.onError(error);
			}

			public void onResponseReceived(final int statusCode,
					final String content) {
				concurrent--;
				// TODO: check if its a valid response
				listeners.onResponse(content);
				keepItBusy();
			}
		};
	}

	public void addListener(final BoshListener listener) {
		listeners.add(listener);
	}

	public void pause() {

	}

	public void removeListener(final BoshListener listener) {
		listeners.remove(listener);
	}

	public void resume() {

	}

	public void send(final Packet packet) {
		final String content = packet.toString();
		logger.info("SEND: \n{0}", content);
		send(content);
	}

	public void sendRequest(final String request,
			final ConnectorCallback callback) {
		try {
			concurrent++;
			final String httpBase = options.getHttpBase();
			connector.send(httpBase, request, callback);
		} catch (final Exception e) {
			listeners.onError(e);
		}
		listeners.onRequest(request);
	}

	public void setRestart(final boolean shouldRestart) {
		this.shouldRestart = shouldRestart;
	}

	public void start() {
		final String sessionRequest = XMLHelper.buildSessionCreationRequest(
				options, rid);
		sendRequest(sessionRequest, new ConnectorCallback() {

			public void onError(final Throwable error) {
				defaultCallback.onError(error);
			}

			public void onResponseReceived(final int statusCode,
					final String responseText) {
				setRunning(true);
				sid = XMLHelper.extractAttribute("sid", responseText);
				inactivity = XMLHelper.extractIntegerAttribute("inactivity",
						responseText);
				defaultCallback.onResponseReceived(statusCode, responseText);
			}
		});
	}

	public void stop() {
		final String terminate = XMLHelper.terminate(rid, sid);
		sendRequest(terminate, defaultCallback);
		setRunning(false);
	}

	private boolean isRunning() {
		return isRunning;
	}

	private void keepItBusy() {
		final Delayed delayed = new Delayed() {
			public void run() {
				logger.debug("HOT! running {0} - concurrent  {1}", isRunning,
						concurrent);
				if (isRunning() && concurrent == 0) {
					sendEmpty();
				}
			}
		};
		connector.schedule(delayed, 1000);
	}

	private void send(final String stanza) {
		rid++;
		final String request;

		if (shouldRestart) {
			logger.debug("Sending RESTART");
			shouldRestart = false;
			request = XMLHelper.restart(stanza, rid, sid);
		} else {
			request = XMLHelper.wrap(stanza, rid, sid);
		}
		sendRequest(request, defaultCallback);
	}

	private void sendEmpty() {
		rid++;
		final String request = XMLHelper.empty(rid, sid, inactivity);
		sendRequest(request, defaultCallback);
	}

	private void setRunning(final boolean isRunning) {
		this.isRunning = isRunning;
		logger.debug("BOSH RUNNING {0}", isRunning);
	}

}
