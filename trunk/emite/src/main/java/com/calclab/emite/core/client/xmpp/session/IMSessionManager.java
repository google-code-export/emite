package com.calclab.emite.core.client.xmpp.session;

import com.calclab.emite.core.client.bosh.Connection;
import com.calclab.emite.core.client.packet.IPacket;
import com.calclab.emite.core.client.xmpp.stanzas.IQ;
import com.calclab.emite.core.client.xmpp.stanzas.XmppURI;
import com.calclab.suco.client.events.Event;
import com.calclab.suco.client.events.Listener;

/**
 * Handle the IM session request.
 * 
 * @see http://www.xmpp.org/extensions/xep-0206.html#preconditions-sasl
 */
public class IMSessionManager {
    private final Connection connection;
    private final Event<XmppURI> onSessionCreated;

    public IMSessionManager(final Connection connection) {
	this.connection = connection;
	this.onSessionCreated = new Event<XmppURI>("sessionManager:onSessionCreated");

	connection.onStanzaReceived(new Listener<IPacket>() {
	    public void onEvent(final IPacket stanza) {
		if ("im-session-request".equals(stanza.getAttribute("id"))) {
		    onSessionCreated.fire(XmppURI.uri(stanza.getAttribute("to")));
		}
	    }

	});
    }

    public void onSessionCreated(final Listener<XmppURI> listener) {
	onSessionCreated.add(listener);
    }

    public void requestSession(final XmppURI uri) {
	final IQ iq = new IQ(IQ.Type.set, uri.getHostURI());
	iq.setFrom(uri);
	iq.setAttribute("id", "im-session-request");
	iq.Includes("session", "urn:ietf:params:xml:ns:xmpp-session");

	connection.send(iq);
    }
}
