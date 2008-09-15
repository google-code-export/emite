/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.im.client.xold_roster;

import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.listener.Listener;
import com.google.gwt.user.client.rpc.IsSerializable;

// FIXME: vjrj - necesito ayuda para documentar esta clase
// no entiendo EXACTAMENTE qu� hace cada m�todo
public interface XRosterManager {
	public static enum SubscriptionMode implements IsSerializable {
		autoAcceptAll, autoRejectAll, manual
	}

	// FIXME: Vicente... esto no deber�a estar aqu�... puedes preguntar a
	// rosterMaganer por el modo actual o as�...
	public static final SubscriptionMode DEF_SUBSCRIPTION_MODE = SubscriptionMode.manual;

	// FIXME: vjrj - documentaci�n: qu� significa este presence?
	public void acceptSubscription(Presence presence);

	// FIXME: vjrj - documentaci�n: igual que arriba
	public void cancelSubscriptor(XmppURI xmppURI);

	public 	void denySubscription(Presence presence);

	public SubscriptionMode getSubscriptionMode();

	public void onRosterReady(Listener<XRoster> slot);

	public void onSubscriptionRequested(Listener<Presence> listener);

	public void onUnsubscribedReceived(Listener<XmppURI> listener);

	// FIXME: vjrj - documentaci�n: �por qu� si existe un requestAddItem
	// no existe un onItemAdded?
	// FIXME: �no deber�a estar este m�todo en el Roster?
	/**
	 * 7.4. Adding a Roster Item
	 * 
	 * At any time, a user MAY add an item to his or her roster.
	 * 
	 * @param the
	 *            JID of the user you want to add
	 * @param name
	 * @param group
	 * @see http://www.xmpp.org/rfcs/rfc3921.html#roster
	 */
	public void requestAddItem(XmppURI jid, String name, String group);

	// FIXME: lo mismo que arriba
	/**
	 * 7.4. Adding a Roster Item
	 * 
	 * At any time, a user MAY add an item to his or her roster.
	 * 
	 * @param JID
	 * @param name
	 * @param group
	 * @see http://www.xmpp.org/rfcs/rfc3921.html#roster
	 */
	public 	void requestRemoveItem(XmppURI xmppURI);

	/**
	 * A request to subscribe to another entity's presence is made by sending a
	 * presence stanza of type "subscribe". If the subscription request is being
	 * sent to an instant messaging contact, the JID supplied in the 'to'
	 * attribute SHOULD be of the form <contact@example.org> rather than
	 * <contact@example.org/resource>
	 * 
	 */
	public void requestSubscribe(XmppURI to);

	/**
	 * If a user would like to unsubscribe from the presence of another entity,
	 * it sends a presence stanza of type "unsubscribe".
	 */
	public void requestUnsubscribe(XmppURI to);

	public void setSubscriptionMode(SubscriptionMode mode);

}
