package com.calclab.emite.client.xmpp.session;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;
import static com.calclab.emite.testing.MockSlot.verifyCalled;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.bosh3.ConnectionTestHelper;
import com.calclab.emite.client.core.bosh3.Bosh3Settings;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.xmpp.resource.ResourceBindingManager;
import com.calclab.emite.client.xmpp.sasl.AuthorizationTransaction;
import com.calclab.emite.client.xmpp.sasl.SASLManager;
import com.calclab.emite.client.xmpp.session.Session.State;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.testing.MockSlot;
import com.calclab.emite.testing.SignalTester;
import com.calclab.suco.client.signal.Slot;

public class SessionTest {

    private XmppSession session;
    private SessionScope scope;
    private SASLManager saslManager;
    private ResourceBindingManager bindingManager;
    private SignalTester<XmppURI> bindSignal;
    private ConnectionTestHelper helper;

    @Before
    public void beforeTest() {
	helper = new ConnectionTestHelper();
	scope = mock(SessionScope.class);

	saslManager = mock(SASLManager.class);

	bindingManager = mock(ResourceBindingManager.class);

	session = new XmppSession(helper.connection, scope, saslManager, bindingManager);

	bindSignal = new SignalTester<XmppURI>();
	bindSignal.attachTo(bindingManager).onBinded(bindSignal.asSlot());
    }

    @Test
    public void shouldConnectOnLogin() {
	final XmppURI uri = uri("name@domain/resource");
	final Bosh3Settings settings = new Bosh3Settings("base", "host");
	session.login(uri, "password", settings);
	verify(helper.connection).connect(same(settings));
    }

    @Test
    public void shouldHandleFailedAuthorizationResult() {
	final SignalTester<AuthorizationTransaction> onAuthorized = new SignalTester<AuthorizationTransaction>();
	verify(saslManager).onAuthorized(argThat(onAuthorized));
	onAuthorized.fire(new AuthorizationTransaction(uri("node@domain"), "password",
		AuthorizationTransaction.State.failed));
	verify(helper.connection).disconnect();
    }

    @Test
    public void shouldHandleSucceedAuthorizationResult() {
	final SignalTester<AuthorizationTransaction> onAuthorized = new SignalTester<AuthorizationTransaction>();
	verify(saslManager).onAuthorized(argThat(onAuthorized));
	onAuthorized.fire(new AuthorizationTransaction(uri("node@domain/resource"), "password",
		AuthorizationTransaction.State.succeed));

	assertEquals(Session.State.authorized, session.getState());
	verify(helper.connection).restartStream();
	verify(bindingManager).bindResource(anyString());
    }

    @Test
    public void shouldRequestSessionWhenBinded() {
	bindSignal.fire(uri("name@domain/resource"));
	helper.verifySentLike(new IQ(IQ.Type.set).With("id", "session_1"));
	helper.simulateReception(new IQ(Type.result).With("id", "session_1"));
	assertEquals(State.ready, session.getState());

    }

    @Test
    public void shouldSignalMessages() {
	final MockSlot<Message> listener = new MockSlot<Message>();
	session.onMessage(listener);

	final SignalTester<IPacket> onStanza = new SignalTester<IPacket>();
	verify(helper.connection).onStanzaReceived(argThat(onStanza));
	onStanza.fire(new Packet("message"));
	verifyCalled(listener);
    }

    @Test
    public void shouldSignalPresences() {
	final MockSlot<Presence> listener = new MockSlot<Presence>();
	session.onPresence(listener);

	final SignalTester<IPacket> onStanza = new SignalTester<IPacket>();
	verify(helper.connection).onStanzaReceived(argThat(onStanza));
	onStanza.fire(new Packet("presence"));
	verifyCalled(listener);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldSignalStateChanges() {
	final Slot<Session.State> listener = mock(Slot.class);
	session.onStateChanged(listener);
	session.setState(Session.State.ready);
	verify(listener).onEvent(same(Session.State.ready));
    }

    @Test
    public void shouldStartScopeOnLogin() {
	final XmppURI uri = uri("name@domain/resource");
	final Bosh3Settings settings = new Bosh3Settings("base", "host");
	session.login(uri, "password", settings);
	verify(scope).createAll();
    }

    @Test
    public void shouldStopAndDisconnectWhenLoggedOut() {
    }
}
