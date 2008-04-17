package com.calclab.emite.client.xmpp.session;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.xmpp.session.Session.State;

public class SessionTest {

    private SessionManager manager;
    private Session session;

    @Before
    public void aaCreate() {
	manager = mock(SessionManager.class);
	session = new Session(manager);
    }

    @Test
    public void shouldFireCurrentStateWhenAddAListener() {
	final State initialState = State.disconnected;
	session.setState(initialState);
	final SessionListener listener1 = mock(SessionListener.class);
	final SessionListener listener2 = mock(SessionListener.class);
	session.addListener(listener2);
	session.addListener(listener1);
	verify(listener1).onStateChanged(initialState, initialState);
	verify(listener2).onStateChanged(initialState, initialState);
	final State newState = State.connected;
	session.setState(newState);
	verify(listener1).onStateChanged(initialState, newState);
	verify(listener2).onStateChanged(initialState, newState);

    }
}
