package com.calclab.suco.client.signal;

import java.util.ArrayList;

import com.allen_sauer.gwt.log.client.Log;

public class Signal0 {
    private ArrayList<Slot0> slots;
    private final String id;

    public Signal0(final String id) {
	this.id = id;
	slots = null;
    }

    public void add(final Slot0 slot) {
	if (slots == null) {
	    this.slots = new ArrayList<Slot0>();
	}
	slots.add(slot);
    }

    public void fire() {
	Log.debug("Signal " + id);
	if (slots != null) {
	    for (final Slot0 listener : slots) {
		listener.onEvent();
	    }
	}
    }

    public void remove(final Slot0 listener) {
	if (slots != null) {
	    slots.remove(listener);
	}
    }
}
