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

public class BoshState {
    private int currentConnections;
    private boolean isLastResponseEmpty;
    private boolean isRunning;
    private boolean isTerminating;
    private long lastSendTime;
    private int poll;
    private String sid;

    public BoshState() {
	init();
    }

    public void decreaseRequests() {
	currentConnections--;
    }

    public int getCurrentRequestsCount() {
	return currentConnections;
    }

    public long getLastSendTime() {
	return lastSendTime;
    }

    public int getPoll() {
	return poll;
    }

    public String getSID() {
	return sid;
    }

    public void increaseRequests() {
	currentConnections++;
    }

    public boolean isFirstResponse() {
	return sid == null;
    }

    public boolean isLastResponseEmpty() {
	return isLastResponseEmpty;
    }

    public boolean isRunning() {
	return isRunning;
    }

    public boolean isTerminating() {
	return isTerminating;
    }

    public void setLastResponseEmpty(final boolean isLastResponseEmpty) {
	this.isLastResponseEmpty = isLastResponseEmpty;

    }

    public void setLastSend(final long lastSendTime) {
	this.lastSendTime = lastSendTime;

    }

    public void setPoll(final int poll) {
	this.poll = poll;
    }

    public void setRunning(final boolean isRunning) {
	if (isRunning) {
	    this.isRunning = isRunning;
	} else {
	    init();
	}
    }

    public void setSID(final String sid) {
	Log.debug("Setting SID!" + sid);
	this.sid = sid;
    }

    public void setTerminating() {
	this.isTerminating = true;

    }

    private void init() {
	Log.debug("INIT BOSH STATE");
	this.isRunning = false;
	this.sid = null;
	this.currentConnections = 0;
	this.poll = 1;
	this.isTerminating = false;
    }

}
