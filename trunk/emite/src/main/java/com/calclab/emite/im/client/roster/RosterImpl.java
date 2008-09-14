package com.calclab.emite.im.client.roster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.MatcherFactory;
import com.calclab.emite.core.client.packet.PacketMatcher;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.core.client.xmpp.stanzas.IQ.Type;
import com.calclab.emite.im.client.roster.RosterItem.Subscription;
import com.calclab.suco.client.signal.Signal;
import com.calclab.suco.client.signal.Slot;

public class RosterImpl implements Roster {

    private static final PacketMatcher ROSTER_QUERY_FILTER = MatcherFactory.byNameAndXMLNS("query", "jabber:iq:roster");
    private final Session session;
    private final HashMap<XmppURI, RosterItem> itemsByJID;
    private final HashMap<String, List<RosterItem>> itemsByGroup;

    private final Signal<Collection<RosterItem>> onRosterReady;
    private final Signal<RosterItem> onItemAdded;
    private final Signal<RosterItem> onItemUpdated;
    private final Signal<RosterItem> onItemRemoved;

    public RosterImpl(final Session session) {
	this.session = session;
	itemsByJID = new HashMap<XmppURI, RosterItem>();
	itemsByGroup = new HashMap<String, List<RosterItem>>();

	this.onItemAdded = new Signal<RosterItem>("roster:onItemAdded");
	this.onItemUpdated = new Signal<RosterItem>("roster:onItemUpdated");
	this.onItemRemoved = new Signal<RosterItem>("roster:onItemRemoved");

	this.onRosterReady = new Signal<Collection<RosterItem>>("roster:onRosterReady");

	session.onLoggedIn(new Slot<XmppURI>() {
	    public void onEvent(final XmppURI user) {
		requestRoster(user);
	    }
	});

	session.onIQ(new Slot<IQ>() {
	    public void onEvent(final IQ iq) {
		final IPacket query = iq.getFirstChild(ROSTER_QUERY_FILTER);
		if (query != null) {
		    for (final IPacket child : query.getChildren()) {
			handle(RosterItem.parse(child));
		    }
		}
	    }

	    private void handle(final RosterItem item) {
		final RosterItem old = findByJID(item.getJID());
		if (old == null) {
		    addItem(item);
		    onItemAdded.fire(item);
		} else {
		    removeItem(old);
		    if (item.getSubscription() == Subscription.remove) {
			onItemRemoved.fire(item);
		    } else {
			addItem(item);
			onItemUpdated.fire(item);
		    }
		}

	    }

	});
    }

    public void addItem(final XmppURI jid, final String name, final String... groups) {
	if (findByJID(jid) == null) {
	    final RosterItem item = new RosterItem(jid, null, name);
	    for (final String group : groups) {
		item.addGroup(group);
	    }
	    final IQ iq = new IQ(Type.set);
	    item.addStanzaTo(iq.addQuery("jabber:iq:roster"));
	    session.sendIQ("roster", iq, new Slot<IPacket>() {
		public void onEvent(final IPacket parameter) {
		}
	    });
	}
    }

    public RosterItem findByJID(final XmppURI jid) {
	return itemsByJID.get(jid.getJID());
    }

    public Set<String> getGroups() {
	return itemsByGroup.keySet();
    }

    public Collection<RosterItem> getItems() {
	return itemsByJID.values();
    }

    public Collection<RosterItem> getItemsByGroup(final String groupName) {
	return itemsByGroup.get(groupName);
    }

    public void onItemAdded(final Slot<RosterItem> slot) {
	onItemAdded.add(slot);
    }

    public void onItemRemoved(final Slot<RosterItem> callback) {
	onItemRemoved.add(callback);
    }

    public void onItemUpdated(final Slot<RosterItem> callback) {
	onItemUpdated.add(callback);
    }

    public void onRosterRetrieved(final Slot<Collection<RosterItem>> slot) {
	onRosterReady.add(slot);
    }

    public void removeItem(final XmppURI uri) {
	final RosterItem item = findByJID(uri.getJID());
	if (item != null) {
	    final IQ iq = new IQ(Type.set);
	    final IPacket itemNode = iq.addQuery("jabber:iq:roster").addChild("item", null);
	    itemNode.With("subscription", "remove").With("jid", item.getJID().toString());
	    session.sendIQ("remove-roster-item", iq, new Slot<IPacket>() {
		public void onEvent(final IPacket parameter) {
		}
	    });
	}
    }

    /**
     * Add item either to itemsByGroup and itemsById
     * 
     * @param item
     */
    private void addItem(final RosterItem item) {
	itemsByJID.put(item.getJID(), item);
	for (final String group : item.getGroups()) {
	    List<RosterItem> items = itemsByGroup.get(group);
	    if (items == null) {
		items = new ArrayList<RosterItem>();
		itemsByGroup.put(group, items);
	    }
	    items.add(item);
	}
    }

    private void removeItem(final RosterItem item) {
	itemsByJID.remove(item.getJID());
	final ArrayList<String> groupsToRemove = new ArrayList<String>();
	for (final String groupName : itemsByGroup.keySet()) {
	    final List<RosterItem> group = itemsByGroup.get(groupName);
	    group.remove(item);
	    if (group.size() == 0) {
		groupsToRemove.add(groupName);
	    }
	}
	for (final String groupName : groupsToRemove) {
	    itemsByGroup.remove(groupName);
	}
    }

    private void requestRoster(final XmppURI user) {
	session.sendIQ("roster", new IQ(IQ.Type.get, user, null).WithQuery("jabber:iq:roster"), new Slot<IPacket>() {
	    public void onEvent(final IPacket received) {
		if (IQ.isSuccess(received)) {
		    itemsByJID.clear();
		    final List<? extends IPacket> children = received.getFirstChild("query").getChildren();
		    for (final IPacket child : children) {
			final RosterItem item = RosterItem.parse(child);
			addItem(item);
		    }
		    onRosterReady.fire(getItems());
		}
	    }

	});
    }
}
