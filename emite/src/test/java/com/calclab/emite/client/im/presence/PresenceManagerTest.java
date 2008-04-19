package com.calclab.emite.client.im.presence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.im.roster.RosterManager;
import com.calclab.emite.client.xmpp.session.SessionManager;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.Presence.Show;
import com.calclab.emite.client.xmpp.stanzas.Presence.Type;
import com.calclab.emite.testing.InstallationTester;
import com.calclab.emite.testing.InstallationTester.InstallTest;
import com.calclab.emite.testing.InstallationTester.InstallVerifier;

public class PresenceManagerTest {

    private PresenceManager manager;
    private PresenceListener presenceListener;
    private Emite emite;

    @Before
    public void aaaCreateManager() {
        emite = Mockito.mock(Emite.class);
        manager = new PresenceManager(emite);
        presenceListener = Mockito.mock(PresenceListener.class);
        manager.addListener(presenceListener);
    }

    @Test
    public void managerShouldFireAvailablePresence() {
        final Presence presence = createPresence(Type.available);
        manager.eventPresence(presence);
        Mockito.verify(presenceListener).onPresenceReceived(presence);
    }

    @Test
    public void managerShouldFireSubscriptionRequests() {
        final Presence presence = createPresence(Type.subscribe);
        manager.eventPresence(presence);
        Mockito.verify(presenceListener).onSubscriptionRequest(presence);
    }

    @Test
    public void managerShouldFireUnavailablePresence() {
        final Presence presence = createPresence(Type.unavailable);
        manager.eventPresence(presence);
        Mockito.verify(presenceListener).onPresenceReceived(presence);
    }

    @Test
    public void managerShouldFireUnsubscribeEvents() {
        final Presence presence = createPresence(Type.unsubscribed);
        manager.eventPresence(presence);
        Mockito.verify(presenceListener).onUnsubscribedReceived(presence);
    }

    @Test
    public void sendOwnPresenceCorrectly() {
        String statusText = "Out for dinner";
        manager.setOwnPresence(statusText, Show.dnd);
        // FIXME: Dani check this (I did delayedPresence default instead of
        // private)
        assertEquals(Show.dnd, manager.delayedPresence.getShow());
        assertEquals(statusText, manager.delayedPresence.getStatus());
    }

    @Test
    public void shouldAttach() {
        new InstallationTester(new InstallTest() {
            public void prepare(final Emite emite, final InstallVerifier verifier) {
                new PresenceManager(emite).install();
                verifier.shouldAttachTo(new Presence());
                verifier.shouldAttachTo(RosterManager.Events.ready);
                verifier.shouldAttachTo(SessionManager.Events.loggedOut);
                verifier.shouldAttachTo(SessionManager.Events.loggedIn);
            }
        });
    }

    @Test
    public void shouldHandleLoggedIn() {
        final String uri = "name@domain/resource";
        manager.eventLoggedIn(SessionManager.Events.loggedIn.With("uri", uri));
        final XmppURI userURI = manager.getUserURI();
        assertNotNull(userURI);
        assertEquals(uri, userURI.toString());
    }

    private Presence createPresence(final Type type) {
        final Presence presence = new Presence(type, XmppURI.parse("from@domain"), XmppURI.parse("to@domain"));
        return presence;
    }
}
