package com.calclab.emite.j2se.swing.roster;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;

import java.util.Collection;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.emite.im.client.xold_roster.XRoster;
import com.calclab.emite.im.client.xold_roster.XRosterManager;
import com.calclab.suco.client.signal.Slot;
import com.calclab.suco.client.signal.Slot2;

public class RosterControl {

    private final XRosterManager rosterManager;
    private final XRoster xRoster;
    private final Session session;

    public RosterControl(final Session session, final XRosterManager xRosterManager, final XRoster xRoster) {
	this.session = session;
	rosterManager = xRosterManager;
	this.xRoster = xRoster;
    }

    public void setView(final RosterPanel rosterPanel) {
	rosterPanel.onAddRosterItem(new Slot2<String, String>() {
	    public void onEvent(final String jid, final String name) {
		rosterManager.requestAddItem(uri(jid), name, null);
	    }
	});

	rosterPanel.onRemoveItem(new Slot<RosterItem>() {
	    public void onEvent(final RosterItem item) {
		rosterManager.requestRemoveItem(item.getJID());
	    }
	});

	xRoster.onItemChanged(new Slot<RosterItem>() {
	    public void onEvent(final RosterItem item) {
		rosterPanel.refresh();
	    }
	});
	xRoster.onRosterChanged(new Slot<Collection<RosterItem>>() {
	    public void onEvent(final Collection<RosterItem> items) {
		rosterPanel.clear();
		for (final RosterItem item : items) {
		    rosterPanel.add(item.getName(), item);
		}
	    }
	});

	session.onStateChanged(new Slot<Session.State>() {
	    public void onEvent(final Session.State current) {
		if (current == Session.State.disconnected) {
		    rosterPanel.clear();
		}
	    }
	});

	rosterManager.onSubscriptionRequested(new Slot<Presence>() {
	    public void onEvent(final Presence presence) {
		final String message = presence.getFromAsString() + " want to add you to his/her roster. Accept?";
		if (rosterPanel.isConfirmed(message)) {
		    rosterManager.acceptSubscription(presence);
		}
	    }
	});
    }

}
