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
package com.calclab.emite.widgets.client.login;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.suco.client.events.Listener;
import com.calclab.suco.client.events.Listener0;
import com.calclab.suco.client.events.Listener2;

public class LoginController {
    private final Session session;
    private LoginWidget widget;

    public LoginController(final Session session) {
	this.session = session;
    }

    public void setWidget(final LoginWidget widget) {
	this.widget = widget;
	setLoggedIn(false);
	widget.showError(null);

	widget.onLogin.add(new Listener2<String, String>() {
	    public void onEvent(final String jid, final String password) {
		session.login(uri(jid), password);
	    }
	});

	widget.onLogout.add(new Listener0() {
	    public void onEvent() {
		session.logout();
	    }
	});

	session.onStateChanged(new Listener<Session.State>() {
	    public void onEvent(final State state) {
		widget.showMessage(state.toString());

		switch (state) {
		case connecting:
		    widget.showError(null);
		    break;
		case ready:
		    setLoggedIn(true);
		    widget.setButtonEnabled(true);
		    widget.showMessage("Logged as: " + session.getCurrentUser().getJID().toString());
		    break;
		case disconnected:
		    setLoggedIn(false);
		    widget.setButtonEnabled(true);
		    widget.showMessage("Please login.");
		    break;
		case notAuthorized:
		    widget.showError("Not authorized.");
		    break;
		case error:
		    break;
		}
	    }
	});
    }

    private void setLoggedIn(final boolean isLoggedIn) {
	final String actionLabel = isLoggedIn ? "logout" : "login";
	widget.setButtonBehaviour(isLoggedIn, actionLabel);
	widget.setFieldsEnabled(!isLoggedIn);
    }

}
