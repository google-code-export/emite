package com.calclab.emite.testing;

import org.junit.Test;
import static com.calclab.emite.testing.SlotTester.*;

public class ListenerTesterTest {

    @Test
    public void shouldVerify() {
	final SlotTester<Object> listener = new SlotTester<Object>();
	final Object param1 = new Object();
	listener.onEvent(param1);
	verifyCalledWith(listener, param1);
    }
}
