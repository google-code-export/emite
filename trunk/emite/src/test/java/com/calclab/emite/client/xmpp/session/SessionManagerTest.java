package com.calclab.emite.client.xmpp.session;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.core.bosh.BoshManager;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.dispatcher.Dispatcher.Events;
import com.calclab.emite.client.xmpp.session.Session.State;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.testing.EmiteTestHelper;

public class SessionManagerTest {

    private EmiteTestHelper emite;
    private Session session;
    private SessionManager manager;

    @Before
    public void aaCreate() {
	emite = new EmiteTestHelper();
	session = mock(Session.class);
	manager = new SessionManager(session, emite);
    }

    @Test
    public void shouldHandleAuthorization() {
	session.login(uri("name@domain/resource"), "password");
	emite.receives(SessionManager.Events.onAuthorized);
	verify(session).setState(same(State.authorized));
	emite.verifyPublished(BoshManager.Events.onRestartStream);
    }

    @Test
    public void shouldInformAboutBadAuthentication() {
	emite.receives(SessionManager.Events.onAuthorizationFailed);
	verify(session).setState(State.notAuthorized);
	emite.verifyPublished(Events.error("not-authorized", ""));
    }

    @Test
    public void shouldRequestSessionWhenBinded() {
	emite.receives(SessionManager.Events.binded("name@domain/resource"));
	emite.verifyIQSent(new IQ(Type.set));
	emite.answerSuccess();
	emite.verifyPublished(SessionManager.Events.loggedIn("name@domain/resource"));
	verify(session).setState(State.connected);

    }

    @Test
    public void shouldSendReadyWhenLoggedIn() {
	emite.receives(SessionManager.Events.onBinded);
	final PacketListener listener = emite.verifyIQSent(new IQ(Type.set));
	verify(session).setState(State.connected);
	emite.verifySent(SessionManager.Events.ready);
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
