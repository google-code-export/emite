package com.calclab.module.client.signal;

import org.junit.Test;

import com.calclab.emite.testing.ListenerTester;
import com.calclab.modular.client.signal.Signal;

import static com.calclab.emite.testing.ListenerTester.*;

public class SignalTest {

    @Test
    public void shouldRemoveListener() {
	final Signal<Object> signal = new Signal<Object>();
	final ListenerTester<Object> listener = new ListenerTester<Object>();
	signal.add(listener);
	signal.remove(listener);
	signal.fire(new Object());
	verifyNoCalled(listener);
    }

    @Test
    public void shouldSignal() {
	final Signal<Object> signal = new Signal<Object>();
	final ListenerTester<Object> listener1 = new ListenerTester<Object>();
	signal.add(listener1);
	final ListenerTester<Object> listener2 = new ListenerTester<Object>();
	signal.add(listener2);
	final Object event = new Object();
	signal.fire(event);
	verifyCalledWith(listener1, event);
	verifyCalledWith(listener2, event);
    }
}
