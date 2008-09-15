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
import com.calclab.emite.im.client.roster.RosterItem.Subscription;
import com.calclab.emite.im.client.xold_roster.XRoster;
import com.calclab.suco.testing.listener.MockListener;

public class RosterTest {
    private XRoster xRoster;

    @Before
    public void aaCreate() {
	xRoster = new XRoster();
    }

    @Test
    public void shouldFindByJID() {
	final RosterItem item = new RosterItem(uri("someone@domain/resource"), null, null);
	xRoster.add(item);
	assertSame(item, xRoster.findItemByJID(uri("someone@domain/different_resource")));
    }

    @Test
    public void shouldFireListenersWhenPresenceChanged() {
	final RosterItem item = new RosterItem(uri("one@domain"), Subscription.none, "one");
	final MockListener<RosterItem> listener = new MockListener<RosterItem>();
	xRoster.onItemChanged(listener);
	xRoster.add(item);
	xRoster.changePresence(item.getJID(), new Presence());
	MockListener.verifyCalled(listener);
    }

    @Test
    public void shouldFireListenerWhenItemRemoved() {
	final MockListener<Collection<RosterItem>> slot = new MockListener<Collection<RosterItem>>();
	xRoster.onRosterChanged(slot);
	xRoster.add(new RosterItem(uri("one@domain/resource1"), Subscription.none, "one"));
	MockListener.verifyCalled(slot, 1);
	assertEquals(1, slot.getValue(0).size());
	xRoster.removeItem(uri("one@domain/resource2"));
	MockListener.verifyCalled(slot, 2);

	assertEquals(0, slot.getValue(1).size());
    }

    @Test
    public void shouldInformWhenRosterChanged() {
	final MockListener<Collection<RosterItem>> slot = new MockListener<Collection<RosterItem>>();
	xRoster.onRosterChanged(slot);
	final List<RosterItem> itemCollection = new ArrayList<RosterItem>();
	itemCollection.add(new RosterItem(uri("name@domain"), Subscription.none, "name"));
	xRoster.setItems(itemCollection);
	MockListener.verifyCalled(slot, 1);
	assertTrue(slot.getValue(0).contains(itemCollection.get(0)));
	// verify(oldListener).onRosterChanged(hasSame(itemCollection));
    }

    @Test
    public void shouldInformWhenRosterItemChanged() {
	final MockListener<Collection<RosterItem>> slot = new MockListener<Collection<RosterItem>>();
	xRoster.onRosterChanged(slot);
	xRoster.setItems(new ArrayList<RosterItem>());
	MockListener.verifyCalled(slot, 1);
	assertEquals(0, slot.getValue(0).size());
	xRoster.add(new RosterItem(uri("name@domain/res"), null, null));
	MockListener.verifyCalled(slot, 2);
	assertEquals(1, slot.getValue(1).size());
    }
}
