package com.calclab.emite.widgets.client.login;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;

import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.session.Session.State;
import com.calclab.suco.client.signal.Slot;
import com.calclab.suco.client.signal.Slot0;
import com.calclab.suco.client.signal.Slot2;

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

	widget.onLogin.add(new Slot2<String, String>() {
	    public void onEvent(final String jid, final String password) {
		session.login(uri(jid), password);
	    }
	});

	widget.onLogout.add(new Slot0() {
	    public void onEvent() {
		session.logout();
	    }
	});

	session.onStateChanged(new Slot<Session.State>() {
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
