package com.calclab.emite.client.im.presence;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.calclab.emite.client.core.emite.Emite;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.Presence.Type;

public class PresenceManagerTests {

    private PresenceManager manager;
    private PresenceListener presenceListener;

    @Before
    public void aaaCreateManager() {
	final Emite emite = Mockito.mock(Emite.class);
	manager = new PresenceManager(emite);
	presenceListener = Mockito.mock(PresenceListener.class);
	manager.addListener(presenceListener);
    }

    @Test
    public void managerShouldFireAvailablePresence() {
	final Presence presence = createPresence(Type.available);
	manager.onPresenceReceived(presence);
	Mockito.verify(presenceListener).onPresenceReceived(presence);
    }

    @Test
    public void managerShouldFireSubscriptionRequests() {
	final Presence presence = createPresence(Type.subscribe);
	manager.onPresenceReceived(presence);
	Mockito.verify(presenceListener).onSubscriptionRequest(presence);
    }

    @Test
    public void managerShouldFireUnavailablePresence() {
	final Presence presence = createPresence(Type.unavailable);
	manager.onPresenceReceived(presence);
	Mockito.verify(presenceListener).onPresenceReceived(presence);
    }

    @Test
    public void managerShouldFireUnsubscribeEvents() {
	final Presence presence = createPresence(Type.unsubscribed);
	manager.onPresenceReceived(presence);
	Mockito.verify(presenceListener).onUnsubscribedReceived(presence);
    }

    private Presence createPresence(final Type type) {
	final Presence presence = new Presence(type, XmppURI.parse("from@domain"), XmppURI.parse("to@domain"));
	return presence;
    }
}
