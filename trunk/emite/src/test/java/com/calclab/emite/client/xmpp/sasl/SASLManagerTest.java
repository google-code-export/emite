package com.calclab.emite.client.xmpp.sasl;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.xmpp.session.SessionManager;
import com.calclab.emite.testing.EmiteStub;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.*;

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
	emite.receives("<failure xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\"><not-authorized/></failure>");
	emite.verifyPublished(SessionManager.Events.onAuthorizationFailed);
    }

    @Test
    public void shouldHandleSuccessWhenAuthorizationSent() {
	emite.receives(SessionManager.Events.login(uri("name@domain/res"), "password"));
	emite.receives(SessionManager.Events.onDoAuthorization);
	emite.receives("<success xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\"/>");
	emite.verifyPublished(SessionManager.Events.onAuthorized);
    }

    @Test
    public void shouldSendAnonymousIfNoUserProvided() {
	emite.receives(SessionManager.Events.login(uri("domain/resource"), null));
	emite.receives(SessionManager.Events.onDoAuthorization);
	emite.verifySent(new Packet("auth", "urn:ietf:params:xml:ns:xmpp-sasl").With("mechanism", "ANONYMOUS"));
    }
}
