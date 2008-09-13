package com.calclab.emite.widgets.client.roster;

import java.util.Collection;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.emite.im.client.xold_roster.XRoster;
import com.calclab.emite.im.client.xold_roster.XRosterItem;
import com.calclab.emite.im.client.xold_roster.XRosterManager;
import com.calclab.suco.client.signal.Slot;

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
	manager.onRosterReady(new Slot<XRoster>() {
	    public void onEvent(final XRoster xRoster) {
		setRoster(xRoster);
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

    private void setItems(final Collection<XRosterItem> items) {
	Log.debug("Adding roster items: " + items.size());
	widget.clearItems();
	for (final XRosterItem item : items) {
	    widget.addItem(item.getJID());
	}
    }

    private void setRoster(final XRoster xRoster) {
	setItems(xRoster.getItems());

	xRoster.onItemChanged(new Slot<XRosterItem>() {
	    public void onEvent(final XRosterItem item) {
		Log.debug("(widget) Roster item changed: " + item);
	    }
	});
	xRoster.onRosterChanged(new Slot<Collection<XRosterItem>>() {
	    public void onEvent(final Collection<XRosterItem> items) {
		Log.debug("(widget) Roster changed: " + items);
		setItems(items);
	    }
	});
    }

}
