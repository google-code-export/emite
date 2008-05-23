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
import com.calclab.emite.client.core.bosh.Bosh.BoshState;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.DispatcherStateListener;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.dispatcher.matcher.Matchers;
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
public class BoshManager implements ConnectorCallback, DispatcherStateListener {

    public static class Events {
	public static final Event onRestartStream = new Event("bosh-manager:do:restart_stream");
	public static final Event stop = new Event("bosh-manager:do:stop");
	protected final static Event pull = new Event("bosh-manager:do:pull");
	static final Event onDoXStart = new Event("bosh-manager:do:start");

	public static IPacket start(final String domain) {
	    return BoshManager.Events.onDoXStart.Params("domain", domain);
	}
    }

    public static class Signals {
	public void onStart(final Dispatcher dispatcher, final PacketListener listener) {
	    dispatcher.subscribe(Matchers.when(Events.onDoXStart), listener);
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
	install();
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

    public String getDomain() {
	return domain;
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
	emite.publish(Dispatcher.Events.error("connector-error", throwable.getMessage()));
    }

    /**
     * An incoming body from the server
     * 
     * @see ConnectorCallback
     */
    public void onResponseReceived(final int statusCode, final String content) {
	bosh.requestCountDecreases();

	if (statusCode >= 400) {
	    emite.publish(Dispatcher.Events.error("response-status", ("" + statusCode)));
	} else {
	    final IPacket response = services.toXML(content);
	    if (!isRunning()) {

	    } else if (bosh.isTerminateSent()) {
		setRunning(false);
	    } else if ("body".equals(response.getName())) {
		emite.publish(response);
	    } else {
		emite.publish(Dispatcher.Events.error("bad-stanza", response.getName()));
	    }
	}
    }

    public void setDomain(final String domain) {
	this.domain = domain;
    }

    /**
     * Set BoshManager in running state. (default) for testing purposes
     * 
     * @param isRunning
     */
    void setRunning(final boolean isRunning) {
	this.isRunning = isRunning;
    }

    private void install() {
	Log.debug("INSTALLING BOSH MANAGER");
	emite.subscribe(when(BoshManager.Events.onRestartStream), new PacketListener() {
	    public void handle(final IPacket received) {
		bosh.setRestart();
	    }
	});
	emite.subscribe(when(BoshManager.Events.onDoXStart), new PacketListener() {
	    public void handle(final IPacket received) {
		Log.debug("START IN BOSH MANAGER!!!");
		setDomain(received.getAttribute("domain"));
		setRunning(true);
		bosh.init(services.getCurrentTime(), getDomain());
	    }

	});
	emite.subscribe(when("body"), new PacketListener() {
	    public void handle(final IPacket packet) {
		if (isTerminal(packet)) {
		    emite.publish(Dispatcher.Events.error("terminal", packet.getAttribute("condition")));
		} else {
		    if (bosh.isFirstResponse()) {
			final String sid = packet.getAttribute("sid");
			final int poll = Integer.parseInt(packet.getAttribute("polling"));
			final int requests = Integer.parseInt(packet.getAttribute("requests"));
			bosh.setAttributes(sid, poll, requests);
		    }
		    final List<? extends IPacket> children = packet.getChildren();
		    for (final IPacket stanza : children) {
			emite.publish(stanza);
		    }
		}
	    }
	});

	emite.subscribe(when(Dispatcher.Events.onError), new PacketListener() {
	    public void handle(final IPacket received) {
		setRunning(false);
	    }
	});

	emite.subscribe(when("stream:error"), new PacketListener() {
	    public void handle(final IPacket received) {
		emite.publish(Dispatcher.Events.error("stream:error", ""));
	    }
	});

	emite.subscribe(when(BoshManager.Events.stop), new PacketListener() {
	    public void handle(final IPacket received) {
		bosh.setTerminating(true);
	    }
	});
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
	    emite.publish(Dispatcher.Events.error("connector:send-exception", e.getMessage()));
	} catch (final Exception e) {
	    emite.publish(Dispatcher.Events.error("connector:exception", e.getMessage()));
	}
    }
}
