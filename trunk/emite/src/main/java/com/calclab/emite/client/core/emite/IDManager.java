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
package com.calclab.emite.client.core.emite;

import java.util.HashMap;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.IPacket;

class IDManager {
    private int id;
    private final HashMap<String, PacketListener> listeners;

    public IDManager() {
	id = 0;
	this.listeners = new HashMap<String, PacketListener>();
    }

    public void handle(final IPacket received) {
	final String key = received.getAttribute("id");
	final PacketListener listener = listeners.remove(key);
	if (listener != null) {
	    Log.debug("ID LISTENER FOUNDED : " + key);
	    listener.handle(received);
	}
    }

    public String register(final String category, final PacketListener listener) {
	id++;
	final String key = category + "_" + id;
	listeners.put(key, listener);
	return key;
    }

}
