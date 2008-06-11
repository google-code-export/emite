package com.calclab.suco.client.signal;

import java.util.ArrayList;

import com.allen_sauer.gwt.log.client.Log;

public class Signal2<A, B> {
    private ArrayList<Slot2<A, B>> slots;
    private final String id;

    public Signal2(final String id) {
	this.id = id;
	slots = null;
    }

    public void add(final Slot2<A, B> slot) {
	if (slots == null) {
	    this.slots = new ArrayList<Slot2<A, B>>();
	}
	slots.add(slot);
    }

    public void fire(final A param1, final B param2) {
	Log.debug("Signal " + id + ": " + param1 + ", " + param2);
	if (slots != null) {
	    for (final Slot2<A, B> listener : slots) {
		listener.onEvent(param1, param2);
	    }
	}
    }

    public void remove(final Slot2<A, B> slot) {
	if (slots != null) {
	    slots.remove(slot);
	}
    }
}
