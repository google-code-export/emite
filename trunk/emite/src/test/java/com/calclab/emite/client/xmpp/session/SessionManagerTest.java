package com.calclab.emite.client.xmpp.session;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.core.bosh.BoshManager;
import com.calclab.emite.client.xmpp.session.Session.State;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.testing.EmiteStub;

public class SessionManagerTest {

    private EmiteStub emite;
    private SessionManager manager;
    private Session session;

    @Before
    public void aaCreate() {
	emite = new EmiteStub();
	session = mock(Session.class);
	manager = new SessionManager(emite);
	manager.install();
	manager.setSession(session);
    }

    @Test
    public void shouldHandleAuthorization() {
	manager.eventAuthorized();
	verify(session).setState(State.authorized);
	emite.verifyPublished(BoshManager.Events.onDoRestart);
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
	emite.verifySendCallback(new IQ(Type.set));
	emite.answerSuccess();
	emite.verifyPublished(SessionManager.Events.loggedIn("name@domain/resource"));
	verify(session).setState(State.connected);

    }

    @Test
    public void shouldSetStatesWhenError() {
	manager.eventOnError();
	verify(session).setState(State.error);
	verify(session).setState(State.disconnected);
    }

    @Test
    public void shouldStopAndDisconnectWhenLoggedOut() {
	manager.eventLoggedOut();
	verify(session).setState(State.disconnected);
	emite.verifyPublished(BoshManager.Events.stop);
    }
}
