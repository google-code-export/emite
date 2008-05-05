package com.calclab.emite.client.core.bosh;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.core.bosh.Bosh.BoshState;

public class BoshTest {

    private IStream iStream;
    private Bosh bosh;
    private BoshOptions options;

    @Before
    public void aaCreate() {
	iStream = mock(IStream.class);
	options = new BoshOptions("domain");
	bosh = new Bosh(iStream, options);
    }

    @Test
    public void shouldPrepareBody() {
	bosh.prepareBody();
	verify(iStream).prepareBody(null);
    }

    @Test
    public void shouldRestartStream() {
	bosh.init(0, "the-domain");
	bosh.setRestart();
	verify(iStream).setRestart("the-domain");
    }

    @Test
    public void shouldSetAttributes() {
	bosh.setAttributes("theSID", 5, 2);
	assertEquals(5000 + options.getWait(), bosh.getPoll());
    }

    @Test
    public void whenBodyEmptyCheckRequestCount() {
	stub(iStream.isEmpty()).toReturn(true);
	bosh.setAttributes("theSID", 5, 2);
	bosh.init(0, "the domain");
	assertTrue(bosh.getState(10).shouldSend());
	bosh.requestCountEncreasesAt(20);
	assertTrue(bosh.getState(1000000).shouldIgnore());
    }

    @Test
    public void whenBodyEmptyNotPoll() {
	stub(iStream.isEmpty()).toReturn(true);
	bosh.init(0, "the-domain");
	bosh.setAttributes("theSid", 5, 2);
	bosh.requestCountEncreasesAt(1000);
	bosh.requestCountDecreases();
	final BoshState state = bosh.getState(2000);
	assertTrue(state.shouldWait());
	assertEquals(4000 + options.getWait(), state.getTime());
    }

    @Test
    public void whenBodyNotEmptyCheckRequestCount() {
	stub(iStream.isEmpty()).toReturn(false);
	bosh.setAttributes("theSID", 5, 2);
	assertTrue(bosh.getState(System.currentTimeMillis()).shouldSend());
	bosh.requestCountEncreasesAt(System.currentTimeMillis());
	assertTrue(bosh.getState(System.currentTimeMillis()).shouldSend());
	bosh.requestCountEncreasesAt(System.currentTimeMillis());
	assertTrue(bosh.getState(System.currentTimeMillis()).shouldIgnore());
    }

}
