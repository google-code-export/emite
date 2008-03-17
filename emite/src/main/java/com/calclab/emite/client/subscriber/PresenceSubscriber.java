package com.calclab.emite.client.subscriber;

import com.calclab.emite.client.matcher.BasicMatcher;
import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.packet.stanza.Presence;

public abstract class PresenceSubscriber extends AbstractPacketSubscriber {

	public PresenceSubscriber() {
		super(new BasicMatcher("presence", null, null));
	}

	@Override
	public void handle(final Packet stanza) {
		this.handlePresence(new Presence(stanza));
	}

	protected abstract void handlePresence(Presence presence);

}
