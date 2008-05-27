package com.calclab.emite.client.core.signals;

import org.junit.Test;
import static org.mockito.Mockito.*;

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
