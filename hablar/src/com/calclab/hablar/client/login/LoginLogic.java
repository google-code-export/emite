package com.calclab.hablar.client.login;

import com.calclab.emite.browser.client.PageAssist;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.events.Listener;

public class LoginLogic {
    private final Session session;
    private final LoginWidget widget;

    public LoginLogic(final LoginWidget widget) {
	this.widget = widget;
	this.session = Suco.get(Session.class);

	String userName = PageAssist.getMeta("hablar.user");
	widget.setUserName(userName);
	
	session.onStateChanged(new Listener<State>() {
	    @Override
	    public void onEvent(State state) {
		setState(state);
	    }
	});
	setState(session.getState());
    }
    
    private void setState(State state) {
	if (state == State.ready) {
	    widget.setActionText("Logout");
	    widget.setActionEnabled(true);
	    String userName = session.getCurrentUser().getNode();
	    setStatus("Connected as " + userName);
	} else if (state == State.disconnected) {
	    widget.setActionText("Login");
	    widget.setActionEnabled(true);
	    setStatus("Disconnected");
	} else {
	    widget.setActionText("Wait...");
	    widget.setActionEnabled(false);
	}
	widget.addMessage("Session state: " + state);
    }

    private void setStatus(String status) {
	widget.setStatus(status);
	widget.setHeaderTitle(status);
    }

    public void onAction() {
	State state = session.getState();
	if (state == State.disconnected) {
	    XmppURI uri = getUri();
	    if (uri != null) {
		session.login(uri, widget.getPassword());
	    }
	} else if (state == State.ready) {
	    session.logout();
	}
    }

    // FIXME
    private XmppURI getUri() {
	try {
	    return XmppURI.uri(widget.getUserName());
	} catch (RuntimeException e) {
	    return null;
	}
    }

}