package com.calclab.emite.testing;

import org.junit.Test;

import com.calclab.suco.testing.MockSlot;

public class ListenerTesterTest {

    @Test
    public void shouldVerify() {
	final MockSlot<Object> listener = new MockSlot<Object>();
	final Object param1 = new Object();
	listener.onEvent(param1);
	MockSlot.verifyCalledWith(listener, param1);
    }
}
