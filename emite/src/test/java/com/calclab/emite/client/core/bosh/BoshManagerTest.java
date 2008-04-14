package com.calclab.emite.client.core.bosh;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.components.DefaultContainer;
import com.calclab.emite.client.core.services.ConnectorException;
import com.calclab.emite.client.core.services.ServicesPlugin;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public class BoshManagerTest {

    private MockedServer server;
    private Xmpp xmpp;

    @Before
    public void aaCreate() {
	final Container container = new DefaultContainer();
	final BoshOptions options = new BoshOptions("httpbind");
	server = new MockedServer();
	ServicesPlugin.install(container, server);
	xmpp = new Xmpp(container, options, server.getDispatcherMonitor());
    }

    @Test
    public void testStart() throws ConnectorException {
	xmpp.login(XmppURI.parse("user@domain/resource"), "password", null, null);
	assertEquals(1, server.getRequestCount());
	server
		.answer("<body xmlns=\"http://jabber.org/protocol/httpbind\" xmlns:stream=\"http://etherx.jabber.org/streams\" authid=\"505ea252\" "
			+ "sid=\"505ea252\" secure=\"true\" requests=\"2\" inactivity=\"30\" polling=\"5\" wait=\"60\" "
			+ "ver=\"1.6\"><stream:features><mechanisms xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\">"
			+ "<mechanism>PLAIN</mechanism><mechanism>CRAM-MD5</mechanism><mechanism>ANONYMOUS</mechanism>"
			+ "<mechanism>DIGEST-MD5</mechanism></mechanisms><compression xmlns=\"http://jabber.org/features/compress\">"
			+ "<method>zlib</method></compression><bind xmlns=\"urn:ietf:params:xml:ns:xmpp-bind\"/>"
			+ "<session xmlns=\"urn:ietf:params:xml:ns:xmpp-session\"/></stream:features></body>");

	assertEquals(2, server.getRequestCount());
    }
}
