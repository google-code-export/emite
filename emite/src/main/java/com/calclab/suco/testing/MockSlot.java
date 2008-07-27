package com.calclab.suco.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import com.calclab.suco.client.signal.Slot;

public class MockSlot<S> implements Slot<S> {

    public static void verifyCalled(final MockSlot<?> slot) {
	verifyCalled(slot, 1);
    }

    public static void verifyCalled(final MockSlot<?> slot, final int times) {
	assertTrue("slot should be called " + times + " times", slot.isCalled(times));
    }

    public static <T> void verifyCalledWith(final MockSlot<T> slot, final T... expectedOrderedValues) {
	verifyCalled(slot, expectedOrderedValues.length);
	for (int index = 0; index < expectedOrderedValues.length; index++) {
	    final T expected = expectedOrderedValues[index];
	    assertEquals(expected, slot.getValue(index));
	}
    }

    public static <T> void verifyCalledWithSame(final MockSlot<T> slot, final T... expected) {
	verifyCalled(slot);
	for (int index = 0; index < expected.length; index++) {
	    assertSame(expected[index], slot.getValue(index));
	}
    }

    public static void verifyNoCalled(final MockSlot<?> slot) {
	verifyCalled(slot, 0);
    }

    private final ArrayList<S> parameters;

    public MockSlot() {
	parameters = new ArrayList<S>();
    }

    public int getCalledTimes() {
	return parameters.size();
    }

    public S getValue(final int index) {
	return parameters.get(index);
    }

    public boolean isCalled(final int timesCalled) {
	return getCalledTimes() == timesCalled;
    }

    public void onEvent(final S parameter) {
	parameters.add(parameter);
    }

}
