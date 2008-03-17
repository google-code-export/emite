package com.calclab.emite.client.im.roster;

import com.calclab.emite.client.Engine;
import com.calclab.emite.client.packet.Event;
import com.calclab.emite.client.packet.stanza.Presence;
import com.calclab.emite.client.plugin.Plugin;
import com.calclab.emite.client.subscriber.EventSubscriber;
import com.calclab.emite.client.subscriber.PresenceSubscriber;

public class RosterPlugin implements Plugin {
	private static final String COMPONENT_NAME = "roster";

	public static Roster getRoster(final Engine engine) {
		return (Roster) engine.getComponent(COMPONENT_NAME);
	}

	public void start(final Engine engine) {
		// TODO: mejorar esto... pasar el engine en los listeners?
		final Roster roster = new Roster(engine);
		engine.register(COMPONENT_NAME, roster);

		engine.addListener(new EventSubscriber("session:success") {
			@Override
			protected void handleEvent(final Event event) {
				roster.onSessionStarted();
			}
		});

		engine.addListener(new PresenceSubscriber() {
			@Override
			protected void handlePresence(final Presence presence) {
				roster.onPresence();
			}
		});

	}

}
