package com.calclab.emite.j2se.swing.login;

import java.util.Date;

import com.calclab.emite.core.client.bosh.Bosh3Settings;
import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.Presence;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.emite.im.client.presence.PresenceManager;
import com.calclab.emite.j2se.swing.ConnectionConfiguration;
import com.calclab.emite.j2se.swing.LoginPanel;
import com.calclab.emite.j2se.swing.LoginPanel.LoginParams;
import com.calclab.suco.client.listener.Listener;
import com.calclab.suco.client.listener.Listener0;

public class LoginControl {

    private final Connection connection;
    private final Session session;
    private final PresenceManager presenceManager;

    public LoginControl(final Connection connection, final Session session, final PresenceManager presenceManager) {
	this.connection = connection;
	this.session = session;
	this.presenceManager = presenceManager;
    }

    public void setView(final LoginPanel loginPanel) {
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
		presenceManager.setOwnPresence(Presence.build("do not disturb at: " + new Date().toString(),
			Presence.Show.dnd));
	    }
	});

	loginPanel.onLogout(new Listener0() {
	    public void onEvent() {
		session.logout();

	    }
	});

	session.onStateChanged(new Listener<Session.State>() {
	    public void onEvent(final Session.State current) {
		final boolean isConnected = current == Session.State.ready;
		loginPanel.showState("state: " + current.toString(), isConnected);
		if (current == Session.State.notAuthorized) {
		    loginPanel.showMessage("lo siento, tienes mal la contraseña o el usuario");
		}
	    }
	});

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
