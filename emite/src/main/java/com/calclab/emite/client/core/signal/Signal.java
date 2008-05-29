package com.calclab.emite.client.core.signal;

import java.util.ArrayList;

import com.allen_sauer.gwt.log.client.Log;

public class Signal<T> {
    private ArrayList<Listener<T>> listeners;

    public Signal() {
	listeners = null;
    }

    public void add(final Listener<T> listener) {
	if (listeners == null) {
	    this.listeners = new ArrayList<Listener<T>>();
	}
	listeners.add(listener);
    }

    public void fire(final T event) {
	Log.debug("Signal fired: " + event);
	if (listeners != null) {
	    for (final Listener<T> listener : listeners) {
		listener.onEvent(event);
	    }
	}
    }

    public void remove(final Listener<T> listener) {
	if (listeners != null) {
	    listeners.remove(listener);
	}
    }

}
