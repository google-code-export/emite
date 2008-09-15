package com.calclab.emite.core.client.xmpp.session;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.bosh.ConnectionTestHelper;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.Packet;
import com.calclab.emite.core.client.xmpp.resource.ResourceBindingManager;
import com.calclab.emite.core.client.xmpp.sasl.AuthorizationTransaction;
import com.calclab.emite.core.client.xmpp.sasl.SASLManager;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.SessionScope;
import com.calclab.emite.core.client.xmpp.session.XmppSession;
import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.IQ.Type;
import com.calclab.suco.client.listener.Listener;
import com.calclab.suco.testing.listener.MockListener;
import com.calclab.suco.testing.listener.EventTester;

public class SessionTest {

    private XmppSession session;
    private SessionScope scope;
    private SASLManager saslManager;
    private ResourceBindingManager bindingManager;
    private EventTester<XmppURI> bindEvent;
    private ConnectionTestHelper helper;

    @Before
    public void beforeTest() {
	helper = new ConnectionTestHelper();
	scope = mock(SessionScope.class);

	saslManager = mock(SASLManager.class);
	bindingManager = mock(ResourceBindingManager.class);
	session = new XmppSession(helper.connection, scope, saslManager, bindingManager);

	bindEvent = new EventTester<XmppURI>();
	bindEvent.mock(bindingManager).onBinded(bindEvent.getListener());
    }

    @Test
    public void shouldConnectOnLogin() {
	session.login(uri("name@domain/resource"), "password");
	verify(helper.connection).connect();
    }

    @Test
    public void shouldHandleFailedAuthorizationResult() {
	final EventTester<AuthorizationTransaction> onAuthorized = new EventTester<AuthorizationTransaction>();
	verify(saslManager).onAuthorized(argThat(onAuthorized));
	onAuthorized.fire(new AuthorizationTransaction(uri("node@domain"), "password",
		AuthorizationTransaction.State.failed));
	verify(helper.connection).disconnect();
    }

    @Test
    public void shouldHandleSucceedAuthorizationResult() {
	final EventTester<AuthorizationTransaction> onAuthorized = new EventTester<AuthorizationTransaction>();
	verify(saslManager).onAuthorized(argThat(onAuthorized));
	onAuthorized.fire(new AuthorizationTransaction(uri("node@domain/resource"), "password",
		AuthorizationTransaction.State.succeed));

	assertEquals(Session.State.authorized, session.getState());
	verify(helper.connection).restartStream();
	verify(bindingManager).bindResource(anyString());
    }

    @Test
    public void shouldRequestSessionWhenBinded() {
	bindEvent.fire(uri("name@domain/resource"));
	helper.verifySentLike(new IQ(IQ.Type.set).With("id", "session_1"));
	helper.simulateReception(new IQ(Type.result).With("id", "session_1"));
	assertEquals(State.ready, session.getState());

    }

    @Test
    public void shouldEventMessages() {
	final MockListener<Message> listener = new MockListener<Message>();
	session.onMessage(listener);

	final EventTester<IPacket> onStanza = new EventTester<IPacket>();
	verify(helper.connection).onStanzaReceived(argThat(onStanza));
	onStanza.fire(new Packet("message"));
	MockListener.verifyCalled(listener);
    }

    @Test
    public void shouldEventPresences() {
	final MockListener<Presence> listener = new MockListener<Presence>();
	session.onPresence(listener);

	final EventTester<IPacket> onStanza = new EventTester<IPacket>();
	verify(helper.connection).onStanzaReceived(argThat(onStanza));
	onStanza.fire(new Packet("presence"));
	MockListener.verifyCalled(listener);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldEventStateChanges() {
	final Listener<Session.State> listener = mock(Listener.class);
	session.onStateChanged(listener);
	session.setState(Session.State.ready);
	verify(listener).onEvent(same(Session.State.ready));
    }

    @Test
    public void shouldStartScopeOnLogin() {
	final XmppURI uri = uri("name@domain/resource");
	session.login(uri, "password");
	verify(scope).createAll();
    }

    @Test
    public void shouldStopAndDisconnectWhenLoggedOut() {
    }
}
