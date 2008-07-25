package com.calclab.widgets.comenta.client;

import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.signal.Slot;
import com.calclab.suco.client.signal.Slot2;
import com.calclab.widgets.comenta.client.ui.ComentaWidget;

public class ComentaController {

    private final Session session;
    private ComentaWidget widget;

    public ComentaController(final Session session) {
	this.session = session;

    }

    public void setWidget(final ComentaWidget widget) {
	assert this.widget == null;

	this.widget = widget;

	session.onStateChanged(new Slot<Session.State>() {
	    public void onEvent(final Session.State state) {
		widget.showStatus(state.toString());
	    }
	});

	widget.onLogin(new Slot2<String, String>() {
	    public void onEvent(final String name, final String password) {
		session.login(XmppURI.uri(name), password);
	    }
	});
	widget.showStatus("Please login.");
    }
}
