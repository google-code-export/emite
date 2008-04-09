package com.calclab.emite.client;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.core.bosh.BoshOptions;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.DispatcherPlugin;
import com.calclab.emite.j2se.connector.HttpConnector;
import com.calclab.emite.j2se.connector.HttpConnectorListener;
import com.calclab.emite.j2se.packet.TigaseXMLService;
import com.calclab.emite.j2se.scheduler.ThreadScheduler;

public class TestHelper {

    public static Xmpp createXMPP() {
	return createXMPP(new HttpConnectorListener() {
	    public void onError(final String id, final String cause) {
	    }

	    public void onFinish(final String id, final long duration) {
	    }

	    public void onResponse(final String id, final String response) {
	    }

	    public void onSend(final String id, final String xml) {
	    }

	    public void onStart(final String id) {
	    }

	});
    }

    public static Xmpp createXMPP(final BoshOptions options, final HttpConnectorListener listener) {

	final TigaseXMLService xmlService = new TigaseXMLService();
	final HttpConnector connector = new HttpConnector(listener);
	final ThreadScheduler scheduler = new ThreadScheduler();
	return Xmpp.create(connector, xmlService, scheduler, options);
    }

    public static Xmpp createXMPP(final HttpConnectorListener httpConnectorListener) {

	final Xmpp xmpp = createXMPP(new BoshOptions("http://localhost:8383/http-bind/", "localhost"),
		httpConnectorListener);
	final Container container = xmpp.getComponents();
	final Dispatcher dispatcher = DispatcherPlugin.getDispatcher(container);
	return xmpp;
    }

}
