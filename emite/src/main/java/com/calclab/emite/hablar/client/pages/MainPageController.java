package com.calclab.emite.hablar.client.pages;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.listener.Listener;
import com.calclab.suco.client.listener.Listener0;
import com.calclab.suco.client.listener.Listener2;

public class MainPageController {

    public MainPageController(final Session session, final MainPage view) {

	view.onLogin(new Listener2<String, String>() {
	    public void onEvent(final String user, final String password) {
		setState(view, false);
		session.login(XmppURI.uri(user), password);
	    }
	});

	view.onLogout(new Listener0() {
	    public void onEvent() {
		session.logout();
	    }
	});

	session.onStateChanged(new Listener<Session.State>() {
	    public void onEvent(final State state) {
		view.show("status: " + state);
		if (state == State.disconnected) {
		    setState(view, true);
		}
	    }
	});

	setState(view, true);
    }

    private void setState(final MainPage view, final boolean isLogin) {
	view.show("");
	view.setLoginEnabled(isLogin);
	view.setLogoutEnabled(!isLogin);
	view.showContent(isLogin ? 1 : 0);
    }

}
