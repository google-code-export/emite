package com.calclab.emite.client.subscriber;

import com.calclab.emite.client.matcher.BasicMatcher;
import com.calclab.emite.client.packet.Event;
import com.calclab.emite.client.packet.Packet;

public abstract class EventSubscriber extends AbstractPacketSubscriber {

	public EventSubscriber(final String eventName) {
		super(new BasicMatcher("event", "name", eventName));
	}

	@Override
	public void handle(final Packet stanza) {
		this.handleEvent(new Event(stanza));
	}

	protected abstract void handleEvent(Event event);

}
