package com.calclab.emite.client.core.bosh;

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
    private int activeConnections;
    private Packet body;
    private final Services services;
    private final ConnectorCallback callback;
    private boolean running;
    private StreamSettings stream;
    private final Signal<String> onError;
    private final Signal<String> onDisconnected;
    private final Signal0 onConnected;
    private final Signal<IPacket> onStanzaReceived;
    private final Signal<IPacket> onStanzaSent;
    private boolean shouldCollectResponses;
    private Bosh3Settings userSettings;
    private int errors;

    public Bosh3Connection(final Services services) {
	this.services = services;
	this.onError = new Signal<String>("bosh:onError");
	this.onDisconnected = new Signal<String>("bosh:onDisconnected");
	this.onConnected = new Signal0("bosh:onConnected");
	this.onStanzaReceived = new Signal<IPacket>("bosh:onReceived");
	this.onStanzaSent = new Signal<IPacket>("bosh:onSent");
	this.errors = 0;

	this.callback = new ConnectorCallback() {

	    public void onError(final String request, final Throwable throwable) {
		errors++;
		if (errors > 2) {
		    running = false;
		    onError.fire("Exception thrown: " + throwable.toString());
		} else {
		    send(request);
		}
	    }

	    public void onResponseReceived(final int statusCode, final String content) {
		errors--;
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
	    this.stream = new StreamSettings();
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
	onStanzaReceived.add(slot);
    }

    public void onStanzaSent(final Slot<IPacket> slot) {
	onStanzaSent.add(slot);
    }

    public StreamSettings pause() {
	createBody();
	body.setAttribute("pause", stream.maxPause);
	sendBody();
	return stream;
    }

    public void restartStream() {
	createBody();
	body.setAttribute("xmpp:restart", "true");
    }

    public boolean resume(final StreamSettings settings) {
	running = true;
	stream = settings;
	continueConnection(null);
	return running;
    }

    public void send(final IPacket packet) {
	createBody();
	body.addChild(packet);
	sendBody();
	onStanzaSent.fire(packet);
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
	    body.With("rid", stream.getNextRid());
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
	body.setAttribute("rid", stream.getNextRid());
	body.setAttribute("to", userSettings.hostName);
	body.With("hold", userSettings.hold);
	body.With("wait", userSettings.wait);
    }

    private void handleResponse(final IPacket response) {
	if (isTerminate(response.getAttribute("type"))) {
	    onDisconnected.fire("disconnected by server");
	} else {
	    if (stream.sid == null) {
		initStream(response);
		onConnected.fire();
	    }
	    shouldCollectResponses = true;
	    final List<? extends IPacket> stanzas = response.getChildren();
	    for (final IPacket stanza : stanzas) {
		onStanzaReceived.fire(stanza);
	    }
	    shouldCollectResponses = false;
	    continueConnection(response.getAttribute("ack"));
	}
    }

    private void initStream(final IPacket response) {
	stream.sid = response.getAttribute("sid");
	stream.wait = response.getAttribute("wait");
	stream.inactivity = response.getAttribute("inactivity");
	stream.maxPause = response.getAttribute("maxpause");
    }

    private boolean isTerminate(final String type) {
	// Openfire bug: terminal instead of terminate
	return "terminate".equals(type) || "terminal".equals(type);
    }

    /**
     * Sends a new request (and count the activeConnections)
     * 
     * @param request
     */
    private void send(final String request) {
	try {
	    activeConnections++;
	    services.send(userSettings.httpBase, request, callback);
	} catch (final ConnectorException e) {
	    activeConnections--;
	    e.printStackTrace();
	}
    }

    private void sendBody() {
	if (!shouldCollectResponses) {
	    final String request = services.toString(body);
	    body = null;
	    send(request);
	}
    }
}
