package com.calclab.emite.client.core.bosh2;

import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.services.ConnectorCallback;
import com.calclab.emite.client.services.ConnectorException;
import com.calclab.emite.client.services.Services;
import com.calclab.suco.client.signal.Signal;
import com.calclab.suco.client.signal.Slot;

/**
 * Bosh
 * 
 * Responsabilities:
 * <ul>
 * <li>Sends packets</li>
 * <li>Know total's concurrent connections</li>
 * <li>Know if its running or not</li>
 * </ul>
 * 
 */
public class B2Bosh {
    private final Services services;
    private final Signal<B2BoshOptions> onStart;
    private String httpBase;
    private final ConnectorCallback callback;
    private final Signal<B2Error> onError;
    private int concurrentConnections;
    private boolean isRunning;
    private final Signal<IPacket> onBody;

    public B2Bosh(final Services services) {
	this.services = services;
	this.onStart = new Signal<B2BoshOptions>("bosh:onStart");
	this.onError = new Signal<B2Error>("bosh:error");
	this.onBody = new Signal<IPacket>("bosh:body");

	this.callback = new ConnectorCallback() {
	    public void onError(final Throwable throwable) {
		if (isRunning) {

		}
	    }

	    public void onResponseReceived(final int statusCode, final String content) {
		if (isRunning) {
		    handleResponse(statusCode, content);
		}
	    }
	};
    }

    public void onBody(final Slot<IPacket> slot) {
	onBody.add(slot);
    }

    public void onError(final Slot<B2Error> slot) {
	onError.add(slot);
    }

    public void onStart(final Slot<B2BoshOptions> slot) {
	onStart.add(slot);
    }

    /**
     * Send a packet (always a wrapping body) to the server
     */
    public void send(final Packet body) {
	final String request = services.toString(body);

	if (!isRunning)
	    throw new IllegalStateException("can't send packets if not running");

	try {
	    this.concurrentConnections++;
	    services.send(httpBase, request, callback);
	} catch (final ConnectorException e) {
	    onError.fire(new B2Error("connection exception", e));
	}
    }

    /**
     * Start a bosh connection
     */
    public void start(final B2BoshOptions options) {
	this.httpBase = options.httpBase;
	this.isRunning = true;
	onStart.fire(options);
    }

    public void stop() {
    }

    void handleResponse(final int statusCode, final String content) {
	concurrentConnections--;

	if (statusCode >= 400) {
	    onError.fire(new B2Error("bad response status", "status code: " + statusCode));
	} else {
	    final IPacket response = services.toXML(content);
	    if ("body".equals(response.getName())) {
		onBody.fire(response);
	    } else {
		onError.fire(new B2Error("not valid response from server", content));
	    }
	}
    }
}
