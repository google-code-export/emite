package com.calclab.emite.j2se.swing.login;

import com.calclab.emite.core.client.bosh.Bosh3Settings;
import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.listener.Listener;
import com.calclab.suco.client.listener.Listener0;

public class LoginControl {

    public LoginControl(final Connection connection, final Session session, final LoginPanel loginPanel) {

	loginPanel.onLogin(new Listener<LoginParams>() {
	    public void onEvent(final LoginParams p) {
		final String resource = "emite-swing";
		connection.setSettings(new Bosh3Settings(p.httpBase, p.domain));
		XmppURI uri;
		String password = p.password;
		if ("anonymous".equals(p.userName)) {
		    uri = Session.ANONYMOUS;
		    password = null;
		} else {
		    uri = XmppURI.uri(p.userName, p.domain, resource);
		}
		session.login(uri, password);
	    }
	});

	loginPanel.onLogout(new Listener0() {
	    public void onEvent() {
		session.logout();
	    }
	});

	session.onStateChanged(new Listener<Session.State>() {
	    public void onEvent(final Session.State current) {
		loginPanel.showState(current.toString());
		switch (current) {
		case connecting:
		    loginPanel.setConnected(true);
		    break;
		case notAuthorized:
		    loginPanel.showMessage("lo siento, tienes mal la contraseña o el usuario");
		    break;
		case ready:
		    loginPanel.showState("connected as " + session.getCurrentUser());
		    break;
		case disconnected:
		    loginPanel.setConnected(false);
		}
	    }
	});

	addConfigurations(loginPanel);
    }

    private void addConfigurations(final LoginPanel loginPanel) {
	loginPanel.addConfiguration(new ConnectionConfiguration("empty", "", "", "", ""));
	loginPanel.addConfiguration(new ConnectionConfiguration("admin @ local openfire",
		"http://localhost:5280/http-bind/", "localhost", "admin", "easyeasy"));
	loginPanel.addConfiguration(new ConnectionConfiguration("dani @ local ejabberd",
		"http://localhost:5280/http-bind/", "localhost", "dani", "dani"));
	loginPanel.addConfiguration(new ConnectionConfiguration("dani @ emite demo",
		"http://emite.ourproject.org/proxy", "emitedemo.ourproject.org", "dani", "dani"));
	loginPanel.addConfiguration(new ConnectionConfiguration("test1 @ jetty proxy",
		"http://localhost:4444/http-bind", "localhost", "test1", "test1"));
	loginPanel.addConfiguration(new ConnectionConfiguration("test1 @ jetty bosh servlet",
		"http://emite.ourproject.org/proxy", "localhost", "test1", "test1"));
    }
}
