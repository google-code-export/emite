package com.calclab.module.client.signal;

import static com.calclab.emite.testing.SlotTester.verifyCalledWith;
import static com.calclab.emite.testing.SlotTester.verifyNoCalled;

import org.junit.Test;

import com.calclab.emite.testing.SlotTester;
import com.calclab.modular.client.signal.Signal;

public class SignalTest {

    @Test
    public void shouldRemoveListener() {
	final Signal<Object> signal = new Signal<Object>("aSignal");
	final SlotTester<Object> listener = new SlotTester<Object>();
	signal.add(listener);
	signal.remove(listener);
	signal.fire(new Object());
	verifyNoCalled(listener);
    }

    @Test
    public void shouldSignal() {
	final Signal<Object> signal = new Signal<Object>("aSignal");
	final SlotTester<Object> listener1 = new SlotTester<Object>();
	signal.add(listener1);
	final SlotTester<Object> listener2 = new SlotTester<Object>();
	signal.add(listener2);
	final Object event = new Object();
	signal.fire(event);
	verifyCalledWith(listener1, event);
	verifyCalledWith(listener2, event);
    }
}
