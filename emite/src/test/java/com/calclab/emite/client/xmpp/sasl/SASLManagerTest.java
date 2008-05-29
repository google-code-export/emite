package com.calclab.emite.client.xmpp.sasl;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.testing.EmiteTestHelper;
import com.calclab.emite.testing.ListenerTester;
import static com.calclab.emite.testing.ListenerTester.*;
import static org.junit.Assert.assertSame;

public class SASLManagerTest {

    private EmiteTestHelper emite;
    private SASLManager manager;
    private ListenerTester<AuthorizationTicket> listener;

    @Before
    public void aaCreate() {
	emite = new EmiteTestHelper();
	manager = new SASLManager(emite);
	listener = new ListenerTester<AuthorizationTicket>();
	manager.onAuthorized(listener);
    }

    @Test
    public void shouldHandleFailure() {
	manager.sendAuthorizationRequest(new AuthorizationTicket(uri("node@domain"), "password"));
	emite.receives("<failure xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\"><not-authorized/></failure>");
	verifyCalled(listener);
	assertSame(AuthorizationTicket.State.failed, listener.getValue(0).getState());
    }

    @Test
    public void shouldHandleSuccessWhenAuthorizationSent() {
	manager.sendAuthorizationRequest(new AuthorizationTicket(uri("me@domain"), "password"));
	emite.receives("<success xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\"/>");
	verifyCalled(listener);
	assertSame(AuthorizationTicket.State.succeed, listener.getValue(0).getState());
    }

    @Test
    public void shouldSendAnonymousIfNoUserProvided() {
	manager.sendAuthorizationRequest(new AuthorizationTicket(uri("domain/resource"), null));
	emite.verifySent(new Packet("auth", "urn:ietf:params:xml:ns:xmpp-sasl").With("mechanism", "ANONYMOUS"));
    }

    @Test
    public void shouldSendPlainAuthorization() {
	manager.sendAuthorizationRequest(new AuthorizationTicket(uri("node@domain/resource"), "password"));
	emite.verifySent(new Packet("auth", "urn:ietf:params:xml:ns:xmpp-sasl").With("mechanism", "PLAIN"));
    }
}
