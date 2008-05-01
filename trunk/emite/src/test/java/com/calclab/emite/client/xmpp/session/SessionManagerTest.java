package com.calclab.emite.client.xmpp.session;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.core.bosh.BoshManager;
import com.calclab.emite.client.xmpp.session.Session.State;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.testing.EmiteTestHelper;
import static com.calclab.emite.client.xmpp.stanzas.XmppURI.*;

public class SessionManagerTest {

    private EmiteTestHelper emite;
    private SessionManager manager;
    private Session session;

    @Before
    public void aaCreate() {
	emite = new EmiteTestHelper();
	session = mock(Session.class);
	manager = new SessionManager(emite);
	manager.install();
	manager.setSession(session);
    }

    @Test
    public void shouldHandleAuthorization() {
	manager.doLogin(uri("name@domain/resource"), "password");
	emite.receives(SessionManager.Events.onAuthorized);
	verify(session).setState(State.authorized);
	emite.verifyPublished(BoshManager.Events.onRestart);
    }

    @Test
    public void shouldInformAboutBadAuthentication() {
	emite.receives(SessionManager.Events.onAuthorizationFailed);
	verify(session).setState(State.notAuthorized);
	emite.verifyPublished(BoshManager.Events.error("not-authorized", ""));
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
    public void shouldSetStatesWhenError() {
	emite.receives(BoshManager.Events.error("cause", "info"));
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
