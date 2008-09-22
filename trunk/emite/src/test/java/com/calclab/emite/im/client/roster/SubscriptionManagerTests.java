package com.calclab.emite.im.client.roster;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.Presence.Type;
import com.calclab.emite.testing.MockedSession;
import com.calclab.suco.testing.listener.EventTester;
import com.calclab.suco.testing.listener.MockListener2;

public class SubscriptionManagerTests {

    private MockedSession session;
    private SubscriptionManager manager;
    private Roster roster;

    @Before
    public void beforeTests() {
	session = new MockedSession();
	roster = mock(Roster.class);
	manager = new SubscriptionManagerImpl(session, roster);
	session.login(uri("user@local"), "anything");
    }

    @Test
    public void shouldApproveSubscriptionRequestsAndAddItemToTheRosterIfNotThere() {
	final XmppURI otherEntityJID = XmppURI.jid("other@domain");
	stub(roster.findByJID(eq(otherEntityJID))).toReturn(null);

	manager.approveSubscriptionRequest(otherEntityJID, "nick");
	verify(roster).addItem(eq(otherEntityJID), eq("nick"));
	session.verifySent("<presence type='subscribed' to='other@domain' />");
	session.verifySent("<presence type='subscribe' to='other@domain' />");
    }

    @Test
    public void shouldCancelSubscription() {
	manager.cancelSubscription(uri("friend@domain"));
	session.verifySent("<presence from='user@local' to='friend@domain' type='unsubscribe' />");
    }

    @Test
    public void shouldFireSubscriptionRequests() {
	final MockListener2<XmppURI, String> listener = new MockListener2<XmppURI, String>();
	manager.onSubscriptionRequested(listener);
	session.receives("<presence to='user@local' from='friend@domain' type='subscribe' />");
	assertEquals(1, listener.getCalledTimes());
    }

    @Test
    public void shouldSendSubscriptionRequest() {
	manager.requestSubscribe(uri("name@domain/RESOURCE"));
	session.verifySent("<presence from='user@local' to='name@domain' type='subscribe'/>");
    }

    /**
     * Step 2: As a result, the user's server (1) MUST initiate a roster push
     * for the new roster item to all available resources associated with this
     * user that have requested the roster, setting the 'subscription' attribute
     * to a value of "none"
     * 
     * Step 3: If the user wants to request a subscription to the contact's
     * presence information, the user's client MUST send a presence stanza of
     * type='subscribe' to the contact:
     */
    @Test
    public void shouldSendSubscriptionRequestOnNewRosterItem() {
	final EventTester<RosterItem> event = new EventTester<RosterItem>();
	verify(roster).onItemAdded(argThat(event));

	// only NONE subscription
	event.fire(new RosterItem(uri("name@domain"), SubscriptionState.both, "TheName", null));
	session.verifyNotSent("<presence />");

	// only if ASK is null
	event.fire(new RosterItem(uri("name@domain"), SubscriptionState.none, "TheName", Type.subscribe));
	session.verifyNotSent("<presence />");

	event.fire(new RosterItem(uri("name@domain"), SubscriptionState.none, "TheName", null));
	session.verifySent("<presence from='user@local' to='name@domain' type='subscribe'/>");
    }

    @Test
    public void shouldUnsubscribe() {
	manager.unsubscribe(uri("friend@domain"));
	session.verifySent("<presence from='user@local' to='friend@domain' type='unsubscribe' />");
    }
}
