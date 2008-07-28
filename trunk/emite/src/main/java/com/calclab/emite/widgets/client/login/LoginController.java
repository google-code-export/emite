package com.calclab.emite.widgets.client.login;

import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;

import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.session.Session.State;
import com.calclab.suco.client.signal.Slot;
import com.calclab.suco.client.signal.Slot0;
import com.calclab.suco.client.signal.Slot2;

public class LoginController {
    private final Session session;

    public LoginController(final Session session) {
	this.session = session;
    }

    public void setWidget(final LoginWidget widget) {
	setLoginMode(widget);
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
		    setLogoutMode(widget);
		    widget.setButtonEnabled(true);
		    widget.showMessage("Logged as: " + session.getCurrentUser().getJID().toString());
		    break;
		case disconnected:
		    setLoginMode(widget);
		    widget.setButtonEnabled(true);
		    widget.showMessage("Please login.");
		    break;
		case notAuthorized:
		    widget.showError("Not authorized.");
		    break;
		case error:
		    widget.showError("Couldn't login.");
		    break;
		}
	    }
	});
    }

    private void setLoginMode(final LoginWidget widget) {
	widget.setButtonBehaviour(false, "login");
	widget.setFieldsEnabled(true);
    }

    private void setLogoutMode(final LoginWidget widget) {
	widget.setButtonBehaviour(true, "logout");
	widget.setFieldsEnabled(false);
    }

}
