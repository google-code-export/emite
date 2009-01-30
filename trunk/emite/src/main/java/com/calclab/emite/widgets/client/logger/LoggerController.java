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
package com.calclab.emite.widgets.client.logger;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.suco.client.events.Listener;
import com.calclab.suco.client.events.Listener0;

public class LoggerController {
    private final Connection connection;

    public LoggerController(final Connection connection) {
	this.connection = connection;
    }

    public void setWidget(final LoggerWidget widget) {
	widget.onClear.add(new Listener0() {
	    public void onEvent() {
		widget.clearContent();
	    }
	});

	connection.onStanzaReceived(new Listener<IPacket>() {
	    public void onEvent(final IPacket stanza) {
		widget.write(LoggerWidget.RECEIVED, stanza.toString());
	    }
	});

	connection.onStanzaSent(new Listener<IPacket>() {
	    public void onEvent(final IPacket stanza) {
		widget.write(LoggerWidget.SENT, stanza.toString());
	    }
	});

	connection.onError(new Listener<String>() {
	    public void onEvent(final String message) {
		Log.debug("ERROR: " + message);
		widget.write(LoggerWidget.ERROR, message);
	    }
	});
    }
}
