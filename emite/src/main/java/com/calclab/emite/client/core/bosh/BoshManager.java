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
import com.calclab.emite.client.components.Globals;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.DispatcherComponent;
import com.calclab.emite.client.core.dispatcher.DispatcherStateListener;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.Event;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.core.services.Connector;
import com.calclab.emite.client.core.services.ConnectorCallback;
import com.calclab.emite.client.core.services.ConnectorException;
import com.calclab.emite.client.core.services.Scheduler;

public class BoshManager extends DispatcherComponent implements ConnectorCallback {

    public static class Events {
	public static final Event error = new Event("connection:has:error");
	public static final Event restart = new Event("connection:do:restart");
	public static final Event start = new Event("connection:do:start");
	public static final Event stop = new Event("connection:do:stop");;
    }

    private Activator activator;
    private final Connector connector;
    private final Dispatcher dispatcher;
    private final EmiteBosh emite;
    private final BoshOptions options;
    private final Scheduler scheduler;
    private final BoshState state;

    public BoshManager(final Dispatcher dispatcher, final Globals globals, final Connector connector,
	    final Scheduler scheduler, final EmiteBosh emite, final BoshOptions options) {
	super(dispatcher);
	this.dispatcher = dispatcher;
	this.connector = connector;
	this.scheduler = scheduler;
	this.emite = emite;
	this.options = options;
	this.activator = new Activator(this);
	this.state = new BoshState();

	globals.setDomain(options.getDomain());
	globals.setResourceName(options.getResource());

	dispatcher.addListener(new DispatcherStateListener() {
	    public void afterDispatching() {
		firePackets();
	    }

	    public void beforeDispatching() {
		catchPackets();
	    }
	});

    }

    @Override
    public void attach() {
	when(BoshManager.Events.restart, new PacketListener() {
	    public void handle(final Packet received) {
		setRestart();
	    }
	});
	when(BoshManager.Events.start, new PacketListener() {
	    public void handle(final Packet received) {
		state.setRunning(true);
		emite.restartRID();
		emite.initBody(null);
		emite.getBody().setCreationState(options.getDomain());
	    }
	});
	when(BoshManager.Events.error, new PacketListener() {
	    public void handle(final Packet stanza) {
		state.setRunning(false);
	    }
	});

	// on Body received
	when("body", new PacketListener() {
	    public void handle(final Packet packet) {
		publishBodyStanzas(new Body(packet));
	    }
	});
	when(BoshManager.Events.stop, new PacketListener() {
	    public void handle(final Packet received) {
		setTerminate();
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
	dispatcher.publish(BoshManager.Events.error);
    }

    /**
     * An incoming body from the server
     * 
     * @see ConnectorCallback
     */
    public void onResponseReceived(final int statusCode, final String content) {
	state.decreaseRequests();
	if (statusCode >= 400) {
	    dispatcher.publish(BoshManager.Events.error);
	} else {
	    final Packet response = emite.bodyFromResponse(content);
	    if ("body".equals(response.getName())) {
		dispatcher.publish(response);
	    } else {
		dispatcher.publish(BoshManager.Events.error);
	    }
	}
    }

    public void setRestart() {
	emite.getBody().setRestart(options.getDomain());
    }

    public void setTerminate() {
	emite.getBody().setTerminate();
	state.setTerminating();
    }

    void sendResponse() {
	try {
	    Log.debug("SENDING. Current: " + state.getCurrentRequestsCount() + ", after: "
		    + (scheduler.getCurrentTime() - state.getLastSendTime()));
	    connector.send(options.getHttpBase(), emite.getResponse(), this);
	    emite.clearBody();
	    final long now = scheduler.getCurrentTime();
	    final long last = state.getLastSendTime();
	    Log.debug("BOSH SEND: " + last + " -> " + now + "(" + (now - last) + ")");
	    state.setLastSend(now);
	    state.increaseRequests();
	} catch (final ConnectorException e) {
	    dispatcher.publish(BoshManager.Events.error);
	}
    }

    private void catchPackets() {
	emite.initBody(state.getSID());
    }

    private void delaySend() {
	this.activator = new Activator(this);
	final int ms = state.getPoll();
	final int diference = (int) (scheduler.getCurrentTime() - state.getLastSendTime());
	int total = ms - diference;
	Log.debug("DELAYING - poll: " + ms + ", diff: " + diference + ", total: " + total);
	if (total < 1) {
	    total = 1;
	}
	scheduler.schedule(total, activator);
    }

    private void firePackets() {
	activator.cancel();
	if (state.isRunning()) {
	    if (emite.getBody().isEmpty()) {
		if (state.getCurrentRequestsCount() > 0) {
		    // no need
		} else {
		    delaySend();
		}
	    } else {
		sendResponse();
	    }
	} else {
	    Log.debug("FIRE PACKETS CANCELLED BECAUSE WE'RE STOPPED");
	}
    }

    private void publishBodyStanzas(final Body response) {
	if (state.isTerminating()) {
	    onStopComponent();
	} else if (state.isFirstResponse()) {
	    final String sid = response.getSID();
	    state.setSID(sid);
	    emite.getBody().setSID(sid);
	    state.setPoll(response.getPoll() + 500);
	}
	state.setLastResponseEmpty(response.isEmpty());
	if (response.isTerminal()) {
	    dispatcher.publish(new Event(BoshManager.Events.error).Because(response.getCondition()));
	} else {
	    final List<? extends Packet> children = response.getChildren();
	    for (final Packet stanza : children) {
		dispatcher.publish(stanza);
	    }
	}
    }

}
