package com.calclab.emite.client.xmpp.session;

import static com.calclab.emite.client.core.dispatcher.matcher.Matchers.when;

import com.calclab.emite.client.components.Installable;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import static com.calclab.emite.client.xmpp.stanzas.XmppURI.*;

public class SessionComponent implements Installable {
    protected final Emite emite;
    protected XmppURI userURI;

    public SessionComponent(final Emite emite) {
	this.emite = emite;
	this.userURI = null;
    }

    public void install() {
	emite.subscribe(when(SessionManager.Events.onLoggedOut), new PacketListener() {
	    public void handle(final IPacket received) {
		loggedOut();
	    }
	});
	emite.subscribe(when(SessionManager.Events.onLoggedIn), new PacketListener() {
	    public void handle(final IPacket received) {
		loggedIn(uri(received.getAttribute("uri")));
	    }
	});
    }

    public void loggedIn(final XmppURI uri) {
	this.userURI = uri;
    }

    public void loggedOut() {
	this.userURI = null;
    }
}
