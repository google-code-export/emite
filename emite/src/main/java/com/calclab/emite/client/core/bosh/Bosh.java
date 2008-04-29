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
import com.calclab.emite.client.core.packet.IPacket;

public class Bosh {
    public static final int TIME_NOW = 0;
    public static final int TIME_NEVER = -1;

    private int currentConnections;
    private boolean isTerminating;
    private long lastSendTime;
    private int poll;
    private String sid;

    private boolean isCurrentResponseEmpty;
    private final String domain;
    private final Stream stream;

    public Bosh(final Stream stream, final BoshOptions options) {
	this.stream = stream;
	this.domain = options.getHttpBase();
    }

    public int getCurrentRequestsCount() {
	return currentConnections;
    }

    public String getDomain() {
	return domain;
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

    public int getState(final long currentTime) {
	setResponseEmpty(stream.isEmpty());
	int time = 0;
	if (!isCurrentResponseEmpty) {
	    Log.debug("Sending not empty. Conn: " + currentConnections);
	    time = TIME_NOW;
	} else {
	    if (currentConnections > 0) {
		Log.debug("Not send. Too many conn: " + currentConnections);
		time = TIME_NEVER;
	    } else {
		final int delay = getNecesaryDelayFromLastRequest(currentTime);
		if (delay > 0) {
		    time = delay;
		} else {
		    Log.debug("Sending: Poll overhead: " + delay + ", connections: " + currentConnections);
		    time = TIME_NOW;
		}
	    }
	}
	return time;
    }

    public void init(final long currentTime) {
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

    public void requestSentAt(final long lastSendTime) {
	this.lastSendTime = lastSendTime;
	currentConnections++;
    }

    public void responseRecevied() {
	currentConnections--;
    }

    public void setPoll(final int poll) {
	this.poll = poll;
    }

    public void setResponseEmpty(final boolean isResponseEmpty) {
	this.isCurrentResponseEmpty = isResponseEmpty;
    }

    public void setRestart(final String domain) {
	stream.setRestart(domain);
    }

    public void setSID(final String sid) {
	if (this.sid != null) {
	    throw new RuntimeException("can't change the sid");
	}
	stream.setSID(sid);
	this.sid = sid;
    }

    public void setTerminating(final boolean isTerminating) {
	this.isTerminating = isTerminating;
	if (isTerminating) {
	    stream.setTerminate();
	}
    }

    private int getNecesaryDelayFromLastRequest(final long currentTime) {
	final int ms = poll;
	final int diference = (int) (currentTime - lastSendTime);
	final int total = ms - diference;
	return total;
    }

}
