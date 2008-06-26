package com.calclab.emite.testing;

import static com.calclab.emite.testing.MockSlot.verifyCalledWith;

import org.junit.Test;

public class ListenerTesterTest {

    @Test
    public void shouldVerify() {
	final MockSlot<Object> listener = new MockSlot<Object>();
	final Object param1 = new Object();
	listener.onEvent(param1);
	verifyCalledWith(listener, param1);
    }
}
