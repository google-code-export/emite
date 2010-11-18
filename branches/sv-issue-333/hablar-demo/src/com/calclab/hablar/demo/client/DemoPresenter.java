package com.calclab.hablar.demo.client;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.events.Listener;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

/**
 * The Presenter of the MVP (Model-View-Presenter) pattern
 * 
 */
public class DemoPresenter {

    private final Session session;
    private final DemoDisplay display;

    public DemoPresenter(final DemoDisplay display) {
	this.display = display;
	session = Suco.get(Session.class);
	session.onStateChanged(new Listener<Session>() {
	    @Override
	    public void onEvent(final Session session) {
		showState(session.getState());
	    }
	});

	display.getLogin().addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(final ClickEvent event) {
		login();
	    }
	});

	display.getLogout().addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(final ClickEvent event) {
		logout();
	    }
	});
	showState(session.getState());
    }

    private void login() {
	display.getState().setText("Starting login process...");
	final String userName = display.getUserName().getText();
	final String password = display.getPassword().getText();
	session.login(XmppURI.uri(userName), password);
    }

    private void logout() {
	session.logout();
    }

    private void showState(final State state) {
	display.setLoginEnabled(state == State.disconnected);
	display.setLogoutEnabled(state == State.ready);
	display.getState().setText("Session: " + state);
    }

}
