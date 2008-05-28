package com.calclab.emite.client.core.signal;

import java.util.ArrayList;

public class Signal<T> {
    private final ArrayList<Listener<T>> listeners;

    public Signal() {
	this.listeners = new ArrayList<Listener<T>>();
    }

    public void add(final Listener<T> listener) {
	listeners.add(listener);
    }

    public void fire(final T event) {
	for (final Listener<T> listener : listeners) {
	    listener.onEvent(event);
	}
    }

}
