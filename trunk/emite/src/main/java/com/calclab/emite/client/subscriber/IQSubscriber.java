package com.calclab.emite.client.subscriber;

import com.calclab.emite.client.matcher.BasicMatcher;
import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.packet.stanza.IQ;

public abstract class IQSubscriber extends AbstractPacketSubscriber {

	public IQSubscriber(final String id) {
		super(new BasicMatcher("iq", "id", id));
	}

	@Override
	public void handle(final Packet stanza) {
		this.handleIQ(new IQ(stanza));
	}

	protected abstract void handleIQ(IQ iq);

}