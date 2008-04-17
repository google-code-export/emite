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
public class BoshManager extends EmiteComponent implements ConnectorCallback, DispatcherStateListener {

    public static class Events {
	public static final Event onError = new Event("connection:on:error");
	/** ATTRIBUTE: domain */
	public static final Event restart = new Event("connection:do:restart");
	/** ATTRIBUTE: domain */
	public static final Event start = new Event("connection:do:start");
	public static final Event stop = new Event("connection:do:stop");
	protected final static Event pull = new Event("connection:do:pull");
    }

    private final String httpBase;
    private boolean isRunning;
    private final Services services;
    private BoshState state;
    private final Stream stream;

    public BoshManager(final Services services, final Emite emite, final Stream stream, final BoshOptions options) {
	super(emite);
	this.services = services;
	this.stream = stream;
	this.httpBase = options.getHttpBase();
	emite.addListener(this);
    }

    public void afterDispatching() {
	if (isRunning) {
	    state.setResponseEmpty(stream.isEmpty());
	    final int delay = state.getState(services.getCurrentTime());
	    Log.debug(">>>> " + delay);
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

    @Override
    public void attach() {
	emite.subscribe(when(BoshManager.Events.restart), new PacketListener() {
	    public void handle(final IPacket received) {
		eventRestart(received.getAttribute("domain"));
	    }
	});
	emite.subscribe(when(BoshManager.Events.start), new PacketListener() {
	    public void handle(final IPacket received) {
		eventStart(received.getAttribute("domain"));
	    }
	
	});
	emite.subscribe(when(BoshManager.Events.onError), new PacketListener() {
	    public void handle(final IPacket stanza) {
		eventError();
	    }
	});

	emite.subscribe(when("body"), new PacketListener() {
	    public void handle(final IPacket iPacket) {
		eventBody(iPacket);
	    }
	});

	emite.subscribe(when("stream:error"), new PacketListener() {
	    public void handle(final IPacket received) {
		emite.publish(BoshManager.Events.onError.Params("cause", "stream error").With(received));
	    }
	});

	emite.subscribe(when(BoshManager.Events.stop), new PacketListener() {
	    public void handle(final IPacket received) {
		eventStop();
	    }
	});
    }

    public void beforeDispatching() {
	if (isRunning) {
	    stream.newRequest(state.getSID());
	}
    }

    /**
     * When connector has an error
     * 
     * @see ConnectorCallback
     */
    public void onError(final Throwable throwable) {
	state.responseRecevied();
	emite.publish(BoshManager.Events.onError);
    }

    /**
     * An incoming body from the server
     * 
     * @see ConnectorCallback
     */
    public void onResponseReceived(final int statusCode, final String content) {
	state.responseRecevied();

	if (statusCode >= 400) {
	    emite.publish(BoshManager.Events.onError);
	} else {
	    final IPacket response = services.toXML(content);
	    if (!isRunning) {

	    } else if (state.isTerminateSent()) {
		isRunning = false;
	    } else if ("body".equals(response.getName())) {
		emite.publish(response);
	    } else {
		emite.publish(BoshManager.Events.onError);
	    }
	}
    }

    void eventBody(final IPacket response) {
	if (state.isFirstResponse()) {
	    final String sid = response.getAttribute("sid");
	    state.setSID(sid);
	    stream.setSID(sid);
	    state.setPoll(response.getAttributeAsInt("polling") * 1000 + 500);
	}
	if (response.hasAttribute("terminal")) {
	    emite.publish(new Event(BoshManager.Events.onError).Params("terminal", response.getAttribute("condition")));
	} else {
	    final List<? extends IPacket> children = response.getChildren();
	    for (final IPacket stanza : children) {
		emite.publish(stanza);
	    }
	}
    }

    void eventError() {
	isRunning = false;
    }

    void eventRestart(final String domain) {
	stream.setRestart(domain);
    }

    void eventStart(final String domain) {
	isRunning = true;
	this.state = new BoshState();
	this.stream.start(domain);
    }

    void eventStop() {
	stream.setTerminate();
	state.setTerminating(true);
    }

    BoshState getState() {
	return state;
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
	    emite.publish(BoshManager.Events.onError);
	}
    }
}
