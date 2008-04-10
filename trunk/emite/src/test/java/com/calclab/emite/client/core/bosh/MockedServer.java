package com.calclab.emite.client.core.bosh;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.core.dispatcher.DispatcherMonitor;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.services.ConnectorCallback;
import com.calclab.emite.client.core.services.ConnectorException;
import com.calclab.emite.client.core.services.ScheduledAction;
import com.calclab.emite.client.core.services.Services;
import com.calclab.emite.j2se.services.TigaseXMLService;

public class MockedServer implements Services {

    private int requestCount;
    private String nextResponse;
    private ConnectorCallback lastCallback;
    private final TigaseXMLService xmler;

    public MockedServer() {
	requestCount = 0;
	xmler = new TigaseXMLService();
    }

    public void answer(final String nextResponse) {
	this.nextResponse = nextResponse;
	lastCallback.onResponseReceived(200, nextResponse);
    }

    public long getCurrentTime() {
	return System.currentTimeMillis();
    }

    public DispatcherMonitor getDispatcherMonitor() {
	return new DispatcherMonitor() {
	    public void publishing(final IPacket packet) {
		System.out.println("DISPATCHING: " + packet);
	    }
	};
    }

    public int getRequestCount() {
	return requestCount;
    }

    public synchronized void schedule(final int msecs, final ScheduledAction action) {
	try {
	    wait(msecs);
	    action.run();
	} catch (final InterruptedException e) {
	}
    }

    public void send(final String httpBase, final String request, final ConnectorCallback callback)
	    throws ConnectorException {
	this.lastCallback = callback;
	Log.debug("SENT: " + request);
	requestCount++;
    }

    public String toString(final IPacket packet) {
	return packet.toString();
    }

    public IPacket toXML(final String xml) {
	return xmler.toXML(xml);
    }
}
