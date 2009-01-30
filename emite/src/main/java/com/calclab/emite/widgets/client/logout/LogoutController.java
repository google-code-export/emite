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
