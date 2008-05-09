package com.calclab.emite.client.core.bosh;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.Dispatcher.Events;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.core.services.ConnectorCallback;
import com.calclab.emite.client.core.services.ConnectorException;
import com.calclab.emite.client.core.services.ScheduledAction;
import com.calclab.emite.client.core.services.Services;
import com.calclab.emite.testing.EmiteTestHelper;

public class BoshManagerTest {
    private Services services;
    private EmiteTestHelper emite;
    private BoshManager manager;
    private Bosh bosh;

    @Before
    public void aaCreate() {
	services = mock(Services.class);
	emite = new EmiteTestHelper();
	bosh = mock(Bosh.class);
	manager = new BoshManager(services, emite, bosh);
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
    public void shouldHandleStreamErrors() {
	emite.receives("<stream:error />");
	emite.verifyPublished(Dispatcher.Events.onError);
    }

    @Test
    public void shouldHanldeRestart() {
	emite.receives(BoshManager.Events.onRestartStream);
	verify(bosh).setRestart();
    }

    @Test
    public void shouldPrepareBodyBeforeDispatching() {
	manager.setRunning(true);
	manager.dispatchingBegins();
	verify(bosh).prepareBody();
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
    public void shouldPublishErrorIfServicesFails() throws ConnectorException {
	manager.setRunning(true);
	stub(bosh.getState(anyLong())).toReturn(Bosh.SEND);
	stub(services.toString((IPacket) anyObject())).toThrow(new RuntimeException("the message"));
	manager.dispatchingEnds();
	emite.verifyPublished(Dispatcher.Events.onError);
    }

    @Test
    public void shouldPullAfterDispatching() {
	manager.setRunning(true);
	stub(bosh.getState(anyLong())).toReturn(Bosh.shouldWait(10000));
	manager.dispatchingEnds();

	verify(services).schedule(eq(10000), (ScheduledAction) anyObject());
    }

    @Test
    public void shouldSendResponseAfterDispatching() throws ConnectorException {
	manager.setRunning(true);
	stub(bosh.getState(anyLong())).toReturn(Bosh.SEND);
	stub(bosh.getHttpBase()).toReturn("the http-base");
	stub(services.toString((IPacket) anyObject())).toReturn("the response");

	manager.dispatchingEnds();
	verify(services).send(eq("the http-base"), eq("the response"), (ConnectorCallback) anyObject());
    }

    @Test
    public void shouldSendTerminatingWhenStop() {
	emite.receives(BoshManager.Events.stop);
	verify(bosh).setTerminating(true);
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
    public void shouldStart() {
	final String domain = "this-is-the-domain";
	emite.receives(BoshManager.Events.start(domain));
	assertTrue(manager.isRunning());
	assertEquals(domain, manager.getDomain());
	verify(bosh).init(anyLong(), eq(domain));
    }

    @Test
    public void shouldStopOnError() {
	emite.receives(Dispatcher.Events.error("exception", "some exception info"));
	assertFalse(manager.isRunning());
    }

    @Test
    public void shouldStopWhenBodyTerminate() {
	emite.receives("<body xmlns='http://jabber.org/protocol/httpbind' type='terminal' "
		+ "condition='policy-violation'></body>");
	assertFalse(manager.isRunning());
	emite.verifyPublished(Events.error("terminal", "policy-violation"));
    }
}
