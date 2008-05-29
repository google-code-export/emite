package com.calclab.emite.testing;

import static org.junit.Assert.*;

import java.util.ArrayList;
import com.calclab.emite.client.core.signal.Listener;

public class ListenerTester<S> implements Listener<S> {

    public static void verifyCalled(final ListenerTester<?> listener) {
	verifyCalled(listener, 1);
    }

    public static void verifyCalled(final ListenerTester<?> listener, final int times) {
	assertEquals("listener should be called " + times + " times", times, listener.calledTimes);
    }

    public static <T> void verifyCalledWith(final ListenerTester<T> listener, final T expected) {
	verifyCalled(listener);
	assertEquals(expected, listener.getValue(0));
    }

    public static <T> void verifyCalledWithSame(final ListenerTester<T> listener, final T... expected) {
	verifyCalled(listener);
	for (int index = 0; index < expected.length; index++) {
	    assertSame(expected[index], listener.getValue(index));
	}
    }

    public static void verifyNoCalled(final ListenerTester<?> listener) {
	verifyCalled(listener, 0);
    }

    private int calledTimes;

    private final ArrayList<S> parameters;

    public ListenerTester() {
	calledTimes = 0;
	parameters = new ArrayList<S>();
    }

    public S getValue(final int index) {
	return parameters.get(index);
    }

    public void onEvent(final S parameter) {
	parameters.add(parameter);
	this.calledTimes++;
    }

}
