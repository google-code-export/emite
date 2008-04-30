package com.calclab.emite.client.core.bosh;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.core.services.Services;
import com.calclab.emite.testing.EmiteStub;

public class BoshManagerTest {
    private Services services;
    private EmiteStub emite;
    private BoshManager manager;
    private Bosh bosh;

    @Before
    public void aaCreate() {
	services = mock(Services.class);
	emite = new EmiteStub();
	bosh = mock(Bosh.class);
	manager = new BoshManager(services, emite, bosh);
	manager.install();
    }

    @Test
    public void shouldDispatchIncommingStanzas() {
	manager.setRunning(true);
	stub(services.toXML("<body />")).toReturn(new Packet("body"));
	manager.onResponseReceived(200, "<body />");
	verify(bosh).requestCountDecreases();
	emite.verifyPublished("<body />");
    }

    @Test
    public void shouldHanldeRestart() {
	manager.setDomain("domain");
	emite.receives(BoshManager.Events.onRestart);
	verify(bosh).setRestart("domain");
    }

    @Test
    public void shouldPrepareBodyWhenDispatchingBegins() {
	manager.setRunning(true);
	manager.dispatchingBegins();
	verify(bosh).prepareBody();
    }

    @Test
    public void shouldPublishAnyBodyChild() {
	emite.receives("<body polling='5'><one/><two/></body>");
	emite.verifyPublished(new Packet("two"));
	emite.verifyPublished(new Packet("one"));
    }

    @Test
    public void shouldSetSIDWhenFirstBody() {
	stub(bosh.isFirstResponse()).toReturn(true);
	emite.receives("<body xmlns='http://jabber.org/protocol/httpbind' "
		+ "xmlns:stream='http://etherx.jabber.org/streams' "
		+ "authid='505ea252' sid='theSid' secure='true' requests='2' inactivity='30' "
		+ "polling='5' wait='60' ver='1.6'></body>");
	verify(bosh).setAttributes("theSid", 5, 2);
    }

    @Test
    public void shouldStopWhenBodyTerminate() {
	emite.receives("<body xmlns='http://jabber.org/protocol/httpbind' type='terminal' "
		+ "condition='policy-violation'></body>");
	assertFalse(manager.isRunning());
	emite.verifyPublished(BoshManager.Events.error("terminal", "policy-violation"));
    }

}
