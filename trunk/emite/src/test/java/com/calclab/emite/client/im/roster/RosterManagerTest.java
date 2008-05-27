package com.calclab.emite.client.im.roster;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;
import static com.calclab.emite.testing.MockitoEmiteHelper.isListOfSize;
import static com.calclab.emite.testing.MockitoEmiteHelper.packetLike;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.calclab.emite.client.im.roster.RosterManager.SubscriptionMode;
import com.calclab.emite.client.xmpp.session.SessionManager;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.testing.EmiteTestHelper;

public class RosterManagerTest {
    private EmiteTestHelper emite;
    private RosterManager manager;
    private Roster roster;
    private RosterManagerListener listener;

    @Before
    public void aaCreate() {
	emite = new EmiteTestHelper();
	roster = mock(Roster.class);
	manager = new RosterManager(emite, roster);
	listener = mock(RosterManagerListener.class);
	manager.addListener(listener);
    }

    @Test
    public void shouldAcceptAutomatically() {
	manager.setSubscriptionMode(SubscriptionMode.autoAcceptAll);
	emite.receives(new Presence(Presence.Type.subscribe, uri("from@domain"), uri("to@domain")));
	emite.verifySent("<presence type='subscribed' />");
	emite.verifySent("<presence type='subscribe' to='from@domain' />");
    }

    @Test
    public void shouldAddNewRosterItemWhenSubscriptionAccepted() {
	manager.acceptSubscription(new Presence(Presence.Type.subscribe, uri("from@domain"), uri("to@domain")));
	Mockito.verify(roster).add((RosterItem) anyObject());
    }

    @Test
    public void shouldAddRosterItem() {
	emite.receives(SessionManager.Events.loggedIn("user@domain/res"));
	manager.requestAddItem(uri("name@domain/res"), "the name", "the group");
	verify(roster).add((RosterItem) anyObject());
	emite.verifyIQSent("<iq from='user@domain/res' type='set'><query xmlns='jabber:iq:roster'>"
		+ "<item jid='name@domain/res' name='the name'><group>the group</group></item></query></iq>");
	emite.answerSuccess();
    }

    @Test
    public void shouldFireSubscribeEvents() {
	manager.setSubscriptionMode(SubscriptionMode.manual);
	final Presence presence = new Presence(Presence.Type.subscribe, uri("from@domain"), uri("to@domain"));
	emite.receives(presence);
	Mockito.verify(listener).onSubscriptionRequest((Presence) packetLike(presence), same(SubscriptionMode.manual));
    }

    @Test
    public void shouldHadleUncompleteJids() {
	emite
		.receives("<iq type='set' id='theId' to='user@domain/res'><query xmlns='jabber:iq:roster'><item jid='testunknownjid' name='testunknownname' ask='subscribe' subscription='none'/></query></iq>");
	final XmppURI incompleteJid = uri("testunknownjid");
	verify(roster).changeSubscription(incompleteJid, "none");
    }

    @Test
    public void shouldHandleIQSets() {
	emite.receives("<iq id='theId' type='set'><query xmlns='jabber:iq:roster'><item jid='contact@example.org' "
		+ "subscription='none' name='MyContact'><group>MyBuddies</group></item></query></iq>");
	emite.verifySent("<iq type='result' id='theId' />");
	verify(roster).changeSubscription(uri("contact@example.org"), "none");
    }

    @Test
    public void shouldHandleIQSetsWhenSubscribed() {
	emite.receives("<iq id='theId' type='set'><query xmlns='jabber:iq:roster'><item jid='contact@example.org' "
		+ "subscription='to' name='MyContact'><group>MyBuddies</group></item></query></iq>");
	emite.verifySent("<iq type='result' id='theId' />");
	verify(roster).changeSubscription(uri("contact@example.org"), "to");
    }

    @Test
    public void shouldHandlePresence() {
	emite.receives("<presence from='userInRoster@domain/res' to='user@domain/res'>"
		+ "<priority>2</priority></presence>");
	verify(roster).changePresence(eq(uri("userInRoster@domain/res")), (Presence) anyObject());
    }

    @Test
    public void shouldHandlePresenceWithUncompleteJid() {
	emite.receives("<presence from='userInRoster' to='user@domain/res'>" + "<priority>2</priority></presence>");
	verify(roster).changePresence(eq(uri("userInRoster")), (Presence) anyObject());
    }

    @Test
    public void shouldInformWhenUnsubscribed() {
	final String presence = "<presence from='contact@example.org' to='user@example.com' type='unsubscribed'/>";
	emite.receives(presence);
	Mockito.verify(listener).onUnsubscribedReceived(uri("contact@example.org"));
    }

    @Test
    public void shouldRejectAutomatically() {
	manager.setSubscriptionMode(SubscriptionMode.autoRejectAll);
	emite.receives(new Presence(Presence.Type.subscribe, uri("from@domain"), uri("to@domain")));
	emite.verifySent("<presence type='unsubscribed' />");
    }

    @Test
    public void shouldRemoveItemsToRoster() {
	final XmppURI uri = uri("name@domain/res");
	manager.requestRemoveItem(uri);
	emite.verifyIQSent(new IQ(Type.set));
	emite.answerSuccess();
	verify(roster).removeItem(uri);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldRequestRosterOnLogin() {
	emite.receives(SessionManager.Events.loggedIn("user@domain/res"));
	emite.verifyIQSent(new IQ(IQ.Type.get).WithQuery("jabber:iq:roster"));
	emite.answer("<iq type='result' xmlns='jabber:client'><query xmlns='jabber:iq:roster'>"
		+ "<item jid='name1@domain' subscription='both' name='complete name1' />"
		+ "<item jid='name2@domain' subscription='both' name='complete name2' />" + "</query></iq>");
	verify(roster).setItems(isListOfSize(2));
    }

    @Test
    public void shouldRequestSubscribe() {
	manager.requestSubscribe(uri("some@domain/res"));
	emite.verifySent("<presence to='some@domain' type='subscribe' />");
    }

    @Test
    public void shouldUnsubscribe() {
	manager.setSubscriptionMode(SubscriptionMode.manual);
	final Presence presence = new Presence(Presence.Type.unsubscribed, uri("from@domain"), uri("to@domain"));
	emite.receives(presence);
	verify(listener).onUnsubscribedReceived(uri("from@domain"));
    }

}
