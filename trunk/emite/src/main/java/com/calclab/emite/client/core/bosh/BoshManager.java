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
	public static final Event onDoRestart = new Event("connection:do:restart");

	public static final Event onDoStart = new Event("connection:do:start");
	public static final Event stop = new Event("connection:do:stop");
	public static final Event onError = new Event("connection:on:error");
	protected final static Event pull = new Event("connection:do:pull");

	public static Event error(final String cause, final String info) {
	    return (Event) onError.Params("cause", cause).With("info", info);
	}

	public static IPacket start(final String host) {
	    return BoshManager.Events.onDoStart.Params("domain", host);
	}
    }

    private final String httpBase;
    private boolean isRunning;
    private final Services services;
    private BoshState state;
    private final Stream stream;
    private final Emite emite;

    public BoshManager(final Services services, final Emite emite, final Stream stream, final BoshOptions options) {
	this.services = services;
	this.emite = emite;
	this.stream = stream;
	this.httpBase = options.getHttpBase();
	emite.addListener(this);
    }

    public void dispatchingBegins() {
	if (isRunning) {
	    Log.debug("DISPATCH BEGINS >>>>>>>>>>>");
	    stream.newRequest(state.getSID());
	}
    }

    public void dispatchingEnds() {
	Log.debug("<<<<<<<<<<<< DISPATCH ENDS");
	if (isRunning) {
	    state.setResponseEmpty(stream.isEmpty());
	    final int delay = state.getState(services.getCurrentTime());
	    Log.debug("DELAY: " + delay);
	    if (delay == BoshState.TIME_NOW) {
		sendResponse();
	    } else if (delay == BoshState.TIME_NEVER) {
		// we are currently bussy
	    } else {
		pull(delay);
	    }
	} else {
	    Log.debug("BOSH IS STOP. NO NEED TO SEND ANYTHING");
	}
    }

    public void install() {
	emite.subscribe(when(BoshManager.Events.onDoRestart), new PacketListener() {
	    public void handle(final IPacket received) {
		eventRestart(received.getAttribute("domain"));
	    }
	});
	emite.subscribe(when(BoshManager.Events.onDoStart), new PacketListener() {
	    public void handle(final IPacket received) {
		eventStart(received.getAttribute("domain"));
	    }

	});
	emite.subscribe(when("body"), new PacketListener() {
	    public void handle(final IPacket iPacket) {
		eventBody(iPacket);
	    }
	});

	emite.subscribe(when(BoshManager.Events.onError), new PacketListener() {
	    public void handle(final IPacket received) {
		eventError(received);
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
	state.responseRecevied();
	error("connector-error", throwable.getMessage());
    }

    /**
     * An incoming body from the server
     * 
     * @see ConnectorCallback
     */
    public void onResponseReceived(final int statusCode, final String content) {
	state.responseRecevied();

	if (statusCode >= 400) {
	    error("response-status", "" + statusCode);
	} else {
	    final IPacket response = services.toXML(content);
	    if (!isRunning) {

	    } else if (state.isTerminateSent()) {
		isRunning = false;
	    } else if ("body".equals(response.getName())) {
		emite.publish(response);
	    } else {
		error("bad-stanza", response.getName());
	    }
	}
    }

    public void uninstall() {
	// TODO Auto-generated method stub

    }

    void eventBody(final IPacket body) {
	if (isTerminal(body)) {
	    error("terminal", body.getAttribute("condition"));
	} else {
	    if (state.isFirstResponse()) {
		final String sid = body.getAttribute("sid");
		state.setSID(sid);
		stream.setSID(sid);
		state.setPoll(body.getAttributeAsInt("polling") * 1000 + 500);
	    }
	    final List<? extends IPacket> children = body.getChildren();
	    for (final IPacket stanza : children) {
		emite.publish(stanza);
	    }
	}
    }

    void eventError(final IPacket received) {
	isRunning = false;
    }

    void eventRestart(final String domain) {
	stream.setRestart(domain);
    }

    void eventStart(final String domain) {
	isRunning = true;
	this.state = new BoshState(services.getCurrentTime());
	this.stream.start(domain);
    }

    void eventStop() {
	stream.setTerminate();
	state.setTerminating(true);
    }

    BoshState getState() {
	return state;
    }

    private void error(final String cause, final String info) {
	isRunning = false;
	emite.publish(BoshManager.Events.error(cause, info));
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
	    services.send(httpBase, services.toString(stream.clearBody()), this);
	    state.requestSentAt(services.getCurrentTime());
	} catch (final ConnectorException e) {
	    error("connector:send-exception", e.getMessage());
	}
    }
}
