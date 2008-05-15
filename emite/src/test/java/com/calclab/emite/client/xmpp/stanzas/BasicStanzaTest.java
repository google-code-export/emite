package com.calclab.emite.client.xmpp.stanzas;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

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
