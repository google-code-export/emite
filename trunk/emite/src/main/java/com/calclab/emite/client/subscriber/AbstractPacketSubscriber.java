package com.calclab.emite.client.subscriber;

import com.calclab.emite.client.matcher.Matcher;
import com.calclab.emite.client.packet.Packet;

public abstract class AbstractPacketSubscriber implements PacketSubscriber {
	private final Matcher matcher;

	public AbstractPacketSubscriber(final Matcher matcher) {
		this.matcher = matcher;
	}

	public Matcher getMatcher() {
		return matcher;
	}

	public abstract void handle(final Packet stanza);

}
