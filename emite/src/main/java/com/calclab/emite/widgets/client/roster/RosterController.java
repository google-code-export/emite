package com.calclab.emite.widgets.client.roster;

import java.util.Collection;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.im.roster.Roster;
import com.calclab.emite.client.im.roster.RosterItem;
import com.calclab.emite.client.im.roster.RosterManager;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.session.Session.State;
import com.calclab.suco.client.signal.Slot;

public class RosterController {

    private final RosterManager manager;
    private RosterWidget widget;
    private final Session session;

    public RosterController(final Session session, final RosterManager manager) {
	this.session = session;
	this.manager = manager;
    }

    public void setWidget(final RosterWidget widget) {
	this.widget = widget;
	widget.setDisconnected();
	manager.onRosterReady(new Slot<Roster>() {
	    public void onEvent(final Roster roster) {
		setRoster(roster);
	    }
	});

	session.onStateChanged(new Slot<State>() {
	    public void onEvent(final State state) {
		if (state == State.disconnected) {
		    widget.setDisconnected();
		}
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

    private void setRoster(final Roster roster) {
	setItems(roster.getItems());

	roster.onItemChanged(new Slot<RosterItem>() {
	    public void onEvent(final RosterItem item) {
		Log.debug("(widget) Roster item changed: " + item);
	    }
	});
	roster.onRosterChanged(new Slot<Collection<RosterItem>>() {
	    public void onEvent(final Collection<RosterItem> items) {
		Log.debug("(widget) Roster changed: " + items);
		setItems(items);
	    }
	});
    }

}
