package com.calclab.uimite.client.chat;

import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.session.SessionListener;
import com.calclab.emite.client.xmpp.session.Session.State;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.uimite.client.chat.StatusView.StatusViewListener;

public class StatusPresenter {

    public StatusPresenter(final XmppURI uri, final String password, final Session session, final StatusView view) {
	session.addListener(new SessionListener() {
	    public void onStateChanged(final State old, final State current) {
		view.showState("state: " + current);
	    }
	});

	view.addListener(new StatusViewListener() {
	    public void onStatusChanged(final String status) {
		session.login(uri, password);
	    }
	});
    }

}
