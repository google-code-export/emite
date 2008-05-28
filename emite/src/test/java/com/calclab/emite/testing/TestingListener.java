package com.calclab.emite.testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.calclab.emite.client.core.signal.Listener;

public class TestingListener<S> implements Listener<S> {

    private boolean isCalled;
    private S parameter;

    public TestingListener() {
	reset();
    }

    public S getValue() {
	return parameter;
    }

    public void onEvent(final S parameter) {
	if (isCalled) {
	    throw new RuntimeException("listener called twice!");
	}
	this.parameter = parameter;
	this.isCalled = true;
    }

    public void reset() {
	isCalled = false;
	parameter = null;
    }

    public void verify() {
	assertTrue("listener should be called", isCalled);
    }

    public void verify(final S parameter) {
	verify();
	assertEquals(parameter, this.parameter);
    }

    public void verifyNotCalled() {
	assertFalse("listener should NOT be called", isCalled);
    }

}
