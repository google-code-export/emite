package com.calclab.examples.emite.echo.client;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.stanzas.Message;
import com.calclab.suco.client.listener.Listener;

/**
 * Echo respond to any incoming message with the same message body.
 */
public class Echo {
    public Echo(final Session session) {
	session.onMessage(new Listener<Message>() {
	    public void onEvent(final Message incoming) {
		session.send(new Message(incoming.getBody(), incoming.getFrom()));
	    }
	});
    }
}
