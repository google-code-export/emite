package com.calclab.emite.testing;

import org.junit.Test;

import com.calclab.suco.testing.listener.MockListener;

public class ListenerTesterTest {

    @Test
    public void shouldVerify() {
	final MockListener<Object> listener = new MockListener<Object>();
	final Object param1 = new Object();
	listener.onEvent(param1);
	MockListener.verifyCalledWith(listener, param1);
    }
}
