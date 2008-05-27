package com.calclab.emite.client.core.signals;

import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

public class SignalTest {

    @SuppressWarnings("unchecked")
    @Test
    public void shouldSignal() {
	final Signal<Object> signal = new Signal<Object>();
	final Listener<Object> listener1 = mock(Listener.class);
	signal.add(listener1);
	final Listener<Object> listener2 = mock(Listener.class);
	signal.add(listener2);
	final Object event = new Object();
	signal.fire(event);
	verify(listener1).onEvent(same(event));
	verify(listener2).onEvent(same(event));
    }
}
