package com.calclab.emite.widgets.client.login;

import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.session.Session.State;
import com.calclab.suco.client.signal.Slot;
import com.calclab.suco.client.signal.Slot0;
import com.calclab.suco.client.signal.Slot2;
import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;

public class LoginController {
    private final Session session;

    public LoginController(final Session session) {
	this.session = session;
    }

    public void setWidget(final LoginWidget widget) {
	setLoginEnabled(widget, true);

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
		if (state == State.disconnected) {
		    setLoginEnabled(widget, true);
		    widget.showMessage("Please login.");
		} else if (state == State.ready) {
		    setLoginEnabled(widget, false);
		    widget.showMessage("Logged as: " + session.getCurrentUser().getJID().toString());
		}
	    }

	});
    }

    private void setLoginEnabled(final LoginWidget widget, final boolean enabled) {
	widget.setLoginEnabled(enabled);
	widget.setFieldsEnabled(enabled);
	widget.setLogoutEnabled(!enabled);
    }

}
