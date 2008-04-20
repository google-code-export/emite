package com.calclab.emite.client.im.roster;

import static com.calclab.emite.testing.TestMatchers.hasSame;
import static com.calclab.emite.testing.TestMatchers.isCollectionOfSize;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.calclab.emite.client.im.roster.RosterItem.Subscription;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;

public class RosterTest {
    private RosterListener listener;
    private Roster roster;

    @Before
    public void aaCreate() {
	roster = new Roster();
	listener = mock(RosterListener.class);
	roster.addListener(listener);
    }

    @Test
    public void shouldFindByJID() {
	final RosterItem item = new RosterItem(XmppURI.parse("someone@domain/resource"), null, null);
	roster.add(item);
	assertSame(item, roster.findItemByURI(XmppURI.parse("someone@domain/different_resource")));
    }

    @Test
    public void shouldFireListenerWhenItemRemoved() {
	roster.add(new RosterItem(XmppURI.parse("one@domain"), Subscription.none, "one"));
	verify(listener, atLeastOnce()).onRosterChanged(isCollectionOfSize(1));
	roster.removeItem(XmppURI.parse("one@domain"));
	verify(listener, atLeastOnce()).onRosterChanged(isCollectionOfSize(0));
    }

    @Test
    public void shouldInformWhenRosterChanged() {
	final List<RosterItem> itemCollection = new ArrayList<RosterItem>();
	itemCollection.add(new RosterItem(XmppURI.parse("name@domain"), Subscription.none, "name"));
	roster.setItems(itemCollection);
	verify(listener).onRosterChanged(hasSame(itemCollection));
    }

    @Test
    public void shouldInformWhenRosterItemChanged() {
	roster.setItems(new ArrayList<RosterItem>());
	verify(listener, atLeastOnce()).onRosterChanged(isCollectionOfSize(0));
	roster.add(new RosterItem(XmppURI.parse("name@domain/res"), null, null));
	verify(listener, atLeastOnce()).onRosterChanged(isCollectionOfSize(1));
    }
}
