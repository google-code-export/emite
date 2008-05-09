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

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.components.Component;
import com.calclab.emite.client.core.packet.IPacket;

public class Bosh implements Component {
    public static class BoshState {
	private final int time;

	private BoshState(final int time) {
	    this.time = time;
	}

	public int getTime() {
	    return time;
	}

	public boolean shouldIgnore() {
	    return time < 0;
	}

	public boolean shouldSend() {
	    return time == 0;
	}

	public boolean shouldWait() {
	    return time > 0;
	}
    }
    public static final BoshState SEND = new BoshState(0);
    public static final BoshState IGNORE = new BoshState(-1);

    static BoshState shouldWait(final int time) {
	return new BoshState(time);
    }
    private int currentConnections;
    private boolean isTerminating;
    private long lastSendTime;

    private int poll;
    private String sid;
    private final Stream stream;

    private int requests;

    private BoshOptions options;
    private String domain;

    public Bosh(final Stream iStream) {
	this.stream = iStream;
	this.options = new BoshOptions();
    }

    public int getCurrentRequestsCount() {
	return currentConnections;
    }

    public String getHttpBase() {
	return options.httpBase;
    }

    public BoshOptions getOptions() {
	return options;
    }

    public int getPoll() {
	return poll;
    }

    public IPacket getResponse() {
	return stream.clearBody();
    }

    public String getSID() {
	return sid;
    }

    public BoshState getState(final long currentTime) {
	BoshState state = null;

	if (!stream.isEmpty()) {
	    if (currentConnections < requests) {
		Log.debug("STATE - SEND: Not empty request and current connections ok (" + currentConnections + ")");
		state = SEND;
	    } else {
		Log.debug("STATE - NOT SEND: Not empty request, but too many connections (" + currentConnections + ")");
		state = IGNORE;
	    }
	} else {
	    if (currentConnections > 0) {
		Log.debug("STATE - NOT SEND: Empty request and connections running (" + currentConnections + ")");
		state = IGNORE;
	    } else {
		final int delay = (int) (currentTime - lastSendTime);
		if (delay <= poll) {
		    Log.debug("STATE - NOT SEND: Empty request, no conn but too frequent: " + delay);
		    state = Bosh.shouldWait(poll - delay);
		} else {
		    Log.debug("STATE - SEND: Empty request, not connections (" + currentConnections
			    + ") and delay ok: " + delay + " with poll: " + poll);
		    state = SEND;
		}
	    }
	}
	return state;
    }

    public void init(final long currentTime, final String domain) {
	this.domain = domain;
	this.sid = null;
	this.currentConnections = 0;
	this.poll = 1;
	this.isTerminating = false;
	lastSendTime = currentTime;
	this.stream.start(domain);
    }

    public boolean isFirstResponse() {
	return sid == null;
    }

    public boolean isTerminateSent() {
	return isTerminating;
    }

    public void prepareBody() {
	stream.prepareBody(getSID());
    }

    public void requestCountDecreases() {
	currentConnections--;
    }

    public void requestCountEncreasesAt(final long lastSendTime) {
	this.lastSendTime = lastSendTime;
	currentConnections++;
    }

    public void setAttributes(final String sid, final int poll, final int requests) {
	this.poll = poll * 1000 + options.wait;
	this.requests = requests;
	setSID(sid);
    }

    public void setOptions(final BoshOptions boshOptions) {
	this.options = boshOptions;
    }

    public void setRestart() {
	stream.setRestart(domain);
    }

    public void setTerminating(final boolean isTerminating) {
	this.isTerminating = isTerminating;
	if (isTerminating) {
	    stream.setTerminate();
	}
    }

    private void setSID(final String sid) {
	if (this.sid != null) {
	    final String message = "can't change the sid";
	    Log.error(message);
	    throw new RuntimeException(message);
	}
	stream.setSID(sid);
	this.sid = sid;
    }

}
