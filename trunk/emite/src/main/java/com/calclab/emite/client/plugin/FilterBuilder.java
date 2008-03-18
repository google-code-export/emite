package com.calclab.emite.client.plugin;

import com.calclab.emite.client.IDispatcher;
import com.calclab.emite.client.bosh.IConnection;
import com.calclab.emite.client.matcher.BasicMatcher;
import com.calclab.emite.client.packet.Event;

public class FilterBuilder {

	private final IConnection connection;
	private final IDispatcher dispatcher;

	public FilterBuilder(final IDispatcher dispatcher, final IConnection connection) {
		this.dispatcher = dispatcher;
		this.connection = connection;
	}

	public ActionBuilder Event(final Event event) {
		return new ActionBuilder(new BasicMatcher("event", "name", event.getName()), dispatcher, connection);
	}

	public ActionBuilder IQ(final String id) {
		return new ActionBuilder(new BasicMatcher("iq", "id", id), dispatcher, connection);
	}

	public ActionBuilder MessageTo(final String to) {
		return new ActionBuilder(new BasicMatcher("message", "to", to), dispatcher, connection);
	}

	public ActionBuilder Packet(final String name) {
		return Packet(name, null, null);
	}

	public ActionBuilder Packet(final String name, final String attName, final String attValue) {
		return new ActionBuilder(new BasicMatcher(name, attName, attValue), dispatcher, connection);
	}

}
