package com.calclab.emite.im.client.roster;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.events.Listener2;

/**
 * Manager presence subscriptions between users. Also, it take cares of
 * integration of roster items and presence subscriptions
 * 
 * @see http://www.xmpp.org/rfcs/rfc3921.html#sub
 * @see http://www.xmpp.org/rfcs/rfc3921.html#int
 */
public interface SubscriptionManager {

    /**
     * Approves previously subscription request stanza
     * 
     * @param jid
     *            the other entity's JID
     * @param nick
     *            the desired roster nick
     */
    public void approveSubscriptionRequest(XmppURI jid, String nick);

    /**
     * Cancels a previously-granted subscription
     * 
     * @param jid
     *            the entity's jid (resource ignored)
     */
    public void cancelSubscription(XmppURI jid);

    /**
     * This listener is called when a INCOMING subscription request arrived (a
     * request to subscribe to the current logged in user's presence)
     * 
     * @param listener
     *            the listener to be called (with the jid of the entity and the
     *            nick name)
     */
    public void onSubscriptionRequested(Listener2<XmppURI, String> listener);

    /**
     * Refuse a previously subscription request stanza
     * 
     * @param jid
     *            the other entity's JID
     */
    public void refuseSubscriptionRequest(XmppURI jid);

    /**
     * Send a request to subscribe to another entity's presence
     * 
     * @param jid
     *            the another entity's jid (resource ignored)
     */
    public void requestSubscribe(XmppURI jid);

    /**
     * Unsubscribes from the presence of another entity
     * 
     * @param jid
     *            the another entity's jid (resource ignored)
     */
    public void unsubscribe(XmppURI jid);
}
