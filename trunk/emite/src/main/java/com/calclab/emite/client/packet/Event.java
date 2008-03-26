package com.calclab.emite.client.packet;

public class Event extends DelegatedPacket {

	private static Packet cloneEvent(final Event event) {
		return new BasicPacket("event", "emite:event").With("name", event.getName());
	}

	public Event(final Event event) {
		super(cloneEvent(event));
	}

	public Event(final String name) {
		super(new BasicPacket("event", "emite:event"));
		setAttribute("name", name);
	}
}
