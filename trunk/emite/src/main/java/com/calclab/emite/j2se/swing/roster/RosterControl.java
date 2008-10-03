package com.calclab.emite.j2se.swing.roster;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;

import java.util.Collection;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.roster.Roster;
import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.emite.im.client.roster.SubscriptionManager;
import com.calclab.suco.client.listener.Listener;
import com.calclab.suco.client.listener.Listener2;

public class RosterControl {

    public RosterControl(final Session session, final Roster roster, final SubscriptionManager subscriptionManager,
	    final RosterPanel rosterPanel) {

	rosterPanel.setEnabled(false);

	rosterPanel.onAddRosterItem(new Listener2<String, String>() {
	    public void onEvent(final String jid, final String name) {
		roster.addItem(uri(jid), name);
	    }
	});

	rosterPanel.onRemoveItem(new Listener<RosterItem>() {
	    public void onEvent(final RosterItem item) {
		roster.removeItem(item.getJID());
	    }
	});

	roster.onRosterRetrieved(new Listener<Collection<RosterItem>>() {
	    public void onEvent(final Collection<RosterItem> items) {
		rosterPanel.clear();
		for (final RosterItem item : items) {
		    rosterPanel.addItem(item.getName(), item);
		}
	    }
	});

	roster.onItemAdded(new Listener<RosterItem>() {
	    public void onEvent(final RosterItem item) {
		rosterPanel.addItem(item.getName(), item);
	    }
	});

	roster.onItemChanged(new Listener<RosterItem>() {
	    public void onEvent(final RosterItem parameter) {
		rosterPanel.refresh();
	    }
	});

	roster.onItemRemoved(new Listener<RosterItem>() {
	    public void onEvent(final RosterItem parameter) {
	    }
	});

	session.onStateChanged(new Listener<Session.State>() {
	    public void onEvent(final Session.State current) {
		rosterPanel.setEnabled(current == Session.State.ready);
		if (current == Session.State.disconnected) {
		    rosterPanel.clear();
		}
	    }
	});

	subscriptionManager.onSubscriptionRequested(new Listener2<XmppURI, String>() {
	    public void onEvent(final XmppURI uri, final String nick) {
		final String message = uri.toString() + " want to add you to his/her roster. Accept?";
		if (rosterPanel.isConfirmed(message)) {
		    subscriptionManager.approveSubscriptionRequest(uri, nick);
		} else {
		    subscriptionManager.refuseSubscriptionRequest(uri);
		}
	    }
	});

    }
}
