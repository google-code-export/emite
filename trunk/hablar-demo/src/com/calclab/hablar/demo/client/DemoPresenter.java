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
    private final DemoView view;

    public DemoPresenter(final DemoView view) {
	this.view = view;
	session = Suco.get(Session.class);
	session.onStateChanged(new Listener<Session>() {
	    @Override
	    public void onEvent(Session session) {
		showState(session.getState());
	    }
	});

	view.getLogin().addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		login();
	    }
	});

	view.getLogout().addClickHandler(new ClickHandler() {
	    @Override
	    public void onClick(ClickEvent event) {
		logout();
	    }
	});
	showState(session.getState());
    }

    private void login() {
	String userName = view.getUserName().getText();
	String password = view.getPassword().getText();
	session.login(XmppURI.uri(userName), password);
    }

    private void logout() {
	session.logout();
    }

    private void showState(State state) {
	view.getState().setText("Session: " + state);
    }

}
