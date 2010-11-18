package com.calclab.emite.js.client;

import static com.calclab.emite.core.client.xmpp.stanzas.XmppURI.uri;

import org.timepedia.exporter.client.Export;
import org.timepedia.exporter.client.ExportPackage;
import org.timepedia.exporter.client.Exportable;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.packet.gwt.GWTPacket;
import com.calclab.emite.core.client.services.Services;
import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.Session.State;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.events.Listener;
import com.google.gwt.xml.client.Element;

@Export
@ExportPackage("emitexmpp")
public class Emite implements Exportable {
    private final Session session;
    private final Services services;

    public Emite() {
	Log.debug("Emite facade!");
	this.session = Suco.get(Session.class);
	this.services = Suco.get(Services.class);
    }

    public String getState() {
	return session.getState().toString();
    }

    public void log(final String message) {
	Log.debug(message);
    }

    public void login(final String uri, final String password) {
	session.login(uri(uri), password);
    }

    public void logout() {
	session.logout();
    }

    public void onReceive(final Callback callback) {
	Suco.get(Connection.class).onStanzaReceived(new Listener<IPacket>() {
	    public void onEvent(final IPacket stanza) {
		final Element element = ((GWTPacket) stanza).getElement();
		callback.onEvent(element);
	    }
	});
    }

    public void onStateChanged(final Callback callback) {
	Log.debug("Adding state callback" + callback);
	session.onStateChanged(new Listener<State>() {
	    public void onEvent(final State state) {
		Log.debug("Callback state: " + state);
		callback.onEvent(state.toString());
	    }
	});
    }

    public void send(final String xml) {
	session.send(services.toXML(xml));
    }
}
