package com.calclab.emite.client.im.roster;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;
import static com.calclab.emite.testing.MockSlot.verifyCalled;
import static com.calclab.emite.testing.MockitoEmiteHelper.isListOfSize;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.calclab.emite.client.im.roster.RosterManager.SubscriptionMode;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.testing.MockSlot;
import com.calclab.emite.testing.MockedSession;

public class RosterManagerTest {
    private RosterManager manager;
    private Roster roster;
    private MockedSession session;

    @Before
    public void aaCreate() {
	session = new MockedSession();
	roster = mock(Roster.class);
	manager = new RosterManager(session, roster);

    }

    @Test
    public void shouldAcceptAutomatically() {
	manager.setSubscriptionMode(SubscriptionMode.autoAcceptAll);
	session.receives(new Presence(Presence.Type.subscribe, uri("from@domain"), uri("to@domain")));
	session.verifySent("<presence type='subscribed' />");
	session.verifySent("<presence type='subscribe' to='from@domain' />");
    }

    @Test
    public void shouldAddNewRosterItemWhenSubscriptionAccepted() {
	manager.acceptSubscription(new Presence(Presence.Type.subscribe, uri("from@domain"), uri("to@domain")));
	Mockito.verify(roster).add((RosterItem) anyObject());
    }

    @Test
    public void shouldAddRosterItem() {
	session.setLoggedIn(uri("user@domain/res"));
	manager.requestAddItem(uri("name@domain/res"), "the name", "the group");
	verify(roster).add((RosterItem) anyObject());
	session.verifyIQSent("<iq from='user@domain/res' type='set'><query xmlns='jabber:iq:roster'>"
		+ "<item jid='name@domain/res' name='the name'><group>the group</group></item></query></iq>");
	session.answerSuccess();
    }

    @Test
    public void shouldHadleUncompleteJids() {
	session.receives("<iq type='set' id='theId' to='user@domain/res'>" + "<query xmlns='jabber:iq:roster'>"
		+ "<item jid='testunknownjid' name='testunknownname' ask='subscribe' subscription='none'/>"
		+ "</query></iq>");
	final XmppURI incompleteJid = uri("testunknownjid");
	verify(roster).changeSubscription(incompleteJid, "none");
    }

    @Test
    public void shouldHandleIQSets() {
	session.receives("<iq id='theId' type='set'><query xmlns='jabber:iq:roster'><item jid='contact@example.org' "
		+ "subscription='none' name='MyContact'><group>MyBuddies</group></item></query></iq>");
	session.verifySent("<iq type='result' id='theId' />");
	verify(roster).changeSubscription(uri("contact@example.org"), "none");
    }

    @Test
    public void shouldHandleIQSetsWhenSubscribed() {
	session.receives("<iq id='theId' type='set'><query xmlns='jabber:iq:roster'><item jid='contact@example.org' "
		+ "subscription='to' name='MyContact'><group>MyBuddies</group></item></query></iq>");
	session.verifySent("<iq type='result' id='theId' />");
	verify(roster).changeSubscription(uri("contact@example.org"), "to");
    }

    @Test
    public void shouldHandlePresence() {
	session.receives("<presence from='userInRoster@domain/res' to='user@domain/res'>"
		+ "<priority>2</priority></presence>");
	verify(roster).changePresence(eq(uri("userInRoster@domain/res")), (Presence) anyObject());
    }

    @Test
    public void shouldHandlePresenceWithUncompleteJid() {
	session.receives("<presence from='userInRoster' to='user@domain/res'>" + "<priority>2</priority></presence>");
	verify(roster).changePresence(eq(uri("userInRoster")), (Presence) anyObject());
    }

    @Test
    public void shouldRejectAutomatically() {
	manager.setSubscriptionMode(SubscriptionMode.autoRejectAll);
	session.receives(new Presence(Presence.Type.subscribe, uri("from@domain"), uri("to@domain")));
	session.verifySent("<presence type='unsubscribed' />");
    }

    @Test
    public void shouldRemoveItemsToRoster() {
	final XmppURI uri = uri("name@domain/res");
	manager.requestRemoveItem(uri);
	session.verifyIQSent(new IQ(Type.set));
	session.answerSuccess();
	verify(roster).removeItem(uri);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldRequestRosterOnLogin() {
	session.setLoggedIn("user@domain/res");
	session.verifyIQSent(new IQ(IQ.Type.get).WithQuery("jabber:iq:roster"));
	session.answer("<iq type='result' xmlns='jabber:client'><query xmlns='jabber:iq:roster'>"
		+ "<item jid='name1@domain' subscription='both' name='complete name1' />"
		+ "<item jid='name2@domain' subscription='both' name='complete name2' />" + "</query></iq>");
	verify(roster).setItems(isListOfSize(2));
    }

    @Test
    public void shouldRequestSubscribe() {
	manager.requestSubscribe(uri("some@domain/res"));
	session.verifySent("<presence to='some@domain' type='subscribe' />");
    }

    @Test
    public void shouldSignalSubscribtionRequests() {
	final MockSlot<Presence> listener = new MockSlot<Presence>();
	manager.onSubscriptionRequested(listener);
	final Presence presence = new Presence(Presence.Type.subscribe, uri("from@domain"), uri("to@domain"));
	session.receives(presence);
	verifyCalled(listener);
    }

    @Test
    public void shouldSignalUnsibscirvedEvents() {
	final MockSlot<XmppURI> listener = new MockSlot<XmppURI>();
	manager.onUnsubscribedReceived(listener);

	final String presence = "<presence from='contact@example.org' to='user@example.com' type='unsubscribed'/>";
	session.receives(presence);
	verifyCalled(listener);
    }

    @Test
    @Deprecated
    public void shouldUnsubscribe() {
	final RosterManagerListener oldListener = mock(RosterManagerListener.class);
	new RosterManagerListenerAdapter(manager, oldListener);

	final Presence presence = new Presence(Presence.Type.unsubscribed, uri("from@domain"), uri("to@domain"));
	session.receives(presence);
	verify(oldListener).onUnsubscribedReceived(uri("from@domain"));

    }

}
