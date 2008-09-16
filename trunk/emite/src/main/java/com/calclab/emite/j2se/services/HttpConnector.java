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

import java.text.MessageFormat;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.core.client.services.ConnectorCallback;
import com.calclab.emite.core.client.services.ConnectorException;
import com.calclab.suco.client.log.Logger;

public class HttpConnector {

    private static class HttpConnectorID {
	private static int id = 0;

	public static String getNext() {
	    id++;
	    return String.valueOf(id);
	}

    }

    public HttpConnector() {
    }

    public synchronized void send(final String httpBase, final String xml, final ConnectorCallback callback)
	    throws ConnectorException {
	final String id = HttpConnectorID.getNext();
	Logger.debug("Connector [{0}] send: {1}", id, xml);
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
		    status = client.executeMethod(post);
		    response = post.getResponseBodyAsString();
		} catch (final Exception e) {
		    callback.onError(xml, e);
		    e.printStackTrace();
		} finally {
		    post.releaseConnection();
		}

		if (status == HttpStatus.SC_OK) {
		    Logger.debug("Connector [{0}] receive: {1}", id, response);
		    callback.onResponseReceived(post.getStatusCode(), response);
		} else {
		    Logger.debug("Connector [{0}] bad status: {1}", id, status);
		    callback.onError(xml, new Exception("bad http status " + status));
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
