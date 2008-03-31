package com.calclab.emite.client.connector;

import java.io.IOException;
import java.text.MessageFormat;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;

import com.allen_sauer.gwt.log.client.Log;

public class HttpConnector implements Connector {

	private static class HttpConnectorID {
		private static int id = 0;

		public static String getNext() {
			id++;
			return String.valueOf(id);
		}

	}

	private final HttpConnectorListener listener;

	public HttpConnector(final HttpConnectorListener listener) {
		this.listener = listener;
	}

	public synchronized void send(final String httpBase, final String xml, final ConnectorCallback callback)
			throws ConnectorException {
		final long timeBegin = System.currentTimeMillis();
		final String id = HttpConnectorID.getNext();
		listener.onStart(id);
		final HttpClientParams params = new HttpClientParams();
		params.setConnectionManagerTimeout(10000);
		final HttpClient client = new HttpClient(params);
		debug("HttpClientConnector created!: " + xml);

		final Runnable process = new Runnable() {
			public void run() {
				final PostMethod post = new PostMethod(httpBase);

				try {
					post.setRequestEntity(new StringRequestEntity(xml, "text/xml", "utf-8"));
					listener.onSend(id, xml);
					final int status = client.executeMethod(post);
					if (status == HttpStatus.SC_OK) {
						final String response = post.getResponseBodyAsString();
						listener.onResponse(id, response);
						callback.onResponseReceived(post.getStatusCode(), response);
					} else {
						listener.onError(id, "bad status");
						callback.onError(new Exception("bad http status " + status));
					}
				} catch (final IOException e) {
					listener.onError(id, "exception " + e);
					callback.onError(e);
					e.printStackTrace();
				} finally {
					post.releaseConnection();
					listener.onFinish(id, System.currentTimeMillis() - timeBegin);
				}
			}
		};
		new Thread(process).start();

	}

	protected void debug(final String pattern, final Object... arguments) {
		final String msg = MessageFormat.format(pattern, arguments);
		Log.debug(msg);
	}

}
