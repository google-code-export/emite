package com.calclab.emite.testing;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import com.calclab.emite.client.core.signal.Listener;

public class TestSignal<T> extends BaseMatcher<Listener<T>> {

    private Listener<T> listener;

    public void describeTo(final Description description) {
	description.appendText("signal");
    }

    public void fire(final T parameter) {
	listener.onEvent(parameter);
    }

    @SuppressWarnings("unchecked")
    public boolean matches(final Object arg0) {
	try {
	    this.listener = (Listener<T>) arg0;
	} catch (final ClassCastException e) {
	    return false;
	}
	return true;
    }
}
