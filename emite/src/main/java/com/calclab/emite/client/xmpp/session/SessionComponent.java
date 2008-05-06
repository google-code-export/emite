package com.calclab.emite.client.xmpp.session;

import static com.calclab.emite.client.core.dispatcher.matcher.Matchers.when;

import com.calclab.emite.client.components.Installable;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;
import static com.calclab.emite.client.xmpp.stanzas.XmppURI.*;

/**
 * An base class to extend if you need components with the current user uri as
 * state information.
 * 
 * @author dani
 * 
 */
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
		logOut();
	    }
	});
	emite.subscribe(when(SessionManager.Events.onLoggedIn), new PacketListener() {
	    public void handle(final IPacket received) {
		logIn(uri(received.getAttribute("uri")));
	    }
	});
    }

    public boolean isLoggedIn() {
	return this.userURI != null;
    }

    public void logIn(final XmppURI uri) {
	this.userURI = uri;
    }

    public void logOut() {
	this.userURI = null;
    }
}
