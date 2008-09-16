package com.calclab.emite.im.client.xold_roster;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static com.calclab.emite.testing.MockitoEmiteHelper.isListOfSize;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.emite.im.client.xold_roster.XRosterManager.SubscriptionMode;
import com.calclab.emite.testing.MockedSession;
import com.calclab.suco.testing.listener.MockListener;

public class RosterManagerTest {
    private XRosterManager manager;
    private XRoster xRoster;
    private MockedSession session;

    @Before
    public void aaCreate() {
	session = new MockedSession();
	xRoster = mock(XRoster.class);
	manager = new XRosterManagerImpl(session, xRoster);

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
	Mockito.verify(xRoster).add((RosterItem) anyObject());
    }

    @Test
    public void shouldAddRosterItem() {
	session.setLoggedIn(uri("user@domain/res"));
	manager.requestAddItem(uri("name@domain/res"), "the name", "the group");
	verify(xRoster).add((RosterItem) anyObject());
	session.verifyIQSent("<iq from='user@domain/res' type='set'><query xmlns='jabber:iq:roster'>"
		+ "<item jid='name@domain/res' name='the name'><group>the group</group></item></query></iq>");
	session.answerSuccess();
    }

    @Test
    public void shouldHandlePresence() {
	session.receives("<presence from='userInRoster@domain/res' to='user@domain/res'>"
		+ "<priority>2</priority></presence>");
	verify(xRoster).changePresence(eq(uri("userInRoster@domain/res")), (Presence) anyObject());
    }

    @Test
    public void shouldHandlePresenceWithUncompleteJid() {
	session.receives("<presence from='userInRoster' to='user@domain/res'>" + "<priority>2</priority></presence>");
	verify(xRoster).changePresence(eq(uri("userInRoster")), (Presence) anyObject());
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
	verify(xRoster).removeItem(uri);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldRequestRosterOnLogin() {
	session.setLoggedIn("user@domain/res");
	session.verifyIQSent(new IQ(IQ.Type.get).WithQuery("jabber:iq:roster"));
	session.answer("<iq type='result' xmlns='jabber:client'><query xmlns='jabber:iq:roster'>"
		+ "<item jid='name1@domain' subscription='both' name='complete name1' />"
		+ "<item jid='name2@domain' subscription='both' name='complete name2' />" + "</query></iq>");
	verify(xRoster).setItems(isListOfSize(2));
    }

    @Test
    public void shouldRequestSubscribe() {
	manager.requestSubscribe(uri("some@domain/res"));
	session.verifySent("<presence to='some@domain' type='subscribe' />");
    }

    @Test
    public void shouldEventSubscribtionRequests() {
	final MockListener<Presence> listener = new MockListener<Presence>();
	manager.onSubscriptionRequested(listener);
	final Presence presence = new Presence(Presence.Type.subscribe, uri("from@domain"), uri("to@domain"));
	session.receives(presence);
	MockListener.verifyCalled(listener);
    }

    @Test
    public void shouldEventUnsibscirvedEvents() {
	final MockListener<XmppURI> listener = new MockListener<XmppURI>();
	manager.onUnsubscribedReceived(listener);

	final String presence = "<presence from='contact@example.org' to='user@example.com' type='unsubscribed'/>";
	session.receives(presence);
	MockListener.verifyCalled(listener);
    }

}
