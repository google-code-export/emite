/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.hablar.client.pages.roster;

import java.util.Collection;
import java.util.HashMap;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.hablar.client.pages.roster.RosterItemView.RosterItemViewListener;
import com.calclab.emite.hablar.client.pages.roster.SubscriptionRequestedPanel.SubscriptionRequestedListener;
import com.calclab.emite.im.client.chat.ChatManager;
import com.calclab.emite.im.client.roster.Roster;
import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.emite.im.client.roster.SubscriptionManager;
import com.calclab.suco.client.events.Listener;
import com.calclab.suco.client.events.Listener2;

public class RosterController {
    private final RosterView view;
    private final HashMap<XmppURI, RosterItemView> itemViewsByJID;
    private final ChatManager manager;

    public RosterController(final Session session, final Roster roster, final SubscriptionManager subscriptor,
	    final ChatManager manager, final RosterView view) {
	this.manager = manager;
	this.itemViewsByJID = new HashMap<XmppURI, RosterItemView>();

	this.view = view;
	view.setToolbarVisible(false);

	view.onAddItem(new Listener<String>() {
	    public void onEvent(final String parameter) {
		final XmppURI jid = XmppURI.uri(parameter);
		roster.addItem(jid, jid.getNode());
	    }
	});

	subscriptor.onSubscriptionRequested(new Listener2<XmppURI, String>() {
	    public void onEvent(final XmppURI jid, final String nick) {
		final SubscriptionRequestedPanel notification = new SubscriptionRequestedPanel(jid, nick,
			new SubscriptionRequestedListener() {
			    public void accepted(final SubscriptionRequestedPanel panel, final XmppURI jid,
				    final String nick) {
				subscriptor.approveSubscriptionRequest(jid, nick);
				view.removeNotification(panel);
			    }

			    public void rejected(final SubscriptionRequestedPanel panel, final XmppURI jid) {
				subscriptor.refuseSubscriptionRequest(jid);
				view.removeNotification(panel);
			    }
			});
		view.addNotification(notification);
	    }
	});

	roster.onRosterRetrieved(new Listener<Collection<RosterItem>>() {
	    public void onEvent(final Collection<RosterItem> items) {
		for (final RosterItem item : items) {
		    addItem(item);
		}
	    }
	});

	roster.onItemAdded(new Listener<RosterItem>() {
	    public void onEvent(final RosterItem item) {
		addItem(item);
	    }
	});

	roster.onItemChanged(new Listener<RosterItem>() {
	    public void onEvent(final RosterItem item) {
		final RosterItemView itemView = itemViewsByJID.get(item.getJID());
		if (itemView != null) {
		    itemView.setItem(item);
		}
	    }
	});

	roster.onItemRemoved(new Listener<RosterItem>() {
	    public void onEvent(final RosterItem item) {
		final RosterItemView itemView = itemViewsByJID.get(item.getJID());
		if (itemView != null) {
		    view.removeItem(itemView);
		}
	    }
	});

	session.onStateChanged(new Listener<State>() {
	    public void onEvent(final State state) {
		if (state == State.disconnected) {
		    clearRoster(view);
		    view.setToolbarVisible(false);
		} else if (state == State.ready) {
		    view.setToolbarVisible(true);
		}
	    }
	});

    }

    private void addItem(final RosterItem item) {
	final RosterItemView itemView = new RosterItemView(item, new RosterItemViewListener() {
	    public void onAction(final RosterItemView view) {
		manager.open(item.getJID());
	    }
	});
	view.addItem(itemView);
	itemViewsByJID.put(item.getJID(), itemView);
    }

    private void clearRoster(final RosterView view) {
	itemViewsByJID.clear();
	view.clearItems();
    }

}
