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
package com.calclab.emite.client.core.bosh;

import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.core.dispatcher.DispatcherStateListener;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.emite.Body;
import com.calclab.emite.client.core.emite.EmiteBosh;
import com.calclab.emite.client.core.emite.EmiteComponent;
import com.calclab.emite.client.core.packet.Event;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.services.ConnectorCallback;
import com.calclab.emite.client.core.services.ConnectorException;
import com.calclab.emite.client.core.services.Services;

/**
 * 
 * @author dani
 * 
 */
public class BoshManager extends EmiteComponent implements ConnectorCallback {

    public static class Events {
	public static final Event onError = new Event("connection:on:error");
	/** ATTRIBUTE: domain */
	public static final Event restart = new Event("connection:do:restart");
	/** ATTRIBUTE: domain */
	public static final Event start = new Event("connection:do:start");
	public static final Event stop = new Event("connection:do:stop");
    }

    private Activator activator;
    private BoshState state;
    private boolean isRunning;
    private final EmiteBosh bosh;
    private final String httpBase;
    private final Services services;

    public BoshManager(final Services services, final EmiteBosh emite, final BoshOptions options) {
	super(emite);
	this.services = services;
	this.bosh = emite;
	this.httpBase = options.getHttpBase();
	this.activator = new Activator(this);

	dispatcher.addListener(new DispatcherStateListener() {
	    public void afterDispatching() {
		sendBody();
	    }

	    public void beforeDispatching() {
		prepareBody();
	    }
	});

    }

    @Override
    public void attach() {
	when(BoshManager.Events.restart, new PacketListener() {
	    public void handle(final IPacket received) {
		onRestartStream(received.getAttribute("domain"));
	    }
	});
	when(BoshManager.Events.start, new PacketListener() {
	    public void handle(final IPacket received) {
		onStartBosh(received.getAttribute("domain"));
	    }

	});
	when(BoshManager.Events.onError, new PacketListener() {
	    public void handle(final IPacket stanza) {
		onError();
	    }
	});

	when("body", new PacketListener() {
	    public void handle(final IPacket iPacket) {
		publishBodyStanzas(new Body(iPacket));
	    }
	});

	when(BoshManager.Events.stop, new PacketListener() {
	    public void handle(final IPacket received) {
		onSendStop();
	    }
	});
    }

    /**
     * When connector has an error
     * 
     * @see ConnectorCallback
     */
    public void onError(final Throwable throwable) {
	state.decreaseRequests();
	dispatcher.publish(BoshManager.Events.onError);
    }

    /**
     * An incoming body from the server
     * 
     * @see ConnectorCallback
     */
    public void onResponseReceived(final int statusCode, final String content) {
	state.decreaseRequests();
	// FIXME: which status codes are errors?
	if (statusCode >= 400) {
	    dispatcher.publish(BoshManager.Events.onError);
	} else {
	    final IPacket response = services.toXML(content);
	    if (!isRunning) {

	    } else if (state.isTerminateSent()) {
		isRunning = false;
	    } else if ("body".equals(response.getName())) {
		dispatcher.publish(response);
	    } else {
		dispatcher.publish(BoshManager.Events.onError);
	    }
	}
    }

    public void onRestartStream(final String domain) {
	bosh.getBody().setRestart(domain);
    }

    public void onSendStop() {
	bosh.getBody().setTerminate();
	state.setTerminating(true);
    }

    void sendResponse() {
	try {
	    Log.debug("SENDING. Current: " + state.getCurrentRequestsCount() + ", after: "
		    + (services.getCurrentTime() - state.getLastSendTime()));
	    services.send(httpBase, services.toString(bosh.getBody()), this);
	    bosh.clearBody();
	    final long now = services.getCurrentTime();
	    final long last = state.getLastSendTime();
	    Log.debug("BOSH SEND: " + last + " -> " + now + "(" + (now - last) + ")");
	    state.setLastSend(now);
	    state.increaseRequests();
	} catch (final ConnectorException e) {
	    dispatcher.publish(BoshManager.Events.onError);
	}
    }

    private void delaySend() {
	this.activator = new Activator(this);
	final int ms = state.getPoll();
	final int diference = (int) (services.getCurrentTime() - state.getLastSendTime());
	int total = ms - diference;
	Log.debug("DELAYING - poll: " + ms + ", diff: " + diference + ", total: " + total);
	if (total < 1) {
	    total = 1;
	}
	services.schedule(total, activator);
    }

    private void onError() {
	isRunning = false;
    }

    private void onStartBosh(final String domain) {
	isRunning = true;
	this.state = new BoshState();
	this.bosh.start(domain);
    }

    private void prepareBody() {
	if (isRunning) {
	    bosh.newRequest(state.getSID());
	}
    }

    private void publishBodyStanzas(final Body response) {
	if (state.isFirstResponse()) {
	    final String sid = response.getSID();
	    state.setSID(sid);
	    bosh.getBody().setSID(sid);
	    state.setPoll(response.getPoll() + 500);
	}
	state.setLastResponseEmpty(response.isEmpty());
	if (response.isTerminal()) {
	    dispatcher.publish(new Event(BoshManager.Events.onError).Because(response.getCondition()));
	} else {
	    final List<? extends IPacket> children = response.getChildren();
	    for (final IPacket stanza : children) {
		dispatcher.publish(stanza);
	    }
	}
    }

    private void sendBody() {
	activator.cancel();
	if (isRunning) {
	    if (bosh.getBody().isEmpty()) {
		if (state.getCurrentRequestsCount() > 0) {
		    // no need
		} else {
		    delaySend();
		}
	    } else {
		sendResponse();
	    }
	} else {
	    Log.debug("BOSH IS STOP. NO NEED TO SEND ANYTHING");
	}
    }

}
