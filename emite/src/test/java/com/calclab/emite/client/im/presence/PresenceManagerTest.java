package com.calclab.emite.client.im.presence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.calclab.emite.client.xmpp.session.SessionManager;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.Presence.Type;
import com.calclab.emite.testing.EmiteTestHelper;
import com.calclab.emite.testing.TestMatchers;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.*;

public class PresenceManagerTest {

    private PresenceManager manager;
    private PresenceListener presenceListener;
    private EmiteTestHelper emite;

    @Before
    public void aaaCreateManager() {
	emite = new EmiteTestHelper();
	manager = new PresenceManager(emite);
	manager.install();

	presenceListener = Mockito.mock(PresenceListener.class);
	manager.addListener(presenceListener);
    }

    @Test
    public void managerShouldFireAvailablePresence() {
	final Presence presence = createPresence(Type.available);
	emite.receives(presence);
	Mockito.verify(presenceListener).onPresenceReceived((Presence) TestMatchers.packetLike(presence));
    }

    @Test
    public void managerShouldFireUnavailablePresence() {
	final Presence presence = createPresence(Type.unavailable);
	emite.receives(presence);
	Mockito.verify(presenceListener).onPresenceReceived((Presence) TestMatchers.packetLike(presence));
    }

    @Test
    public void shouldHandleLoggedIn() {
	final String uri = "name@domain/resource";
	emite.receives(SessionManager.Events.loggedIn(uri));
	final XmppURI userURI = manager.getUserURI();
	assertNotNull(userURI);
	assertEquals(uri, userURI.toString());
    }

    private Presence createPresence(final Type type) {
	final Presence presence = new Presence(type, uri("from@domain"), uri("to@domain"));
	return presence;
    }
}
