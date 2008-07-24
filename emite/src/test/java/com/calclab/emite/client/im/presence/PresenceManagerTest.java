package com.calclab.emite.client.im.presence;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;
import static com.calclab.emite.testing.MockSlot.verifyCalled;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.im.roster.Roster;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.client.xmpp.stanzas.Presence.Type;
import com.calclab.emite.testing.MockSlot;
import com.calclab.emite.testing.MockedSession;
import com.calclab.emite.testing.SignalTester;

public class PresenceManagerTest {

    private PresenceManager manager;
    private MockedSession session;
    private Roster roster;
    private SignalTester<Roster> onRosterReady;

    @Before
    public void beforeTest() {
	session = new MockedSession();
	roster = mock(Roster.class);
	onRosterReady = new SignalTester<Roster>();
	manager = new PresenceManager(session, roster);
	verify(roster).onReady(argThat(onRosterReady));
    }

    @Test
    public void shouldBroadcastPresenceIfLoggedin() {
	session.setLoggedIn("myself@domain");
	manager.setOwnPresence("this is my new status", Show.away);
	session.verifySent("<presence from='myself@domain'><show>away</show>"
		+ "<status>this is my new status</status></presence>");
	final Presence current = manager.getOwnPresence();
	assertEquals(Show.away, current.getShow());
	assertEquals("this is my new status", current.getStatus());
    }

    @Test
    public void shouldDelayPresenceIfNotLoggedIn() {
	manager.setOwnPresence("my message", Show.chat);
	session.verifySentNothing();
	assertEquals(Presence.Type.unavailable, manager.getOwnPresence().getType());
    }

    @Test
    public void shouldHavePresenceEvenLoggedOut() {
	assertNotNull(manager.getOwnPresence());
    }

    @Test
    public void shouldSendDelayedAsSoonAsPossible() {
	manager.setOwnPresence("my delayed status", Show.dnd);
	session.setLoggedIn(uri("myself@domain"));
	onRosterReady.fire(roster);
	session.verifySent("<presence from='myself@domain'><show>chat</show></presence>");
	session.verifySent("<presence from='myself@domain'><show>dnd</show>"
		+ "<status>my delayed status</status></presence>");
    }

    @Test
    public void shouldSendFinalPresence() {
	session.setLoggedIn(uri("myself@domain"));
	session.logout();
	session.verifySent("<presence from='myself@domain' type='unavailable' />");
    }

    @Test
    public void shouldSendInitialPresenceAfterRosterReady() {
	session.setLoggedIn(uri("myself@domain"));
	onRosterReady.fire(roster);
	session.verifySent("<presence from='myself@domain'><show>chat</show></presence>");
    }

    @Test
    public void shouldSendPresenceIfLoggedIn() {
	session.setLoggedIn(uri("myself@domain"));
	manager.setOwnPresence(new Presence().With(Presence.Show.dnd));
	session.verifySent("<presence from='myself@domain'><show>dnd</show></presence>");

    }

    @Test
    public void shouldSignalIncommingPresence() {
	final MockSlot<Presence> slot = new MockSlot<Presence>();
	manager.onPresenceReceived(slot);
	session.receives(createPresence(Type.available));
	session.receives(createPresence(Type.unavailable));
	verifyCalled(slot, 2);
    }

    @Test
    public void shouldSignalOwnPresence() {
	session.setLoggedIn(uri("myself@domain"));
	final MockSlot<Presence> listener = new MockSlot<Presence>();
	manager.onOwnPresenceChanged(listener);
	manager.setOwnPresence("status", Show.away);
	verifyCalled(listener);
	assertEquals("status", listener.getValue(0).getStatus());
	assertEquals(Show.away, listener.getValue(0).getShow());
    }

    private Presence createPresence(final Type type) {
	final Presence presence = new Presence(type, uri("from@domain"), uri("to@domain"));
	return presence;
    }
}
