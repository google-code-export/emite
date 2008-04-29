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
    private BoshManager manager;
    private BoshState state;

    @Before
    public void aaCreate() {
	services = mock(Services.class);
	emite = new EmiteStub();
	stream = mock(BoshStream.class);
	state = mock(BoshState.class);
	manager = new BoshManager(services, emite, stream, state);
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
	stub(state.isFirstResponse()).toReturn(true);
	emite
		.receives("<body xmlns='http://jabber.org/protocol/httpbind' xmlns:stream='http://etherx.jabber.org/streams' "
			+ "authid='505ea252' sid='theSid' secure='true' requests='2' inactivity='30' polling='5' wait='60' ver='1.6'></body>");
	verify(state).setSID("theSid");
	verify(state).setPoll(5000 + BoshManager.POLL_SECURITY);
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
