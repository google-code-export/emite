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
import com.calclab.emite.im.client.xold_roster.XRoster;
import com.calclab.emite.im.client.xold_roster.XRosterItem;
import com.calclab.emite.im.client.xold_roster.XRosterItem.Subscription;
import com.calclab.suco.testing.signal.MockSlot;

public class RosterTest {
    private XRoster xRoster;

    @Before
    public void aaCreate() {
	xRoster = new XRoster();
    }

    @Test
    public void shouldFindByJID() {
	final XRosterItem item = new XRosterItem(uri("someone@domain/resource"), null, null);
	xRoster.add(item);
	assertSame(item, xRoster.findItemByJID(uri("someone@domain/different_resource")));
    }

    @Test
    public void shouldFireListenersWhenPresenceChanged() {
	final XRosterItem item = new XRosterItem(uri("one@domain"), Subscription.none, "one");
	final MockSlot<XRosterItem> listener = new MockSlot<XRosterItem>();
	xRoster.onItemChanged(listener);
	xRoster.add(item);
	xRoster.changePresence(item.getJID(), new Presence());
	MockSlot.verifyCalled(listener);
    }

    @Test
    public void shouldFireListenerWhenItemRemoved() {
	final MockSlot<Collection<XRosterItem>> slot = new MockSlot<Collection<XRosterItem>>();
	xRoster.onRosterChanged(slot);
	xRoster.add(new XRosterItem(uri("one@domain/resource1"), Subscription.none, "one"));
	MockSlot.verifyCalled(slot, 1);
	assertEquals(1, slot.getValue(0).size());
	xRoster.removeItem(uri("one@domain/resource2"));
	MockSlot.verifyCalled(slot, 2);

	assertEquals(0, slot.getValue(1).size());
    }

    @Test
    public void shouldInformWhenRosterChanged() {
	final MockSlot<Collection<XRosterItem>> slot = new MockSlot<Collection<XRosterItem>>();
	xRoster.onRosterChanged(slot);
	final List<XRosterItem> itemCollection = new ArrayList<XRosterItem>();
	itemCollection.add(new XRosterItem(uri("name@domain"), Subscription.none, "name"));
	xRoster.setItems(itemCollection);
	MockSlot.verifyCalled(slot, 1);
	assertTrue(slot.getValue(0).contains(itemCollection.get(0)));
	// verify(oldListener).onRosterChanged(hasSame(itemCollection));
    }

    @Test
    public void shouldInformWhenRosterItemChanged() {
	final MockSlot<Collection<XRosterItem>> slot = new MockSlot<Collection<XRosterItem>>();
	xRoster.onRosterChanged(slot);
	xRoster.setItems(new ArrayList<XRosterItem>());
	MockSlot.verifyCalled(slot, 1);
	assertEquals(0, slot.getValue(0).size());
	xRoster.add(new XRosterItem(uri("name@domain/res"), null, null));
	MockSlot.verifyCalled(slot, 2);
	assertEquals(1, slot.getValue(1).size());
    }
}
