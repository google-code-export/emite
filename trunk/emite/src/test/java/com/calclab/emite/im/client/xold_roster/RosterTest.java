package com.calclab.emite.im.client.xold_roster;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.emite.im.client.roster.SubscriptionState;
import com.calclab.suco.testing.listener.MockListener;

public class RosterTest {
    private XRoster xRoster;

    @Before
    public void aaCreate() {
	xRoster = new XRoster();
    }

    @Test
    public void shouldFindByJID() {
	final RosterItem item = new RosterItem(uri("someone@domain/resource"), null, null, null);
	xRoster.add(item);
	assertSame(item, xRoster.findItemByJID(uri("someone@domain/different_resource")));
    }

    @Test
    public void shouldFireListenersWhenPresenceChanged() {
	final RosterItem item = new RosterItem(uri("one@domain"), SubscriptionState.none, "one", null);
	final MockListener<RosterItem> listener = new MockListener<RosterItem>();
	xRoster.onItemChanged(listener);
	xRoster.add(item);
	xRoster.changePresence(item.getJID(), new Presence());
	MockListener.verifyCalled(listener);
    }

    @Test
    public void shouldFireListenerWhenItemRemoved() {
	final MockListener<Collection<RosterItem>> listener = new MockListener<Collection<RosterItem>>();
	xRoster.onRosterChanged(listener);
	xRoster.add(new RosterItem(uri("one@domain/resource1"), SubscriptionState.none, "one", null));
	MockListener.verifyCalled(listener, 1);
	assertEquals(1, listener.getValue(0).size());
	xRoster.removeItem(uri("one@domain/resource2"));
	MockListener.verifyCalled(listener, 2);

	assertEquals(0, listener.getValue(1).size());
    }

    @Test
    public void shouldInformWhenRosterChanged() {
	final MockListener<Collection<RosterItem>> listener = new MockListener<Collection<RosterItem>>();
	xRoster.onRosterChanged(listener);
	final List<RosterItem> itemCollection = new ArrayList<RosterItem>();
	itemCollection.add(new RosterItem(uri("name@domain"), SubscriptionState.none, "name", null));
	xRoster.setItems(itemCollection);
	MockListener.verifyCalled(listener, 1);
	assertTrue(listener.getValue(0).contains(itemCollection.get(0)));
	// verify(oldListener).onRosterChanged(hasSame(itemCollection));
    }

    @Test
    public void shouldInformWhenRosterItemChanged() {
	final MockListener<Collection<RosterItem>> listener = new MockListener<Collection<RosterItem>>();
	xRoster.onRosterChanged(listener);
	xRoster.setItems(new ArrayList<RosterItem>());
	MockListener.verifyCalled(listener, 1);
	assertEquals(0, listener.getValue(0).size());
	xRoster.add(new RosterItem(uri("name@domain/res"), null, null, null));
	MockListener.verifyCalled(listener, 2);
	assertEquals(1, listener.getValue(1).size());
    }
}
