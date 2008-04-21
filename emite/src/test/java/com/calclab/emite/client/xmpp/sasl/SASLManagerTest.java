package com.calclab.emite.client.xmpp.sasl;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.core.bosh.BoshManager;
import com.calclab.emite.client.xmpp.session.SessionManager;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.testing.EmiteStub;

public class SASLManagerTest {

    private EmiteStub emite;
    private SASLManager manager;

    @Before
    public void aaCreate() {
	emite = new EmiteStub();
	manager = new SASLManager(emite);
	manager.install();
    }

    @Test
    public void shouldHandleFailure() {
	emite.simulate("<failure xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\"><not-authorized/></failure>");
	emite.verifyPublished(BoshManager.Events.error("sasl-failure", "not-authorized"));
    }

    @Test
    public void shouldHandleSuccessWhenAuthorizationSent() {
	emite.simulate(SessionManager.Events.login(XmppURI.parse("name@domain/res"), "password"));
	emite.simulate(SessionManager.Events.onDoAuthorization);
	emite.simulate("<success xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\"/>");
	emite.verifyPublished(SessionManager.Events.onAuthorized);
    }
}
