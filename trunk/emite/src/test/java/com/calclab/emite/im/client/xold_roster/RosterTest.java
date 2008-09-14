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
import com.calclab.suco.testing.signal.MockSlot;

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
	final MockSlot<RosterItem> listener = new MockSlot<RosterItem>();
	xRoster.onItemChanged(listener);
	xRoster.add(item);
	xRoster.changePresence(item.getJID(), new Presence());
	MockSlot.verifyCalled(listener);
    }

    @Test
    public void shouldFireListenerWhenItemRemoved() {
	final MockSlot<Collection<RosterItem>> slot = new MockSlot<Collection<RosterItem>>();
	xRoster.onRosterChanged(slot);
	xRoster.add(new RosterItem(uri("one@domain/resource1"), Subscription.none, "one"));
	MockSlot.verifyCalled(slot, 1);
	assertEquals(1, slot.getValue(0).size());
	xRoster.removeItem(uri("one@domain/resource2"));
	MockSlot.verifyCalled(slot, 2);

	assertEquals(0, slot.getValue(1).size());
    }

    @Test
    public void shouldInformWhenRosterChanged() {
	final MockSlot<Collection<RosterItem>> slot = new MockSlot<Collection<RosterItem>>();
	xRoster.onRosterChanged(slot);
	final List<RosterItem> itemCollection = new ArrayList<RosterItem>();
	itemCollection.add(new RosterItem(uri("name@domain"), Subscription.none, "name"));
	xRoster.setItems(itemCollection);
	MockSlot.verifyCalled(slot, 1);
	assertTrue(slot.getValue(0).contains(itemCollection.get(0)));
	// verify(oldListener).onRosterChanged(hasSame(itemCollection));
    }

    @Test
    public void shouldInformWhenRosterItemChanged() {
	final MockSlot<Collection<RosterItem>> slot = new MockSlot<Collection<RosterItem>>();
	xRoster.onRosterChanged(slot);
	xRoster.setItems(new ArrayList<RosterItem>());
	MockSlot.verifyCalled(slot, 1);
	assertEquals(0, slot.getValue(0).size());
	xRoster.add(new RosterItem(uri("name@domain/res"), null, null));
	MockSlot.verifyCalled(slot, 2);
	assertEquals(1, slot.getValue(1).size());
    }
}
