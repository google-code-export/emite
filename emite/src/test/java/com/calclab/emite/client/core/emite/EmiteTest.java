package com.calclab.emite.client.core.emite;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.core.bosh.Stream;
import com.calclab.emite.client.core.packet.Packet;

public class EmiteTest {
    private Stream stream;

    @Before
    public void aaCreate() {
	stream = new Stream();
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

	stream.prepareBody(null);
	body = stream.clearBody();
	assertEquals(Long.parseLong(body.getAttribute("rid")), rid + 1);

    }

}
