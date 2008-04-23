package com.calclab.emite.client.xmpp.stanzas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.client.xmpp.stanzas.Presence.Type;

public class PresenceTest {

    @Test
    public void shouldGetPriority() {
	Presence presence = new Presence(new Packet("presence"));
	assertSame(0, presence.getPriority());
	presence = new Presence(new Packet("presence").With(new Packet("priority").WithText("5")));
	assertSame(5, presence.getPriority());
	presence = new Presence(new Packet("presence").With(new Packet("priority").WithText("not valid")));
	assertSame(0, presence.getPriority());
    }

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
    public void shouldGetType() {
	Presence presence = new Presence(new Packet("presence"));
	assertEquals(Type.available, presence.getType());
	presence = new Presence(new Packet("presence").With("type", Type.probe.toString()));
	assertEquals(Type.probe, presence.getType());
	presence = new Presence(new Packet("presence").With("type", "not valid"));
	assertEquals(Type.error, presence.getType());
    }

    @Test
    public void shouldSetPriority() {
	final Presence presence = new Presence(new Packet("presence"));
	presence.setPriority(1);
	assertEquals(1, presence.getPriority());
    }

    @Test
    public void shouldSetShow() {
	final Presence presence = new Presence(new Packet("presence"));
	for (final Show value : Show.values()) {
	    presence.setShow(value);
	    assertSame(value, presence.getShow());
	}
    }

    @Test
    public void shouldSetStatus() {
	final Presence presence = new Presence(new Packet("presence"));
	presence.setStatus("the status");
	assertEquals("the status", presence.getStatus());
	presence.setStatus("the status2");
	assertEquals("the status2", presence.getStatus());
    }

    @Test
    public void shouldSetType() {
	final Presence presence = new Presence(new Packet("presence"));
	for (final Type type : Type.values()) {
	    presence.setType(type.toString());
	    assertSame(type, presence.getType());
	}
    }

}
