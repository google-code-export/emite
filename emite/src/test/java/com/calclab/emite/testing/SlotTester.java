package com.calclab.emite.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.ArrayList;

import com.calclab.suco.client.signal.Slot;

public class SlotTester<S> implements Slot<S> {

    public static void verifyCalled(final SlotTester<?> slot) {
	verifyCalled(slot, 1);
    }

    public static void verifyCalled(final SlotTester<?> slot, final int times) {
	assertEquals("slot should be called " + times + " times", times, slot.calledTimes);
    }

    public static <T> void verifyCalledWith(final SlotTester<T> slot, final T expected) {
	verifyCalled(slot);
	assertEquals(expected, slot.getValue(0));
    }

    public static <T> void verifyCalledWithSame(final SlotTester<T> slot, final T... expected) {
	verifyCalled(slot);
	for (int index = 0; index < expected.length; index++) {
	    assertSame(expected[index], slot.getValue(index));
	}
    }

    public static void verifyNoCalled(final SlotTester<?> slot) {
	verifyCalled(slot, 0);
    }

    private int calledTimes;

    private final ArrayList<S> parameters;

    public SlotTester() {
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
