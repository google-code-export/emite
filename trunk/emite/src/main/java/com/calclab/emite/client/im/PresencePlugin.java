package com.calclab.emite.client.im;

import com.calclab.emite.client.Engine;
import com.calclab.emite.client.im.session.SessionPlugin;
import com.calclab.emite.client.packet.Event;
import com.calclab.emite.client.packet.stanza.Presence;
import com.calclab.emite.client.plugin.Plugin;
import com.calclab.emite.client.subscriber.EventSubscriber;

public class PresencePlugin implements Plugin {

	public void start(final Engine engine) {
		engine.addListener(new EventSubscriber(SessionPlugin.SUCCESS) {
			@Override
			protected void handleEvent(final Event event) {
				onSessionStarted(engine);
			}
		});
	}

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
	 * @param engine
	 */
	private void onSessionStarted(final Engine engine) {
		final Presence presence = new Presence(engine.getGlobal(Engine.JID));
		presence.setShow(Presence.SHOW_CHAT);
		engine.send(presence);
	}

}
