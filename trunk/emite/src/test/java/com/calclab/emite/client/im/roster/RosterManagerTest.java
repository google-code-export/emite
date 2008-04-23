package com.calclab.emite.client.im.roster;

import static com.calclab.emite.testing.TestMatchers.*;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.xmpp.session.SessionManager;
import com.calclab.emite.client.xmpp.stanzas.IQ;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.testing.EmiteStub;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.*;

public class RosterManagerTest {
    private EmiteStub emite;
    private RosterManager manager;
    private Roster roster;

    @Before
    public void aaCreate() {
	emite = new EmiteStub();
	roster = mock(Roster.class);
	manager = new RosterManager(emite, roster);
	manager.install();
    }

    @Test
    public void shouldAddRosterItem() {
	emite.receives(SessionManager.Events.loggedIn("user@domain/res"));
	manager.requestAddItem(uri("name@domain/res"), "the name", "the group");
	verify(roster).add((RosterItem) anyObject());
	emite.verifySentWithCallback("<iq from='user@domain/res' type='set'><query xmlns='jabber:iq:roster'>"
		+ "<item jid='name@domain/res' name='the name'><group>the group</group></item></query></iq>");
	emite.answerSuccess();

    }

    @Test
    public void shouldHandleIQSets() {
	emite.receives("<iq id='theId' type='set'><query xmlns='jabber:iq:roster'><item jid='contact@example.org' "
		+ "subscription='none' name='MyContact'><group>MyBuddies</group></item></query></iq>");
	emite.verifySent("<iq xmlns='jabber:client' type='result' id='theId' />");
	verify(roster).changeSubscription(uri("contact@example.org"), "none");
    }

    @Test
    public void shouldRemoveItemsToRoster() {
	final XmppURI uri = uri("name@domain/res");
	manager.requestRemoveItem(uri);
	emite.verifySentWithCallback(new IQ(Type.set));
	emite.answerSuccess();
	verify(roster).removeItem(uri);
    }

    @Test
    public void shouldRequestRosterOnLogin() {
	emite.receives(SessionManager.Events.loggedIn("user@domain/res"));
	emite.verifySentWithCallback(new IQ(IQ.Type.get).WithQuery("jabber:iq:roster", null));
	emite.answer("<iq type='result' xmlns='jabber:client'><query xmlns='jabber:iq:roster'>"
		+ "<item jid='name1@domain' subscription='both' name='complete name1' />"
		+ "<item jid='name2@domain' subscription='both' name='complete name2' />" + "</query></iq>");
	verify(roster).setItems(isListOfSize(2));
    }
}
