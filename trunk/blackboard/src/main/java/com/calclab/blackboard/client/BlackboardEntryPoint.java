package com.calclab.blackboard.client;

import com.calclab.blackboard.client.Controls.ControlsListener;
import com.calclab.emite.client.Xmpp;
import com.calclab.emite.client.core.bosh.BoshOptions;
import com.calclab.emite.client.xmpp.session.SessionListener;
import com.calclab.emite.client.xmpp.session.Session.State;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;

public class BlackboardEntryPoint implements EntryPoint {

    private Xmpp xmpp;
    private String domain;
    private Blackboard blackboard;
    private Controls controls;

    public void onModuleLoad() {
	blackboard = new Blackboard();

	controls = new Controls(new ControlsListener() {
	    public void onLogin() {
		final XmppURI uri = new XmppURI(null, domain, "blackboard-" + System.currentTimeMillis());
		blackboard.add(null, "login: " + uri.toString());
		xmpp.login(uri, null, null, null);
	    }
	});
	controls.setLoggedIn(false);

	RootPanel.get("blackboard").add(blackboard);
	RootPanel.get("controls").add(controls);

	blackboard.add(null, "welcome to blackboard");
	blackboard.add(null, "an emite example application");

	final String httpBase = DOM.getElementProperty(DOM.getElementById("emite-httpbase"), "content");
	domain = DOM.getElementProperty(DOM.getElementById("emite-domain"), "content");
	blackboard.add(null, "using http-base: " + httpBase);
	blackboard.add(null, "using domain: " + domain);
	createXmpp(httpBase);

    }

    private void createXmpp(final String httpBase) {
	xmpp = Xmpp.create(new BoshOptions(httpBase));
	xmpp.getSession().addListener(new SessionListener() {
	    public void onStateChanged(final State old, final State current) {
		blackboard.add(null, "state: " + current.toString());
		if (current == State.connected) {
		    controls.setLoggedIn(true);
		} else if (current == State.disconnected) {
		    controls.setLoggedIn(false);
		}
	    }
	});
    }

}
