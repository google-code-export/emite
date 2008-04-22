package com.calclab.emite.client.xmpp.stanzas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.xmpp.stanzas.Presence.Show;

public class PresenceTest {

    @Test
    public void shouldGetShow() {
	Presence presence = new Presence(new Packet("presence"));
	assertSame(Show.notSpecified, presence.getShow());
	presence = new Presence(new Packet("presence").With(new Packet("show").WithText(Show.available.toString())));
	assertSame(Show.available, presence.getShow());
	presence = new Presence(new Packet("presence").With(new Packet("show").WithText("not valid show")));
	assertSame(Show.unknown, presence.getShow());
    }

    @Test
    public void shouldGetStatus() {
	Presence presence = new Presence(new Packet("presence"));
	assertNull(presence.getStatus());
	presence = new Presence(new Packet("presence").With(new Packet("status").WithText("the status")));
	assertEquals("the status", presence.getStatus());
    }

    @Test
    public void shouldReturnPriority() {
	final Presence presence = new Presence(new Packet("presence"));
	assertEquals(0, presence.getPriority());
    }

    @Test
    public void shouldSetShow() {
	final Presence presence = new Presence(new Packet("presence"));
	for (final Show value : Show.values()) {
	    presence.setShow(value);
	    assertSame(value, presence.getShow());
	}
    }

}
