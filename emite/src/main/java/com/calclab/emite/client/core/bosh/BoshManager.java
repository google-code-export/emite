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

import static com.calclab.emite.client.core.dispatcher.matcher.Matchers.when;

import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.components.Installable;
import com.calclab.emite.client.core.bosh.Bosh.BoshState;
import com.calclab.emite.client.core.dispatcher.DispatcherStateListener;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.Event;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.services.ConnectorCallback;
import com.calclab.emite.client.core.services.ConnectorException;
import com.calclab.emite.client.core.services.ScheduledAction;
import com.calclab.emite.client.core.services.Services;

/**
 * 
 * @author dani
 * 
 */
public class BoshManager implements ConnectorCallback, DispatcherStateListener, Installable {

    public static class Events {
	public static final Event onRestart = new Event("connection:do:restart");
	public static final Event onDoStart = new Event("connection:do:start");
	public static final Event stop = new Event("connection:do:stop");
	public static final Event onError = new Event("connection:on:error");
	protected final static Event pull = new Event("connection:do:pull");

	public static Event error(final String cause, final String info) {
	    return (Event) onError.Params("cause", cause).With("info", info);
	}

	public static IPacket start(final String domain) {
	    return BoshManager.Events.onDoStart.Params("domain", domain);
	}
    }

    private boolean isRunning;
    private final Services services;
    private final Bosh bosh;
    private final Emite emite;
    private String domain;

    public BoshManager(final Services services, final Emite emite, final Bosh bosh) {
	this.services = services;
	this.emite = emite;
	this.bosh = bosh;
	emite.addListener(this);
    }

    public void dispatchingBegins() {
	Log.debug("DISPATCH BEGINS >>>>>>>>>>>");
	if (isRunning()) {
	    bosh.prepareBody();
	}
    }

    public void dispatchingEnds() {
	Log.debug("<<<<<<<<<<<< DISPATCH ENDS");
	if (isRunning()) {
	    final BoshState state = bosh.getState(services.getCurrentTime());
	    if (state.shouldSend()) {
		sendResponse();
	    } else if (state.shouldWait()) {
		pull(state.getTime());
	    }
	} else {
	    Log.debug("BOSH IS STOP. NO NEED TO SEND ANYTHING");
	}
    }

    public void install() {
	emite.subscribe(when(BoshManager.Events.onRestart), new PacketListener() {
	    public void handle(final IPacket received) {
		bosh.setRestart(domain);
	    }
	});
	emite.subscribe(when(BoshManager.Events.onDoStart), new PacketListener() {
	    public void handle(final IPacket received) {
		eventStart(domain);
	    }

	});
	emite.subscribe(when("body"), new PacketListener() {
	    public void handle(final IPacket iPacket) {
		if (isTerminal(iPacket)) {
		    error("terminal", iPacket.getAttribute("condition"));
		} else {
		    if (bosh.isFirstResponse()) {
			final String sid = iPacket.getAttribute("sid");
			final int poll = iPacket.getAttributeAsInt("polling");
			final int requests = iPacket.getAttributeAsInt("requests");
			bosh.setAttributes(sid, poll, requests);
		    }
		    final List<? extends IPacket> children = iPacket.getChildren();
		    for (final IPacket stanza : children) {
			emite.publish(stanza);
		    }
		}
	    }
	});

	emite.subscribe(when(BoshManager.Events.onError), new PacketListener() {
	    public void handle(final IPacket received) {
		setRunning(false);
	    }
	});

	emite.subscribe(when("stream:error"), new PacketListener() {
	    public void handle(final IPacket received) {
		error("stream:error", "");
	    }
	});

	emite.subscribe(when(BoshManager.Events.stop), new PacketListener() {
	    public void handle(final IPacket received) {
		eventStop();
	    }
	});
    }

    public boolean isRunning() {
	return isRunning;
    }

    /**
     * When connector has an error
     * 
     * @see ConnectorCallback
     */
    public void onError(final Throwable throwable) {
	bosh.requestCountDecreases();
	error("connector-error", throwable.getMessage());
    }

    /**
     * An incoming body from the server
     * 
     * @see ConnectorCallback
     */
    public void onResponseReceived(final int statusCode, final String content) {
	bosh.requestCountDecreases();

	if (statusCode >= 400) {
	    error("response-status", "" + statusCode);
	} else {
	    final IPacket response = services.toXML(content);
	    if (!isRunning()) {

	    } else if (bosh.isTerminateSent()) {
		setRunning(false);
	    } else if ("body".equals(response.getName())) {
		emite.publish(response);
	    } else {
		error("bad-stanza", response.getName());
	    }
	}
    }

    public void setDomain(final String domain) {
	this.domain = domain;
    }

    public void setRunning(final boolean isRunning) {
	this.isRunning = isRunning;
    }

    public void uninstall() {
	// TODO Auto-generated method stub

    }

    void eventStop() {
	bosh.setTerminating(true);
    }

    private void error(final String cause, final String info) {
	setRunning(false);
	emite.publish(BoshManager.Events.error(cause, info));
    }

    private void eventStart(final String domain) {
	setDomain(domain);
	setRunning(true);
	this.bosh.init(services.getCurrentTime());

    }

    private boolean isTerminal(final IPacket body) {
	final String type = body.getAttribute("type");
	return "terminate".equals(type) || "terminal".equals(type);
    }

    private void pull(final int delay) {
	services.schedule(delay, new ScheduledAction() {
	    public void run() {
		emite.publish(Events.pull);
	    }
	});
    }

    private void sendResponse() {
	try {
	    services.send(bosh.getHttpBase(), services.toString(bosh.getResponse()), this);
	    bosh.requestCountEncreasesAt(services.getCurrentTime());
	} catch (final ConnectorException e) {
	    error("connector:send-exception", e.getMessage());
	}
    }
}
