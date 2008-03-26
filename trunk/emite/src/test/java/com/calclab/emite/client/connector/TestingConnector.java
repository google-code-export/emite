package com.calclab.emite.client.connector;

import java.io.IOException;
import java.text.MessageFormat;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;

import com.allen_sauer.gwt.log.client.Log;

public class TestingConnector implements Connector {

	private final HttpClient client;

	public TestingConnector() {
		final HttpClientParams params = new HttpClientParams();
		params.setConnectionManagerTimeout(2000);
		client = new HttpClient(params);
		debug("HttpClientConnector created!");
	}

	public synchronized void send(final String httpBase, final String xml,
			final ConnectorCallback callback) throws ConnectorException {
		final Runnable process = new Runnable() {
			public void run() {
				final PostMethod post = new PostMethod(httpBase);

				try {
					post.setRequestEntity(new StringRequestEntity(xml,
							"text/xml", "utf-8"));
					debug("HttpClientConnector {0} SEND\n{1}", this.hashCode(),
							xml);
					final int status = client.executeMethod(post);
					if (status == HttpStatus.SC_OK) {
						final String response = post.getResponseBodyAsString();
						debug("HttpClientConnector {0} RESPONSE!", this
								.hashCode());
						callback.onResponseReceived(post.getStatusCode(),
								response);
					} else {
						debug("HttpClientConnector {0} Bad HttpStatus: {1}",
								this.hashCode(), status);
						callback.onError(new Exception("bad http status "
								+ status));
					}
				} catch (final IOException e) {
					Log.debug("Exception! {0}", e);
					callback.onError(e);
					e.printStackTrace();
				} finally {
					post.releaseConnection();
					debug("HttpClientConnector {0} FINISH", this.hashCode());
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
