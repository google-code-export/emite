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
package com.calclab.emite.client.im.roster;

import com.calclab.emite.client.xmpp.stanzas.Presence;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.signal.Slot;
import com.google.gwt.user.client.rpc.IsSerializable;

// FIXME: vjrj - necesito ayuda para documentar esta clase
// no entiendo EXACTAMENTE qué hace cada método
public interface RosterManager {
	public static enum SubscriptionMode implements IsSerializable {
		autoAcceptAll, autoRejectAll, manual
	}

	// FIXME: Vicente... esto no debería estar aquí... puedes preguntar a
	// rosterMaganer por el modo actual o así...
	public static final SubscriptionMode DEF_SUBSCRIPTION_MODE = SubscriptionMode.manual;

	// FIXME: vjrj - documentación: qué significa este presence?
	void acceptSubscription(Presence presence);

	// FIXME: vjrj - documentación: igual que arriba
	void cancelSubscriptor(XmppURI xmppURI);

	void denySubscription(Presence presence);

	SubscriptionMode getSubscriptionMode();

	void onRosterReady(Slot<Roster> slot);

	void onSubscriptionRequested(Slot<Presence> listener);

	void onUnsubscribedReceived(Slot<XmppURI> listener);

	// FIXME: vjrj - documentación: ¿por qué si existe un requestAddItem
	// no existe un onItemAdded?
	// FIXME: ¿no debería estar este método en el Roster?
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
	void requestAddItem(XmppURI jid, String name, String group);

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
	void requestRemoveItem(XmppURI xmppURI);

	/**
	 * A request to subscribe to another entity's presence is made by sending a
	 * presence stanza of type "subscribe". If the subscription request is being
	 * sent to an instant messaging contact, the JID supplied in the 'to'
	 * attribute SHOULD be of the form <contact@example.org> rather than
	 * <contact@example.org/resource>
	 * 
	 */
	void requestSubscribe(XmppURI to);

	/**
	 * If a user would like to unsubscribe from the presence of another entity,
	 * it sends a presence stanza of type "unsubscribe".
	 */
	void requestUnsubscribe(XmppURI to);

	void setSubscriptionMode(SubscriptionMode mode);

}
