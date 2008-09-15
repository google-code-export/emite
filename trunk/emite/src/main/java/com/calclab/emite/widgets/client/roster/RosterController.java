package com.calclab.emite.widgets.client.roster;

import java.util.Collection;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.emite.im.client.roster.RosterItem;
import com.calclab.emite.im.client.xold_roster.XRoster;
import com.calclab.emite.im.client.xold_roster.XRosterManager;
import com.calclab.suco.client.listener.Listener;

public class RosterController {

    private final XRosterManager manager;
    private RosterWidget widget;
    private final Session session;

    public RosterController(final Session session, final XRosterManager manager) {
	this.session = session;
	this.manager = manager;
    }

    public void setWidget(final RosterWidget widget) {
	this.widget = widget;
	widget.setDisconnected();
	manager.onRosterReady(new Listener<XRoster>() {
	    public void onEvent(final XRoster xRoster) {
		setRoster(xRoster);
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

    private void setItems(final Collection<RosterItem> items) {
	Log.debug("Adding roster items: " + items.size());
	widget.clearItems();
	for (final RosterItem item : items) {
	    widget.addItem(item.getJID());
	}
    }

    private void setRoster(final XRoster xRoster) {
	setItems(xRoster.getItems());

	xRoster.onItemChanged(new Listener<RosterItem>() {
	    public void onEvent(final RosterItem item) {
		Log.debug("(widget) Roster item changed: " + item);
	    }
	});
	xRoster.onRosterChanged(new Listener<Collection<RosterItem>>() {
	    public void onEvent(final Collection<RosterItem> items) {
		Log.debug("(widget) Roster changed: " + items);
		setItems(items);
	    }
	});
    }

}
