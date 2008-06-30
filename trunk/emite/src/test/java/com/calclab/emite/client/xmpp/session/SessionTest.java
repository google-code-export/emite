package com.calclab.emite.client.xmpp.session;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;
import static com.calclab.emite.testing.MockSlot.verifyCalled;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.core.bosh.BoshManager;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.core.packet.Packet;
import com.calclab.emite.client.xmpp.sasl.AuthorizationTransaction;
import com.calclab.emite.client.xmpp.session.Session.State;
import com.calclab.emite.client.xmpp.stanzas.Message;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.testing.EmiteTestHelper;
import com.calclab.emite.testing.MockSlot;
import com.calclab.emite.testing.SignalTester;
import com.calclab.suco.client.signal.Slot;

public class SessionTest {

    private Session session;
    private EmiteTestHelper emite;
    private BoshManager boshManager;

    @Before
    public void beforeTest() {
	emite = new EmiteTestHelper();
	boshManager = mock(BoshManager.class);
	final SessionScope scope = mock(SessionScope.class);
	session = new Session(boshManager, emite, scope);
    }

    @Test
    public void shouldHandleAuthorization() {
	final MockSlot<AuthorizationTransaction> listener = new MockSlot<AuthorizationTransaction>();
	session.onLogin(listener);
	final XmppURI uri = uri("name@domain/resource");
	session.login(uri, "password");
	emite.verifyPublished(BoshManager.Events.start("domain"));
	verifyCalled(listener);
    }

    @Test
    public void shouldSignalLogin() {
	final MockSlot<AuthorizationTransaction> listener = new MockSlot<AuthorizationTransaction>();
	session.onLogin(listener);
	session.login(uri("name@domain/resource"), "password");
	verifyCalled(listener);
    }

    @Test
    public void shouldSignalMessages() {
	final MockSlot<Message> listener = new MockSlot<Message>();
	session.onMessage(listener);

	final SignalTester<IPacket> onStanza = new SignalTester<IPacket>();
	verify(boshManager).onStanza(argThat(onStanza));
	onStanza.fire(new Packet("message"));
	verifyCalled(listener);
    }

    @Test
    public void shouldSignalPresences() {
	final MockSlot<Presence> listener = new MockSlot<Presence>();
	session.onPresence(listener);

	final SignalTester<IPacket> onStanza = new SignalTester<IPacket>();
	verify(boshManager).onStanza(argThat(onStanza));
	onStanza.fire(new Packet("presence"));
	verifyCalled(listener);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldSignalStateChanges() {
	final Slot<State> listener = mock(Slot.class);
	session.onStateChanged(listener);
	session.setState(State.ready);
	verify(listener).onEvent(same(State.ready));
    }

}
