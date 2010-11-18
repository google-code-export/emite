/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.core.client.services.gwt;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.core.client.services.ConnectorCallback;
import com.calclab.emite.core.client.services.ConnectorException;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

public class GWTConnector {
    public static void send(final String httpBase, final String request, final ConnectorCallback listener)
	    throws ConnectorException {
	Log.debug("GWT CONNECTOR SEND: " + request);
	final RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, httpBase);
	try {
	    builder.sendRequest(request, new RequestCallback() {
		public void onError(final Request arg0, final Throwable throwable) {
		    Log.debug("GWT CONNECTOR ERROR: " + throwable);
		    listener.onError(request, throwable);
		}

		public void onResponseReceived(final Request req, final Response res) {
		    Log.debug("GWT CONNECTOR RECEIVED: " + res.getText());
		    listener.onResponseReceived(res.getStatusCode(), res.getText());
		}
	    });
	} catch (final RequestException e) {
	    throw new ConnectorException(e.getMessage());
	} catch (final Exception e) {
	    Log.error("Some GWT connector exception: " + e);
	    throw new ConnectorException(e.getMessage());
	}
    }

}
