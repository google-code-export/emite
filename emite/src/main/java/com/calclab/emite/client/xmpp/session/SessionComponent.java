package com.calclab.emite.client.xmpp.session;

import static com.calclab.emite.client.core.dispatcher.matcher.Matchers.when;
import static com.calclab.emite.client.xmpp.stanzas.XmppURI.uri;

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.dispatcher.PacketListener;
import com.calclab.emite.client.core.packet.IPacket;
import com.calclab.emite.client.xmpp.stanzas.XmppURI;

/**
 * An base class to extend if you need components with the current user uri as
 * state information.
 * 
 * @author dani
 * 
 */
public class SessionComponent {
    protected final Emite emite;
    protected XmppURI userURI;

    public SessionComponent(final Emite emite) {
	this.emite = emite;
	this.userURI = null;
	install();
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

    private void install() {
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
}
