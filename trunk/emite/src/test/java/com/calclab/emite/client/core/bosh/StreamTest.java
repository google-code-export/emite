package com.calclab.emite.client.core.bosh;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.core.packet.Packet;

public class StreamTest {

    private Stream stream;

    @Before
    public void beforeTest() {
	stream = new Stream();
    }

    @Test
    public void shouldInitCorrectly() {
	stream.start("the domain");
	final Packet body = stream.getBody();
	assertEquals("the domain", body.getAttribute("to"));
	assertEquals("1.0", body.getAttribute("xmpp:version"));
	assertEquals("urn:xmpp:xbosh", body.getAttribute("xmlns:xmpp"));
	assertEquals("http://jabber.org/protocol/httpbind", body.getAttribute("xmlns"));
    }
}
