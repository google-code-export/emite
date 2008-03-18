package com.calclab.emite.client.x.im;

import com.calclab.emite.client.IGlobals;
import com.calclab.emite.client.packet.stanza.Presence;
import com.calclab.emite.client.plugin.FilterBuilder;
import com.calclab.emite.client.plugin.Plugin2;
import com.calclab.emite.client.x.im.session.SessionPlugin;

public class PresencePlugin implements Plugin2 {
	final Presence initialPresence;

	/**
	 * 5.1.1. Initial Presence
	 * 
	 * After establishing a session, a client SHOULD send initial presence to
	 * the server in order to signal its availability for communications. As
	 * defined herein, the initial presence stanza (1) MUST possess no 'to'
	 * address (signalling that it is meant to be broadcasted by the server on
	 * behalf of the client) and (2) MUST possess no 'type' attribute
	 * (signalling the user's availability). After sending initial presence, an
	 * active resource is said to be an "available resource"
	 * 
	 * @link http://www.xmpp.org/rfcs/rfc3921.html#presence
	 * 
	 */

	public PresencePlugin(final IGlobals globals) {
		this.initialPresence = new Presence(globals.getJID()).With(Presence.Show.chat);
	}

	public void start(final FilterBuilder when, final com.calclab.emite.client.IContainer components) {
		when.Event(SessionPlugin.Events.started).send(initialPresence);
	};

}
