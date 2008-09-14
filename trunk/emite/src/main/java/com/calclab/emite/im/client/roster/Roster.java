package com.calclab.emite.im.client.roster;

import java.util.Collection;
import java.util.Set;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.signal.Slot;

public interface Roster {

    /**
     * Request add a item to the Roster. If a item with a same JID is already
     * present in the roster, nothing is done. No callback is called until the
     * item is really added to the roster
     * 
     * @param jid
     *            the user JID (resource ignored)
     * @param name
     *            the item name
     * @param groups
     *            the groups you want to put the groups in
     */
    void addItem(XmppURI jid, String name, String... groups);

    /**
     * Find a roster item by its JID (it doesn't take care of the resource if
     * given)
     * 
     * @param jid
     *            the JID of the item (resource is ignored)
     * @return the item if found in roster, null otherwise
     */
    RosterItem findByJID(XmppURI jid);

    Set<String> getGroups();

    /**
     * Retrieve all the RosterItems of the Roster
     * 
     * @return the items of the roster
     */
    Collection<RosterItem> getItems();

    /**
     * Retrieve all the items that belongs to the given group name
     * 
     * @param groupName
     * @return a collection of items
     */
    Collection<RosterItem> getItemsByGroup(String groupName);

    /**
     * Add a callback to know when a item is added to the roster
     * 
     * @param slot
     */
    void onItemAdded(Slot<RosterItem> slot);

    /**
     * Add a callback to know when a item is removed from the roster
     * 
     * @param callback
     */
    void onItemRemoved(Slot<RosterItem> callback);

    /**
     * Fired when a RosterItem is updated
     * 
     * @param callback
     */
    void onItemUpdated(Slot<RosterItem> callback);

    /**
     * Add a callback to receive the Roster when ready
     * 
     * @param slot
     *            a callback that receives the roster as collection of
     *            RosterItems
     */
    void onRosterRetrieved(Slot<Collection<RosterItem>> slot);

    /**
     * Send a request to remove item. No callback is called until the item is
     * really removed from roster
     * 
     * @param uri
     *            the jid (resource ignored) of the roster item to be removed
     */
    void removeItem(XmppURI uri);

}
