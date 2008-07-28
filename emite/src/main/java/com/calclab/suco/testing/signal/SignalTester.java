package com.calclab.suco.testing.signal;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.mockito.Mockito;

import com.calclab.suco.client.signal.Slot;

public class SignalTester<T> extends BaseMatcher<Slot<T>> {

    private Slot<T> slot;

    public SignalTester() {
    }

    public void describeTo(final Description description) {
	description.appendText("signal");
    }

    public void fire(final T parameter) {
	slot.onEvent(parameter);
    }

    public Slot<T> getSlot() {
	return Mockito.argThat(this);
    }

    @SuppressWarnings("unchecked")
    public boolean matches(final Object arg0) {
	try {
	    this.slot = (Slot<T>) arg0;
	} catch (final ClassCastException e) {
	    return false;
	}
	return true;
    }

    public <M> M mock(final M mock) {
	return Mockito.verify(mock);
    }

}
