package com.calclab.emite.client.subscriber;

import com.calclab.emite.client.matcher.BasicMatcher;
import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.packet.stanza.Message;

public abstract class MessageSubscriber extends AbstractPacketSubscriber {

	public MessageSubscriber(final String to) {
		super(new BasicMatcher("message", "to", to));
	}

	@Override
	public void handle(final Packet stanza) {
		this.handleMessage(new Message(stanza));
	}

	protected abstract void handleMessage(Message message);

}