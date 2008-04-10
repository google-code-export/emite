package com.calclab.emite.client.core.bosh;

import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.emite.Emite;
import com.calclab.emite.client.core.emite.EmiteBosh;
import com.calclab.emite.client.core.packet.Packet;

public class EmiteTests {

    private Emite emite;

    @Before
    public void aaCreate() {
	final Dispatcher dispatcher = mock(Dispatcher.class);
	emite = new EmiteBosh(dispatcher);
    }

    @Test
    public void shouldSend() {
	emite.send(new Packet("hola"));
    }
}
