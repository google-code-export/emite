package com.calclab.suco.client.signal;

import static com.calclab.emite.testing.MockSlot.verifyCalledWith;
import static com.calclab.emite.testing.MockSlot.verifyNoCalled;

import org.junit.Test;

import com.calclab.emite.testing.MockSlot;

public class SignalTest {

    @Test
    public void shouldRemoveListener() {
	final Signal<Object> signal = new Signal<Object>("aSignal");
	final MockSlot<Object> listener = new MockSlot<Object>();
	signal.add(listener);
	signal.remove(listener);
	signal.fire(new Object());
	verifyNoCalled(listener);
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
	verifyCalledWith(listener1, event);
	verifyCalledWith(listener2, event);
    }
}
