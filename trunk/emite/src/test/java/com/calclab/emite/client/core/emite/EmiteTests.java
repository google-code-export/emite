package com.calclab.emite.client.core.emite;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import com.calclab.emite.client.core.packet.Packet;

public class EmiteTests {
    private BoshStream stream;

    @Before
    public void aaCreate() {
	stream = new BoshStream();
    }

    @Test
    public void shouldGenerateConsecutiveRIDs() {
	stream.start("domain");
	Packet body = stream.clearBody();
	assertNotNull(body);
	final String att = body.getAttribute("rid");
	assertNotNull(att);
	final long rid = Long.parseLong(att);
	assertTrue(rid > 1000);

	stream.newRequest(null);
	body = stream.clearBody();
	assertEquals(Long.parseLong(body.getAttribute("rid")), rid + 1);

    }

}
