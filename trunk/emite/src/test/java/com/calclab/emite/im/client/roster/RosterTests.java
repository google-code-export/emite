package com.calclab.emite.im.client.roster;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.Collection;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.testing.MockedSession;
import com.calclab.suco.testing.listener.MockListener;

public class RosterTests {

    private MockedSession session;
    private Roster roster;
    private SubscriptionManager subscriptionManager;

    @Before
    public void beforeTests() {
	session = new MockedSession();

	subscriptionManager = mock(SubscriptionManager.class);
	roster = new RosterImpl(session, subscriptionManager);
    }

    @Test
    public void shouldAddItem() {
	final MockListener<RosterItem> listener = new MockListener<RosterItem>();
	roster.onItemAdded(listener);

	session.receives("<iq type='set'><query xmlns='jabber:iq:roster'>"
		+ "<item jid='friend@domain' name='MyFriend'><group>Group1</group><group>Group2</group>"
		+ "</item></query></iq>");
	MockListener.verifyCalled(listener);
	assertEquals(1, roster.getItems().size());
    }

    @Test
    public void shouldFindRosterItemByJID() {
	shouldRequestRosterOnLogin();
	session.answer(serverRoster());

	final RosterItem item = roster.findByJID(uri("romeo@example.net"));
	assertNotNull(item);
	assertSame(item, roster.findByJID(uri("romeo@example.net/RESOURCE")));
    }

    @Test
    public void shouldFireEventOnlyWhenRosterReady() {
	final MockListener<Collection<RosterItem>> listener = new MockListener<Collection<RosterItem>>();
	roster.onRosterRetrieved(listener);

	shouldRequestRosterOnLogin();
	session.answer(new IQ(Type.error));
	MockListener.verifyNotCalled(listener);
    }

    @Test
    public void shouldFireEventWhenRosterReady() {
	final MockListener<Collection<RosterItem>> listener = new MockListener<Collection<RosterItem>>();
	roster.onRosterRetrieved(listener);

	shouldRequestRosterOnLogin();
	session.answer(serverRoster());
	MockListener.verifyCalled(listener);
    }

    @Test
    public void shouldRemoveItems() {
	final MockListener<RosterItem> listener = new MockListener<RosterItem>();
	roster.onItemRemoved(listener);

	session.receives("<iq type='set'><query xmlns='jabber:iq:roster'>"
		+ "<item jid='friend@domain' name='MyFriend'><group>Group1</group><group>Group2</group>"
		+ "</item></query></iq>");
	assertEquals(1, roster.getItems().size());
	assertEquals(2, roster.getGroups().size());
	session.receives("<iq type='set'><query xmlns='jabber:iq:roster'>"
		+ "<item jid='friend@domain' subscription='remove' name='MyFriend'><group>Group1</group>"
		+ "</item></query></iq>");
	MockListener.verifyCalled(listener);
	assertEquals(0, roster.getItems().size());
	assertEquals(0, roster.getGroups().size());
    }

    @Test
    public void shouldRequestAddItem() {
	roster.addItem(uri("friend@domain/anyResource"), "MyFriend", "Group1", "Group2");
	session.verifyIQSent("<iq type='set'><query xmlns='jabber:iq:roster'>"
		+ "<item jid='friend@domain' name='MyFriend'><group>Group1</group><group>Group2</group>"
		+ "</item></query></iq>");
    }

    @Test
    public void shouldRequestRemoveItem() {
	roster.removeItem(uri("friend@domain"));
	session.verifyNotSent("<iq />");
	session.receives("<iq type='set'><query xmlns='jabber:iq:roster'>"
		+ "<item jid='friend@domain' name='MyFriend'><group>Group1</group><group>Group2</group>"
		+ "</item></query></iq>");
	roster.removeItem(uri("friend@domain"));
	session.verifyIQSent("<iq type='set'><query xmlns='jabber:iq:roster'>"
		+ "<item jid='friend@domain' subscription='remove'/></query></iq>");
    }

    @Test
    public void shouldRequestRosterOnLogin() {
	session.setLoggedIn("user@domain/resource");
	session.verifyIQSent("<iq type='get' ><query xmlns='jabber:iq:roster'/></iq>");
    }

    @Test
    public void shouldRequestUpdateItem() {
	session.receives("<iq type='set'><query xmlns='jabber:iq:roster'>"
		+ "<item jid='friend@domain' name='MyFriend'><group>Group1</group><group>Group2</group>"
		+ "</item></query></iq>");
	final RosterItem item = roster.findByJID(uri("friend@domain"));
	assertNotNull(item);
	roster.updateItem(uri("no@one"), "name", "group");
	session.verifyNotSent("<iq/>");
	roster.updateItem(uri("friend@domain"), "MyOldFriend", "Group1", "Group3");
	session.verifyIQSent("<iq type='set'><query xmlns='jabber:iq:roster'>"
		+ "<item jid='friend@domain' name='MyOldFriend'><group>Group1</group><group>Group3</group>"
		+ "</item></query></iq>");
    }

    @Test
    public void shouldRetrieveItemsByGroup() {
	shouldRequestRosterOnLogin();
	session.answer(serverRoster());
	assertEquals(2, roster.getItemsByGroup("Friends").size());
	assertEquals(1, roster.getItemsByGroup("Work").size());
	assertEquals(1, roster.getItemsByGroup("X").size());
    }

    @Test
    public void shouldRetrieveTheGroups() {
	shouldRequestRosterOnLogin();
	session.answer(serverRoster());
	final Set<String> groups = roster.getGroups();
	assertNotNull(groups);
	assertEquals(3, groups.size());
	assertTrue(groups.contains("Friends"));
	assertTrue(groups.contains("X"));
	assertTrue(groups.contains("Work"));
    }

    @Test
    public void shouldUpdateItem() {
	final MockListener<RosterItem> listener = new MockListener<RosterItem>();
	roster.onItemUpdated(listener);

	session.receives("<iq type='set'><query xmlns='jabber:iq:roster'>"
		+ "<item jid='friend@domain' name='Friend1'><group>GG1</group><group>GG2</group>"
		+ "</item></query></iq>");
	session.receives("<iq type='set'><query xmlns='jabber:iq:roster'>"
		+ "<item jid='friend@domain' name='Friend2'><group>HH1</group><group>HH2</group>"
		+ "</item></query></iq>");
	MockListener.verifyCalled(listener);
	assertEquals(1, roster.getItems().size());
	assertEquals(2, roster.getGroups().size());
	assertTrue(roster.getGroups().contains("HH1"));
	assertTrue(roster.getGroups().contains("HH2"));
    }

    private String serverRoster() {
	return "<iq to='juliet@example.com/balcony' type='result'><query xmlns='jabber:iq:roster'>"
		+ "<item jid='romeo@example.net' name='R' subscription='both'><group>Friends</group><group>X</group></item>"
		+ "<item jid='mercutio@example.org' name='M' subscription='from'> <group>Friends</group></item>"
		+ "<item jid='benvolio@example.org' name='B' subscription='both'><group>Work</group></item>"
		+ "</query></iq>";
    }
}
