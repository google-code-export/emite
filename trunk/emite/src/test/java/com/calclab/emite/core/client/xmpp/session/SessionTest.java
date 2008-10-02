package com.calclab.emite.core.client.xmpp.session;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.bosh.ConnectionTestHelper;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.Packet;
import com.calclab.emite.core.client.xmpp.resource.ResourceBindingManager;
import com.calclab.emite.core.client.xmpp.sasl.AuthorizationTransaction;
import com.calclab.emite.core.client.xmpp.sasl.SASLManager;
import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.listener.Listener;
import com.calclab.suco.testing.listener.EventTester;
import com.calclab.suco.testing.listener.MockListener;

public class SessionTest {

    private SessionImpl session;
    private SASLManager saslManager;
    private ResourceBindingManager bindingManager;
    private ConnectionTestHelper helper;
    private Connection connection;
    private IMSessionManager iMSessionManager;

    @Before
    public void beforeTest() {
	helper = new ConnectionTestHelper();

	saslManager = mock(SASLManager.class);
	bindingManager = mock(ResourceBindingManager.class);
	iMSessionManager = mock(IMSessionManager.class);
	connection = helper.connection;
	session = new SessionImpl(connection, saslManager, bindingManager, iMSessionManager);

    }

    @Test
    public void shouldConnectOnLogin() {
	session.login(uri("name@domain/resource"), "password");
	verify(helper.connection).connect();
    }

    @Test
    public void shouldEventMessages() {
	final MockListener<Message> listener = new MockListener<Message>();
	session.onMessage(listener);

	final EventTester<IPacket> onStanza = new EventTester<IPacket>();
	verify(helper.connection).onStanzaReceived(argThat(onStanza));
	onStanza.fire(new Packet("message"));
	assertTrue(listener.isCalledOnce());
    }

    @Test
    public void shouldEventPresences() {
	final MockListener<Presence> listener = new MockListener<Presence>();
	session.onPresence(listener);

	final EventTester<IPacket> onStanza = new EventTester<IPacket>();
	verify(helper.connection).onStanzaReceived(argThat(onStanza));
	onStanza.fire(new Packet("presence"));
	assertTrue(listener.isCalledOnce());
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
    public void shouldLoginWhenSessionCreated() {

	final MockListener<State> onStateChanged = new MockListener<State>();
	session.onStateChanged(onStateChanged);

	createSession(uri("name@domain/resource"));
	assertTrue(onStateChanged.isCalledWithEquals(State.loggedIn));
    }

    @Test
    public void shouldQueueOutcomingStanzas() {
	session.send(new Message("the Message", uri("other@domain")));
	verify(connection, never()).send((IPacket) anyObject());
	createSession(uri("name@domain/resource"));
	session.setReady();
	verify(connection).send((IPacket) anyObject());
    }

    @Test
    public void shouldRequestSessionWhenBinded() {
	final EventTester<XmppURI> bindEvent = new EventTester<XmppURI>();
	bindEvent.mock(bindingManager).onBinded(bindEvent.getListener());
	final XmppURI uri = uri("name@domain/resource");
	bindEvent.fire(uri);
	verify(iMSessionManager).requestSession(same(uri));
    }

    @Test
    public void shouldStopAndDisconnectWhenLoggedOut() {
    }

    private void createSession(final XmppURI uri) {
	final EventTester<XmppURI> sessionCreatedEvent = new EventTester<XmppURI>();
	sessionCreatedEvent.mock(iMSessionManager).onSessionCreated(sessionCreatedEvent.getListener());
	sessionCreatedEvent.fire(uri);
    }
}
