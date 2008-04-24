package com.calclab.emite.client.xmpp.stanzas;

import static org.junit.Assert.*;

import org.junit.Test;
import static com.calclab.emite.client.xmpp.stanzas.XmppURI.*;

public class BasicStanzaTest {
    @Test
    public void shouldSetTo() {
	final BasicStanza stanza = new BasicStanza("name", "xmlns");

	stanza.setTo(uri("name@domain/resource"));
	assertEquals("name@domain/resource", stanza.getTo());
	stanza.setTo((XmppURI) null);
	assertNull(stanza.getTo());
    }
}
