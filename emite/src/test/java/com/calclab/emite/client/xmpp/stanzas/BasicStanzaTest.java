package com.calclab.emite.client.xmpp.stanzas;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.calclab.emite.client.core.packet.NoPacket;

public class BasicStanzaTest {
    @Test
    public void shouldSetTextToChild() {
	final BasicStanza stanza = new BasicStanza("name", "xmlns");
	stanza.setTextToChild("child", "value");
	assertEquals("value", stanza.getFirstChild("child").getText());
	stanza.setTextToChild("child", null);
	assertSame(NoPacket.INSTANCE, stanza.getFirstChild("child"));
    }

    @Test
    public void shouldSetTo() {
	final BasicStanza stanza = new BasicStanza("name", "xmlns");

	stanza.setTo(uri("name@domain/resource"));
	assertEquals("name@domain/resource", stanza.getTo());
	stanza.setTo((XmppURI) null);
	assertNull(stanza.getTo());
    }
}
