package com.calclab.emite.widgets.client.roster;

import java.util.Collection;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.emite.im.client.roster.Roster;
import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.suco.client.listener.Listener;

public class RosterController {

    private RosterWidget widget;
    private final Session session;
    private final Roster roster;

    public RosterController(final Session session, final Roster roster) {
	this.session = session;
	this.roster = roster;
    }

    public void setWidget(final RosterWidget widget) {
	this.widget = widget;
	widget.setDisconnected();

	roster.onRosterRetrieved(new Listener<Collection<RosterItem>>() {
	    public void onEvent(final Collection<RosterItem> parameter) {
		handleRosterReception(roster);
	    }
	});

	session.onStateChanged(new Listener<State>() {
	    public void onEvent(final State state) {
		if (state == State.disconnected) {
		    widget.setDisconnected();
		}
	    }
	});
    }

    private void handleRosterReception(final Roster roster) {
	setItems(roster.getItems());

	// FIXME: new roster implementation
	roster.onItemUpdated(new Listener<RosterItem>() {
	    public void onEvent(final RosterItem item) {
		Log.debug("(widget) Roster item updated: " + item);
	    }
	});
	roster.onItemAdded(new Listener<RosterItem>() {
	    public void onEvent(final RosterItem item) {
		Log.debug("(widget) Roster item added: " + item);
	    }
	});
	roster.onItemRemoved(new Listener<RosterItem>() {
	    public void onEvent(final RosterItem item) {
		Log.debug("(widget) Roster item removed: " + item);
	    }
	});
    }

    private void setItems(final Collection<RosterItem> items) {
	Log.debug("Adding roster items: " + items.size());
	widget.clearItems();
	for (final RosterItem item : items) {
	    widget.addItem(item.getJID());
	}
    }
}
