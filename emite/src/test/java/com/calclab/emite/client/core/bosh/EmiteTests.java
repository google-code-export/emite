package com.calclab.emite.client.core.bosh;

import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.core.services.XMLService;

public class EmiteTests {

    private Emite emite;

    @Before
    public void aaCreate() {
	final Dispatcher dispatcher = mock(Dispatcher.class);
	final XMLService xmler = mock(XMLService.class);
	emite = new EmiteBosh(dispatcher, xmler);
    }

    @Test
    public void shouldSend() {
	emite.send(new Packet("hola"));
    }
}
