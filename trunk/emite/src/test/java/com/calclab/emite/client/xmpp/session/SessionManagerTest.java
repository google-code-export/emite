package com.calclab.emite.client.xmpp.session;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.TestMatchers;
import com.calclab.emite.client.core.bosh.BoshManager;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.xmpp.session.Session.State;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public class SessionManagerTest {

    private Emite emite;
    private SessionManager manager;
    private Session session;

    @Before
    public void aaCreate() {
	emite = mock(Emite.class);
	session = mock(Session.class);
	manager = new SessionManager(emite);
	manager.setSession(session);
    }

    @Test
    public void s() {
	final XmppURI uri = XmppURI.parse("name@domain/resource");
	manager.eventSession(uri);
	verify(session).setState(State.connected);
	verify(emite).publish(TestMatchers.isPacket(SessionManager.Events.loggedIn.With("uri", uri.toString())));

    }

    @Test
    public void shouldHandleAuthorization() {
	manager.eventAuthorized();
	verify(session).setState(State.authorized);
	verify(emite).publish(BoshManager.Events.restart);
    }

    @Test
    public void shouldRequestSessionWhenBinded() {
	manager.eventBinded("name@domain/resource");
	verify(emite).send(eq("session"), (IPacket) anyObject(), (PacketListener) anyObject());
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
	verify(emite).publish(BoshManager.Events.stop);
    }
}
