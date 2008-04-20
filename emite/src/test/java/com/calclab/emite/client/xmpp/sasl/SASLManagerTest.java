package com.calclab.emite.client.xmpp.sasl;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.core.bosh.BoshManager;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.xmpp.session.SessionManager;
import com.calclab.emite.j2se.services.TigaseXMLService;
import com.calclab.emite.testing.TestMatchers;

public class SASLManagerTest {

    private Emite emite;
    private SASLManager manager;
    private TigaseXMLService xmler;

    @Before
    public void aaCreate() {
	xmler = new TigaseXMLService();
	emite = mock(Emite.class);
	manager = new SASLManager(emite);
    }

    @Test
    public void shouldHandleFailure() {
	final String received = "<failure xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\"><not-authorized/></failure>";
	manager.eventFailure(xmler.toXML(received));
	verify(emite).publish(TestMatchers.packetLike(BoshManager.Events.error("sasl-error", "not-authorized")));
    }

    @Test
    public void shouldHandleFeatures() {
	// TODO
	assertTrue(true);
    }

    @Test
    public void shouldHandleSuccess() {
	final String received = "<success xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\"/>";
	manager.eventSuccess(xmler.toXML(received));
	verify(emite).publish(SessionManager.Events.authorized);
    }

}
