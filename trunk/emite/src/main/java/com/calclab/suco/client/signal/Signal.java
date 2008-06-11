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
package com.calclab.suco.client.signal;

import java.util.ArrayList;

import com.allen_sauer.gwt.log.client.Log;

public class Signal<T> {
    private ArrayList<Slot<T>> slots;
    private final String id;

    public Signal(final String id) {
	this.id = id;
	slots = null;
    }

    public void add(final Slot<T> slot) {
	if (slots == null) {
	    this.slots = new ArrayList<Slot<T>>();
	}
	slots.add(slot);
    }

    public void fire(final T event) {
	Log.debug("Signal " + id + ": " + event);
	if (slots != null) {
	    for (final Slot<T> listener : slots) {
		listener.onEvent(event);
	    }
	}
    }

    public void remove(final Slot<T> listener) {
	if (slots != null) {
	    slots.remove(listener);
	}
    }

}
