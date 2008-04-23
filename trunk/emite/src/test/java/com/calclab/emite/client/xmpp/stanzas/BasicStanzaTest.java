package com.calclab.emite.client.xmpp.stanzas;

import static org.junit.Assert.*;

import org.junit.Test;

public class BasicStanzaTest {
    @Test
    public void shouldSetTo() {
	final BasicStanza stanza = new BasicStanza("name", "xmlns");
	stanza.setTo("value");
	assertEquals("value", stanza.getTo());
	stanza.setTo(null);
	assertNull(stanza.getTo());
    }
}
