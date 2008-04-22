package com.calclab.emite.client.xmpp.stanzas;

import static org.junit.Assert.*;

import org.junit.Test;

import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.xmpp.stanzas.Message.Type;

public class MessageTest {

    @Test
    public void shouldReturnUnkownType() {
	final Message message = new Message(new Packet("message").With("type", "invalid-here"));
	assertSame(Type.unknown, message.getType());
    }

    @Test
    public void shouldTypeNotSpecifiedType() {
	final Message message = new Message(new Packet("message"));
	final Type type = message.getType();
	assertSame(Type.notSpecified, type);
    }
}
