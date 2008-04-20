package com.calclab.emite.client.core.bosh;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.core.services.Services;
import com.calclab.emite.j2se.services.TigaseXMLService;
import com.calclab.emite.testing.InstallationTester;
import com.calclab.emite.testing.TestMatchers;
import com.calclab.emite.testing.InstallationTester.InstallTest;
import com.calclab.emite.testing.InstallationTester.InstallVerifier;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static com.calclab.emite.testing.TestMatchers.*;

public class BoshManagerTest {
    private Services services;
    private Emite emite;
    private BoshStream stream;
    private BoshOptions options;
    private BoshManager manager;
    private TigaseXMLService xmler;

    @Before
    public void aaCreate() {
	xmler = new TigaseXMLService();
	services = mock(Services.class);
	emite = mock(Emite.class);
	stream = mock(BoshStream.class);
	options = new BoshOptions("http-bind");
	manager = new BoshManager(services, emite, stream, options);
    }

    @Test
    public void shouldPublishAnyBodyChild() {
	startManager();
	final String body = "<body polling=\"5\"><one/><two/></body>";
	manager.eventBody(xmler.toXML(body));
	verify(emite, atLeastOnce()).publish(packetLike(new Packet("one")));
	verify(emite, atLeastOnce()).publish(packetLike(new Packet("two")));
    }

    @Test
    public void shouldSetSIDWhenFirstBody() {
	startManager();
	final String body = "<body xmlns=\"http://jabber.org/protocol/httpbind\" xmlns:stream=\"http://etherx.jabber.org/streams\" authid=\"505ea252\" "
		+ "sid=\"theSid\" secure=\"true\" requests=\"2\" inactivity=\"30\" polling=\"5\" wait=\"60\" ver=\"1.6\"></body>";
	manager.eventBody(xmler.toXML(body));
	assertEquals("theSid", manager.getState().getSID());
	assertEquals(5500, manager.getState().getPoll());
    }

    @Test
    public void shouldStopWhenBodyTerminate() {
	startManager();
	final String body = "<body xmlns=\"http://jabber.org/protocol/httpbind\" type=\"terminal\" condition=\"policy-violation\"></body>";
	manager.eventBody(xmler.toXML(body));
	assertFalse(manager.isRunning());
	verify(emite).publish(TestMatchers.packetLike(BoshManager.Events.error("terminal", "policy-violation")));

    }

    @Test
    public void testInstallation() {
	new InstallationTester(new InstallTest() {
	    public void prepare(final Emite emite, final InstallVerifier verifier) {
		new BoshManager(services, emite, stream, options).install();
		verifier.shouldAttachTo(new Packet("body"));
	    }
	});
    }

    private void startManager() {
	manager.dispatchingBegins();
	manager.eventStart("domain");
    }

}
