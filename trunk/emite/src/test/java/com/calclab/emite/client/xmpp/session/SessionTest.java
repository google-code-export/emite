package com.calclab.emite.client.xmpp.session;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.core.bosh.BoshManager;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.core.signal.Listener;
import com.calclab.emite.client.xmpp.session.Session.State;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.testing.EmiteTestHelper;
import com.calclab.emite.testing.TestSignal;
import com.calclab.emite.testing.TestingListener;

public class SessionTest {

    private Session session;
    private EmiteTestHelper emite;
    private BoshManager boshManager;

    @Before
    public void beforeTest() {
	emite = new EmiteTestHelper();
	boshManager = mock(BoshManager.class);
	session = new Session(boshManager, emite);
    }

    @Test
    public void shouldHandleAuthorization() {
	final XmppURI uri = uri("name@domain/resource");
	session.login(uri, "password");
	emite.verifyPublished(BoshManager.Events.start("domain"));
	emite.verifyPublished(SessionManager.Events.login(uri, "password"));
    }

    @Test
    @Deprecated
    public void shouldInformAboutStateChanges() {
	final State initialState = State.disconnected;
	session.setState(initialState);
	final SessionListener listener1 = mock(SessionListener.class);
	final SessionListener listener2 = mock(SessionListener.class);
	session.addListener(listener2);
	session.addListener(listener1);
	// verify(listener1).onStateChanged(initialState, initialState);
	// verify(listener2).onStateChanged(initialState, initialState);
	final State newState = State.connected;
	session.setState(newState);
	verify(listener1).onStateChanged(initialState, newState);
	verify(listener2).onStateChanged(initialState, newState);

    }

    @Test
    public void shouldSignalMessages() {
	final TestingListener<Message> listener = new TestingListener<Message>();
	session.onMessage(listener);

	final TestSignal<IPacket> onStanza = new TestSignal<IPacket>();
	verify(boshManager).onStanza(argThat(onStanza));
	onStanza.fire(new Packet("message"));
	listener.verify();
    }

    @Test
    public void shouldSignalPresences() {
	final TestingListener<Presence> listener = new TestingListener<Presence>();
	session.onPresence(listener);

	final TestSignal<IPacket> onStanza = new TestSignal<IPacket>();
	verify(boshManager).onStanza(argThat(onStanza));
	onStanza.fire(new Packet("presence"));
	listener.verify();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldSignalStateChanges() {
	final Listener<State> listener = mock(Listener.class);
	session.onStateChanged(listener);
	session.setState(State.ready);
	verify(listener).onEvent(same(State.ready));
    }
}
