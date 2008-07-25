/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.j2se.services;

import java.io.IOException;
import java.text.MessageFormat;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.services.ConnectorCallback;
import com.calclab.emite.client.services.ConnectorException;

public class HttpConnector {

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

	final Runnable process = new Runnable() {
	    public void run() {
		int status = 0;
		String response = null;
		final PostMethod post = new PostMethod(httpBase);

		try {
		    post.setRequestEntity(new StringRequestEntity(xml, "text/xml", "utf-8"));
		    listener.onSend(id, xml);
		    status = client.executeMethod(post);
		    response = post.getResponseBodyAsString();
		} catch (final Exception e) {
		    listener.onError(id, "exception " + e);
		    callback.onError(e);
		    e.printStackTrace();
		} finally {
		    post.releaseConnection();
		}

		if (status == HttpStatus.SC_OK) {
		    listener.onFinish(id, System.currentTimeMillis() - timeBegin);
		    listener.onResponse(id, response);
		    callback.onResponseReceived(post.getStatusCode(), response);
		} else {
		    listener.onError(id, "bad status");
		    callback.onError(new Exception("bad http status " + status));
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
