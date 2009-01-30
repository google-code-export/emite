/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emiteuimodule.client.status;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Show;

public class OwnPresence {
    public enum OwnStatus {
	offline, online, onlinecustom, busy, busycustom
    }

    OwnStatus ownStatus;
    String statusText;

    public OwnPresence(final OwnStatus ownStatus) {
	this.ownStatus = ownStatus;
    }

    public OwnPresence(final OwnStatus ownStatus, final String statusText) {
	this(ownStatus);
	this.statusText = statusText;
	if (statusText.length() > 0 && !(ownStatus == OwnStatus.busycustom || ownStatus == OwnStatus.onlinecustom)) {
	    Log.error("Code error: statusText only can be set in onlinecustom and busycustom states");
	}
    }

    public OwnPresence(final Presence currentPresence) {
	this.statusText = currentPresence.getStatus();
	final Show show = currentPresence.getShow();
	final boolean hastStatusText = statusText != null;
	switch (show) {
	case dnd:
	    ownStatus = hastStatusText ? OwnStatus.busycustom : OwnStatus.busy;
	    break;
	case chat:
	    ownStatus = hastStatusText ? OwnStatus.onlinecustom : OwnStatus.online;
	    break;
	case away:
	case xa:
	    Log.error("Code error: Show : " + show + " is not support in current UI");
	default:
	    ownStatus = OwnStatus.online;
	}
    }

    public OwnStatus getStatus() {
	return ownStatus;
    }

    public String getStatusText() {
	return statusText;
    }

}
