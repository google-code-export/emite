package com.calclab.suco.testing;

import java.util.ArrayList;

import com.calclab.suco.client.signal.Slot2;

public class MockSlot2<A, B> implements Slot2<A, B> {

    private final ArrayList<A> paramsA;
    private final ArrayList<B> paramsB;

    public MockSlot2() {
	paramsA = new ArrayList<A>();
	paramsB = new ArrayList<B>();
    }

    public int getCalledTimes() {
	return paramsA.size();
    }

    public void onEvent(final A paramA, final B paramB) {
	paramsA.add(paramA);
	paramsB.add(paramB);
    }

}
