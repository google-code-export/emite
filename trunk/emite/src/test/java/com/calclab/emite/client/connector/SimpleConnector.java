package com.calclab.emite.client.connector;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;

import com.calclab.emite.client.log.Logger;

public class SimpleConnector implements Connector {

	private final HttpClient client;
	private final Logger logger;

	public SimpleConnector(final Logger logger) {
		this.logger = logger;
		final HttpClientParams params = new HttpClientParams();
		params.setConnectionManagerTimeout(2000);
		client = new HttpClient(params);
		logger.debug("HttpClientConnector created!");
	}

	public void schedule(final Delayed delayed, final int msecs) {

	}

	public void send(final String httpBase, final String xml, final ConnectorCallback callback)
			throws ConnectorException {
		final Runnable process = new Runnable() {
			public void run() {
				PostMethod post = new PostMethod(httpBase);

				try {
					post.setRequestEntity(new StringRequestEntity(xml, "text/xml", "utf-8"));
					logger.debug("HttpClientConnector:send {0} - {1}", post.toString(), xml);
					int status = client.executeMethod(post);
					if (status == HttpStatus.SC_OK) {
						String response = post.getResponseBodyAsString();
						logger.debug("HttpClientConnector:response {0}", response);
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
					logger.debug("End of request");
				}
			}
		};
		new Thread(process).start();

	}

}
