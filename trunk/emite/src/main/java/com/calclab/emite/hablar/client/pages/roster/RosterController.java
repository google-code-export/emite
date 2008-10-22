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
import com.calclab.suco.client.listener.Listener;
import com.calclab.suco.client.listener.Listener2;

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
		manager.openChat(item.getJID());
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
