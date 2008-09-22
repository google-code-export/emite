package com.calclab.emite.im.client.presence;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.im.client.roster.Roster;
import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.emite.testing.MockedSession;
import com.calclab.suco.testing.listener.EventTester;
import com.calclab.suco.testing.listener.MockListener;

public class PresenceManagerTest {

    private PresenceManager manager;
    private MockedSession session;
    private Roster roster;
    private EventTester<Collection<RosterItem>> onRosterReady;

    @Before
    public void beforeTest() {
	session = new MockedSession();
	roster = mock(Roster.class);
	onRosterReady = new EventTester<Collection<RosterItem>>();
	manager = new PresenceManagerImpl(session, roster);
	verify(roster).onRosterRetrieved(argThat(onRosterReady));
    }

    @Test
    public void shouldBroadcastPresenceIfLoggedin() {
	session.setLoggedIn("myself@domain");
	manager.setOwnPresence(Presence.build("this is my new status", Show.away));
	session.verifySent("<presence><show>away</show>" + "<status>this is my new status</status></presence>");
	final Presence current = manager.getOwnPresence();
	assertEquals(Show.away, current.getShow());
	assertEquals("this is my new status", current.getStatus());
    }

    @Test
    public void shouldEventOwnPresence() {
	session.setLoggedIn(uri("myself@domain"));
	final MockListener<Presence> listener = new MockListener<Presence>();
	manager.onOwnPresenceChanged(listener);
	manager.setOwnPresence(Presence.build("status", Show.away));
	MockListener.verifyCalled(listener);
	assertEquals("status", listener.getValue(0).getStatus());
	assertEquals(Show.away, listener.getValue(0).getShow());
    }

    @Test
    public void shouldHavePresenceEvenLoggedOut() {
	assertNotNull(manager.getOwnPresence());
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
	onRosterReady.fire(new ArrayList<RosterItem>());
	session.verifySent("<presence from='myself@domain'></presence>");
    }

    @Test
    public void shouldSendPresenceIfLoggedIn() {
	session.setLoggedIn(uri("myself@domain"));
	manager.setOwnPresence(new Presence().With(Presence.Show.dnd));
	session.verifySent("<presence><show>dnd</show></presence>");

    }

}
