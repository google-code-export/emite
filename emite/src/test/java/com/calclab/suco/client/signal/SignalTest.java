package com.calclab.suco.client.signal;

import org.junit.Test;

import com.calclab.suco.testing.MockSlot;

public class SignalTest {

    @Test
    public void shouldRemoveListener() {
	final Signal<Object> signal = new Signal<Object>("aSignal");
	final MockSlot<Object> listener = new MockSlot<Object>();
	signal.add(listener);
	signal.remove(listener);
	signal.fire(new Object());
	MockSlot.verifyNoCalled(listener);
    }

    @Test
    public void shouldSignal() {
	final Signal<Object> signal = new Signal<Object>("aSignal");
	final MockSlot<Object> listener1 = new MockSlot<Object>();
	signal.add(listener1);
	final MockSlot<Object> listener2 = new MockSlot<Object>();
	signal.add(listener2);
	final Object event = new Object();
	signal.fire(event);
	MockSlot.verifyCalledWith(listener1, event);
	MockSlot.verifyCalledWith(listener2, event);
    }
}
