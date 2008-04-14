package com.calclab.emite.client.core.bosh;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.core.dispatcher.Dispatcher;

public class EmiteTest {

    private EmiteBosh emite;

    @Before
    public void aaCreate() {
	final Stream stream = mock(Stream.class);
	final Dispatcher dispatcher = mock(Dispatcher.class);
	emite = new EmiteBosh(dispatcher, stream);
    }

    @Test
    public void shouldWriteTheTests() {
	assertNotNull(emite);
    }

}
