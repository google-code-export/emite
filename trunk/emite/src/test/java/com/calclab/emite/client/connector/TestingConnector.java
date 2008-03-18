package com.calclab.emite.client.connector;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;

import com.calclab.emite.client.log.Logger;

public class TestingConnector implements Connector {

	private final HttpClient client;
	private final Logger logger;

	public TestingConnector(final Logger logger) {
		this.logger = logger;
		final HttpClientParams params = new HttpClientParams();
		params.setConnectionManagerTimeout(2000);
		client = new HttpClient(params);
		logger.debug("HttpClientConnector created!");
	}

	public void schedule(final Delayed delayed, final int msecs) {
		final Runnable r = new Runnable() {
			public synchronized void run() {
				try {
					wait(msecs);
					delayed.run();
				} catch (InterruptedException e) {
					logger.debug("Schedule exception {0}", e);
				}
			}
		};
		new Thread(r).start();
	}

	public synchronized void send(final String httpBase, final String xml, final ConnectorCallback callback)
			throws ConnectorException {
		final Runnable process = new Runnable() {
			public void run() {
				PostMethod post = new PostMethod(httpBase);

				try {
					post.setRequestEntity(new StringRequestEntity(xml, "text/xml", "utf-8"));
					logger.debug("HttpClientConnector:sending");
					int status = client.executeMethod(post);
					if (status == HttpStatus.SC_OK) {
						String response = post.getResponseBodyAsString();
						logger.debug("HttpClientConnector:RESPONSE!");
						callback.onResponseReceived(post.getStatusCode(), response);
					} else {
						logger.debug("Bad HttpStatus: {0}", status);
						callback.onError(new Exception("bad http status " + status));
					}
				} catch (Exception e) {
					logger.debug("Exception! {0}", e);
					callback.onError(e);
				} finally {
					post.releaseConnection();
					logger.debug("HttpClientConnector:End of request");
				}
			}
		};
		new Thread(process).start();

	}
}
