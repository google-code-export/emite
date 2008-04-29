package com.calclab.emite.client.core.bosh;

import static org.mockito.Mockito.*;

public class BoshTest {

    private Stream stream;
    private Bosh bosh;

    public void aaCreate() {
	stream = mock(Stream.class);
	final BoshOptions options = new BoshOptions("domain");
	bosh = new Bosh(stream, options);
    }

    public void shouldPrepareBody() {
	bosh.prepareBody();
	verify(stream).prepareBody(null);
    }

    public void shouldRestartStream() {
	bosh.setRestart("domain");
	verify(stream).setRestart("domain");
    }
}
