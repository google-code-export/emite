package com.calclab.emite.client.xmpp.stanzas;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.xmpp.stanzas.Message.Type;

public class MessageTest {
    @Test
    public void shouldAddSubject() {
	final Message message = new Message(uri("user1@domain/r"), uri("user2@domain/r"), "message")
		.Subject("the subject");
	assertEquals("the subject", message.getSubject());
    }

    @Test
    public void shouldRetrieveSubject() {
	final Message message = new Message(new Packet("message").With(new Packet("subject", null)
		.WithText("the subject")));
	assertEquals("the subject", message.getSubject());
    }

    @Test
    public void shouldReturnUnkownType() {
	final Message message = new Message(new Packet("message").With("type", "invalid-here"));
	assertSame(Type.normal, message.getType());
    }

    @Test
    public void shouldTypeNotSpecifiedType() {
	final Message message = new Message(new Packet("message"));
	final Type type = message.getType();
	assertSame(Type.normal, type);
    }
}
