package com.calclab.emite.client.core.emite;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.Packet;

public class EmiteTests {

    private EmiteBosh emite;
    private Dispatcher dispathcer;

    @Before
    public void aaCreate() {
	dispathcer = mock(Dispatcher.class);
	emite = new EmiteBosh(dispathcer);
    }

    @Test
    public void shouldDispatchSend() {
	emite.send(new Packet("packet"));
	verify(dispathcer).publish((IPacket) anyObject());
    }

    @Test
    public void shouldGenerateRID() {
	emite.start("domain");
	assertNotNull(emite.getBody());
	final String att = emite.getBody().getAttribute("rid");
	assertNotNull(att);
	final long rid = Long.parseLong(att);
	assertTrue(rid > 1000);
	emite.clearBody();
	assertNull(emite.getBody());
	emite.newRequest(null);
	assertNotNull(emite.getBody());
	assertEquals(Long.parseLong(emite.getBody().getAttribute("rid")), rid + 1);

    }

}
