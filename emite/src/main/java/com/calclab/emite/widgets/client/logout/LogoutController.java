package com.calclab.emite.widgets.client.logout;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.suco.client.events.Listener;
import com.calclab.suco.client.events.Listener0;

public class LogoutController {
    private final Session session;

    public LogoutController(final Session session) {
	this.session = session;
    }

    public void setWidget(final LogoutWidget widget) {
	showNotLoggedIn(widget);
	session.onStateChanged(new Listener<State>() {
	    public void onEvent(final State state) {
		if (state == State.disconnected) {
		    showNotLoggedIn(widget);
		} else if (state == State.ready) {
		    showLoggedIn(widget);
		}
	    }
	});

	widget.onLogout.add(new Listener0() {
	    public void onEvent() {
		session.logout();
	    }
	});
    }

    private void showLoggedIn(final LogoutWidget widget) {
	widget.showMessage(session.getCurrentUser().getJID().toString());
	widget.setButtonVisible(true);
    }

    private void showNotLoggedIn(final LogoutWidget widget) {
	widget.showMessage("Not logged in.");
	widget.setButtonVisible(false);
    }

}
