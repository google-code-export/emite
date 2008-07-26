package com.calclab.emite.client.core.bosh3;

import java.util.List;

import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.services.ConnectorCallback;
import com.calclab.emite.client.services.ConnectorException;
import com.calclab.emite.client.services.Services;
import com.calclab.suco.client.signal.Signal;
import com.calclab.suco.client.signal.Signal0;
import com.calclab.suco.client.signal.Slot;

public class Bosh3Connection implements Connection {
    private long rid;
    private int activeConnections;
    private Packet body;
    private final Services services;
    private final ConnectorCallback callback;
    private boolean running;
    private StreamSettings stream;
    private final Signal<String> onError;
    private final Signal<String> onDisconnected;
    private final Signal0 onConnected;
    private final Signal<IPacket> onStanzasReceived;
    private String httpBase;
    private boolean shouldCollectResponses;
    private Bosh3Settings userSettings;

    public Bosh3Connection(final Services services) {
	this.services = services;
	this.onError = new Signal<String>("bosh:onError");
	this.onDisconnected = new Signal<String>("bosh:onDisconnected");
	this.onConnected = new Signal0("bosh:onConnected");
	this.onStanzasReceived = new Signal<IPacket>("bosh:onStanzasReceived");

	this.callback = new ConnectorCallback() {

	    public void onError(final Throwable throwable) {
		running = false;
		onError.fire("Exception thrown: " + throwable.toString());
	    }

	    public void onResponseReceived(final int statusCode, final String content) {
		activeConnections--;
		if (running) {
		    if (statusCode != 200) {
			running = false;
			onError.fire("Bad status: " + statusCode);
		    } else {
			final IPacket response = services.toXML(content);
			if (response != null && "body".equals(response.getName())) {
			    handleResponse(response);
			} else {
			    onError.fire("Bad response: " + content);
			}
		    }
		}

	    }
	};
    }

    public void connect() {
	assert userSettings != null;

	if (!running) {
	    this.running = true;
	    this.httpBase = userSettings.httpBase;
	    this.rid = (long) (Math.random() * 10000000) + 1000;
	    this.stream = null;
	    this.activeConnections = 0;
	    createInitialBody(userSettings);
	    sendBody();
	}
    }

    public void disconnect() {
	createBody();
	body.setAttribute("type", "terminate");
	sendBody();
	running = false;
	onDisconnected.fire("logged out");
    }

    public boolean isConnected() {
	return stream != null;
    }

    public void onError(final Slot<String> slot) {
	onError.add(slot);
    }

    public void onStanzaReceived(final Slot<IPacket> slot) {
	onStanzasReceived.add(slot);
    }

    public void restartStream() {
	createBody();
	body.setAttribute("xmpp:restart", "true");
    }

    public void send(final IPacket packet) {
	createBody();
	body.addChild(packet);
	sendBody();
    }

    public void setSettings(final Bosh3Settings settings) {
	this.userSettings = settings;
    }

    private void continueConnection(final String ack) {
	if (isConnected() && activeConnections == 0) {
	    createBody();
	    sendBody();
	}
    }

    private void createBody() {
	if (body == null) {
	    this.body = new Packet("body");
	    body.With("xmlns", "http://jabber.org/protocol/httpbind");
	    body.With("rid", getNextRid());
	    if (stream != null) {
		body.With("sid", stream.sid);
	    }
	}
    }

    private void createInitialBody(final Bosh3Settings userSettings) {
	this.body = new Packet("body");
	body.setAttribute("content", "text/xml; charset=utf-8");
	body.setAttribute("xmlns", "http://jabber.org/protocol/httpbind");
	body.setAttribute("xmlns:xmpp", "urn:xmpp:xbosh");
	body.setAttribute("xmpp:version", userSettings.version);
	body.setAttribute("xml:lang", "en");
	body.setAttribute("ack", "1");
	body.setAttribute("secure", "true");
	body.setAttribute("rid", getNextRid());
	body.setAttribute("to", userSettings.hostName);
	body.With("hold", userSettings.hold);
	body.With("wait", userSettings.wait);
    }

    private String getNextRid() {
	rid++;
	return "" + rid;
    }

    private void handleResponse(final IPacket response) {
	final String receivedSID = response.getAttribute("sid");
	final String type = response.getAttribute("type");
	final String ack = response.getAttribute("ack");

	if (isTerminate(type)) {
	    onDisconnected.fire("disconnected by server");
	} else {
	    if (stream == null) {
		stream = new StreamSettings(receivedSID, response.getAttribute("wait"), response
			.getAttribute("inactivity"), response.getAttribute("maxpause"));
		onConnected.fire();
	    }
	    shouldCollectResponses = true;
	    final List<? extends IPacket> stanzas = response.getChildren();
	    for (final IPacket stanza : stanzas) {
		onStanzasReceived.fire(stanza);
	    }
	    shouldCollectResponses = false;
	    continueConnection(ack);
	}
    }

    private boolean isTerminate(final String type) {
	// Openfire bug: terminal instead of terminate
	return "terminate".equals(type) || "terminal".equals(type);
    }

    private void sendBody() {
	if (!shouldCollectResponses) {
	    try {
		activeConnections++;
		final String request = services.toString(body);
		body = null;
		services.send(httpBase, request, callback);
	    } catch (final ConnectorException e) {
		activeConnections--;
		e.printStackTrace();
	    }
	}
    }
}
