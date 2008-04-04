package com.calclab.emite.client.core.services.gwt;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.core.services.Connector;
import com.calclab.emite.client.core.services.ConnectorCallback;
import com.calclab.emite.client.core.services.ConnectorException;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

public class GWTConnector implements Connector {
    public void send(final String httpBase, final String request, final ConnectorCallback callback)
            throws ConnectorException {
        Log.debug("GWT CONNECTOR SEND: " + request);
        final RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, httpBase);
        try {
            builder.sendRequest(request, new RequestCallback() {
                public void onError(final Request arg0, final Throwable throwable) {
                    Log.debug("GWT CONNECTOR ERROR: " + throwable);
                    callback.onError(throwable);
                }

                public void onResponseReceived(final Request req, final Response res) {
                    Log.debug("GWT CONNECTOR RECEIVED: " + res.getText());
                    callback.onResponseReceived(res.getStatusCode(), res.getText());
                }
            });
        } catch (final RequestException e) {
            throw new ConnectorException(e.getMessage());
        }
    }

}
