package com.calclab.emite.im.client.roster;

import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.listener.Listener;

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
     */
    public void approveSubscriptionRequest(XmppURI jid);

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
     *            the listener to be called
     */
    public void onSubscriptionRequested(Listener<XmppURI> listener);

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
