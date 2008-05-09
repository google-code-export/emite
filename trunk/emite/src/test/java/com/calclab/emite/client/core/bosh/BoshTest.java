package com.calclab.emite.client.core.bosh;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.core.bosh.Bosh.BoshState;

public class BoshTest {

    private Stream stream;
    private Bosh bosh;
    private BoshOptions options;

    @Before
    public void aaCreate() {
	stream = mock(Stream.class);
	bosh = new Bosh(stream);
	options = bosh.getOptions();
    }

    @Test
    public void shouldPrepareBody() {
	bosh.prepareBody();
	verify(stream).prepareBody(null);
    }

    @Test
    public void shouldRestartStream() {
	bosh.init(0, "the-domain");
	bosh.setRestart();
	verify(stream).setRestart("the-domain");
    }

    @Test
    public void shouldSetAttributes() {
	bosh.setAttributes("theSID", 5, 2);
	assertEquals(5000 + options.wait, bosh.getPoll());
    }

    @Test
    public void whenBodyEmptyCheckRequestCount() {
	stub(stream.isEmpty()).toReturn(true);
	bosh.setAttributes("theSID", 5, 2);
	bosh.init(0, "the domain");
	assertTrue(bosh.getState(10).shouldSend());
	bosh.requestCountEncreasesAt(20);
	assertTrue(bosh.getState(1000000).shouldIgnore());
    }

    @Test
    public void whenBodyEmptyNotPoll() {
	stub(stream.isEmpty()).toReturn(true);
	bosh.init(0, "the-domain");
	bosh.setAttributes("theSid", 5, 2);
	bosh.requestCountEncreasesAt(1000);
	bosh.requestCountDecreases();
	final BoshState state = bosh.getState(2000);
	assertTrue(state.shouldWait());
	assertEquals(4000 + options.wait, state.getTime());
    }

    @Test
    public void whenBodyNotEmptyCheckRequestCount() {
	stub(stream.isEmpty()).toReturn(false);
	bosh.setAttributes("theSID", 5, 2);
	assertTrue(bosh.getState(System.currentTimeMillis()).shouldSend());
	bosh.requestCountEncreasesAt(System.currentTimeMillis());
	assertTrue(bosh.getState(System.currentTimeMillis()).shouldSend());
	bosh.requestCountEncreasesAt(System.currentTimeMillis());
	assertTrue(bosh.getState(System.currentTimeMillis()).shouldIgnore());
    }

}
