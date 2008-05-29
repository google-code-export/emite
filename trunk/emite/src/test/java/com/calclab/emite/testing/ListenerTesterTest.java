package com.calclab.emite.testing;

import org.junit.Test;
import static com.calclab.emite.testing.ListenerTester.*;

public class ListenerTesterTest {

    @Test
    public void shouldVerify() {
	final ListenerTester<Object> listener = new ListenerTester<Object>();
	final Object param1 = new Object();
	listener.onEvent(param1);
	verifyCalledWith(listener, param1);
    }
}
