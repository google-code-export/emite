package com.calclab.emite.testing;

import static org.junit.Assert.assertTrue;

import com.calclab.emite.client.core.signal.Listener;

public class TestingListener<S> implements Listener<S> {

    private boolean isCalled;

    public TestingListener() {
	this.isCalled = false;
    }

    public void onEvent(final S parameter) {
	this.isCalled = true;
    }

    public void verify() {
	assertTrue("listener should be called", isCalled);
    }

}
