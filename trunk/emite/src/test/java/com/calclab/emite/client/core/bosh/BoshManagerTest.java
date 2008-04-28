package com.calclab.emite.client.core.bosh;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.core.services.Services;
import com.calclab.emite.testing.EmiteStub;

public class BoshManagerTest {
    private static final String DOMAIN = "domain";
    private Services services;
    private EmiteStub emite;
    private BoshStream stream;
    private BoshOptions options;
    private BoshManager manager;

    @Before
    public void aaCreate() {
	services = mock(Services.class);
	emite = new EmiteStub();
	stream = mock(BoshStream.class);
	options = new BoshOptions("http-bind");
	manager = new BoshManager(services, emite, stream, options);
	manager.install();
    }

    @Test
    public void shouldDispatchIncommingStanzas() {
	startManager();
	stub(services.toXML("<body />")).toReturn(new Packet("body"));
	manager.onResponseReceived(200, "<body />");
	emite.verifyPublished("<body />");
    }

    @Test
    public void shouldHanldeRestart() {
	startManager();
	emite.receives(BoshManager.Events.restart(DOMAIN));
	verify(stream).setRestart(DOMAIN);
    }

    @Test
    public void shouldPrepareBodyWhenDispatchingBegins() {
	startManager();
	manager.dispatchingBegins();
	verify(stream).prepareBody(null);
    }

    @Test
    public void shouldPublishAnyBodyChild() {
	startManager();
	emite.receives("<body polling='5'><one/><two/></body>");
	emite.verifyPublished(new Packet("two"));
	emite.verifyPublished(new Packet("one"));
    }

    @Test
    public void shouldSetSIDWhenFirstBody() {
	startManager();
	emite
		.receives("<body xmlns='http://jabber.org/protocol/httpbind' xmlns:stream='http://etherx.jabber.org/streams' "
			+ "authid='505ea252' sid='theSid' secure='true' requests='2' inactivity='30' polling='5' wait='60' ver='1.6'></body>");
	assertEquals("theSid", manager.getState().getSID());
	assertEquals(5000 + BoshManager.POLL_SECURITY, manager.getState().getPoll());
    }

    @Test
    public void shouldStopWhenBodyTerminate() {
	startManager();
	emite
		.receives("<body xmlns='http://jabber.org/protocol/httpbind' type='terminal' condition='policy-violation'></body>");
	assertFalse(manager.isRunning());
	emite.verifyPublished(BoshManager.Events.error("terminal", "policy-violation"));
    }

    private void startManager() {
	manager.dispatchingBegins();
	manager.eventStart(DOMAIN);
    }

}
