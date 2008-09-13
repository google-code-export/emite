package com.calclab.emite.client.xmpp.sasl;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.core.bosh.ConnectionTestHelper;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.suco.testing.signal.MockSlot;

public class SASLManagerTest {
    private SASLManager manager;
    private MockSlot<AuthorizationTransaction> listener;
    private ConnectionTestHelper helper;

    @Before
    public void beforeTests() {
	helper = new ConnectionTestHelper();
	manager = new SASLManager(helper.getConnection());
	listener = new MockSlot<AuthorizationTransaction>();
	manager.onAuthorized(listener);
    }

    @Test
    public void shouldHandleSuccessWhenAuthorizationSent() {
	manager.sendAuthorizationRequest(new AuthorizationTransaction(uri("me@domain"), "password"));
	helper.simulateReception("<success xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\"/>");
	MockSlot.verifyCalled(listener);
	assertSame(AuthorizationTransaction.State.succeed, listener.getValue(0).getState());
    }

    @Test
    public void shouldHanonStanzadleFailure() {
	manager.sendAuthorizationRequest(new AuthorizationTransaction(uri("node@domain"), "password"));
	helper.simulateReception("<failure xmlns=\"urn:ietf:params:xml:ns:xmpp-sasl\"><not-authorized/></failure>");
	MockSlot.verifyCalled(listener);
	assertSame(AuthorizationTransaction.State.failed, listener.getValue(0).getState());
    }

    @Test
    public void shouldSendAnonymousIfAnonymousProvided() {
	manager.sendAuthorizationRequest(new AuthorizationTransaction(SASLManager.ANONYMOUS, null));
	helper.verifySentLike(new Packet("auth", "urn:ietf:params:xml:ns:xmpp-sasl").With("mechanism", "ANONYMOUS"));
    }

    @Test
    public void shouldSendPlainAuthorizationUnlessAnonymous() {
	manager.sendAuthorizationRequest(new AuthorizationTransaction(uri("node@domain/resource"), "password"));
	helper.verifySentLike(new Packet("auth", "urn:ietf:params:xml:ns:xmpp-sasl").With("mechanism", "PLAIN"));
    }

    @Test
    public void shouldSendPlainAuthorizationWithoutNode() {
	manager.sendAuthorizationRequest(new AuthorizationTransaction(uri("domain/resource"), null));
	helper.verifySentLike(new Packet("auth", "urn:ietf:params:xml:ns:xmpp-sasl").With("mechanism", "PLAIN"));
    }

}
