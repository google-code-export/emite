package com.calclab.emite.client.xmpp.session;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.core.bosh.BoshManager;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.Dispatcher.Events;
import com.calclab.emite.client.xmpp.resource.ResourceBindingManager;
import com.calclab.emite.client.xmpp.sasl.AuthorizationTicket;
import com.calclab.emite.client.xmpp.sasl.SASLManager;
import com.calclab.emite.client.xmpp.session.Session.State;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.testing.EmiteTestHelper;
import com.calclab.emite.testing.SignalTester;

public class SessionManagerTest {

    private EmiteTestHelper emite;
    private Session session;
    private SASLManager saslManager;
    private ResourceBindingManager bindingManager;

    @Before
    public void aaCreate() {
	emite = new EmiteTestHelper();
	session = mock(Session.class);
	saslManager = mock(SASLManager.class);
	bindingManager = mock(ResourceBindingManager.class);
	new SessionManager(session, emite, saslManager, bindingManager);
    }

    @Test
    public void shouldHandleFailedAuthorizationResult() {
	final SignalTester<AuthorizationTicket> onAuthorized = new SignalTester<AuthorizationTicket>();
	verify(saslManager).onAuthorized(argThat(onAuthorized));
	onAuthorized.fire(new AuthorizationTicket(uri("node@domain"), "password", AuthorizationTicket.State.failed));
	emite.verifyPublished(Dispatcher.Events.onError);
	verify(session).setState(State.notAuthorized);
    }

    @Test
    public void shouldHandleSucceedAuthorizationResult() {
	final SignalTester<AuthorizationTicket> onAuthorized = new SignalTester<AuthorizationTicket>();
	verify(saslManager).onAuthorized(argThat(onAuthorized));
	onAuthorized.fire(new AuthorizationTicket(uri("node@domain/resource"), "password",
		AuthorizationTicket.State.succeed));

	emite.verifyPublished(BoshManager.Events.onRestartStream);
	verify(bindingManager).bindResource(anyString());
    }

    @Test
    public void shouldRequestSessionWhenBinded() {
	final SignalTester<XmppURI> onBinded = new SignalTester<XmppURI>();
	verify(bindingManager).onBinded(argThat(onBinded));
	final XmppURI uri = uri("name@domain/resource");
	onBinded.fire(uri);

	emite.verifyIQSent(new IQ(Type.set));
	emite.answerSuccess();
	emite.verifyPublished(SessionManager.Events.loggedIn("name@domain/resource"));
	verify(session).setState(State.loggedIn);
	emite.verifyPublished(SessionManager.Events.onLoggedIn);
	emite.verifyPublished(SessionManager.Events.ready);
    }

    @Test
    public void shouldSetSessionReadyWhenEvent() {
	emite.receives(SessionManager.Events.ready);
	verify(session).setState(State.ready);
    }

    @Test
    public void shouldSetStatesWhenError() {
	emite.receives(Events.error("cause", "info"));
	verify(session).setState(State.error);
	verify(session).setState(State.disconnected);
    }

    @Test
    public void shouldStopAndDisconnectWhenLoggedOut() {
	emite.receives(SessionManager.Events.onLoggedOut);
	verify(session).setState(State.disconnected);
	emite.verifyPublished(BoshManager.Events.stop);
    }
}
