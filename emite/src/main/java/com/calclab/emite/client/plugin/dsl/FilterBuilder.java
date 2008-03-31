package com.calclab.emite.client.plugin.dsl;

import com.calclab.emite.client.core.bosh.Connection;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.matcher.BasicMatcher;
import com.calclab.emite.client.packet.Event;

public class FilterBuilder {

	BasicMatcher filter;

	private final ActionBuilder actionBuilder;

	private final Dispatcher dispatcher;

	private final Connection connection;

	public FilterBuilder(final Dispatcher dispatcher,
			final Connection connection) {
		this.dispatcher = dispatcher;
		this.connection = connection;
		this.actionBuilder = new ActionBuilder(this);
	}

	public ActionBuilder Event(final Event event) {
		final BasicMatcher matcher = new BasicMatcher("event", "type", event
				.getType());
		return createBuilder(matcher);
	}

	public ActionBuilder IQ(final String id) {
		final BasicMatcher matcher = new BasicMatcher("iq", "id", id);
		return createBuilder(matcher);
	}

	public ActionBuilder MessageTo(final String to) {
		final BasicMatcher matcher = new BasicMatcher("message", "to", to);
		return createBuilder(matcher);
	}

	public ActionBuilder Packet(final String name) {
		return Packet(name, null, null);
	}

	public ActionBuilder Packet(final String name, final String attName,
			final String attValue) {
		final BasicMatcher matcher = new BasicMatcher(name, attName, attValue);
		return createBuilder(matcher);
	}

	Connection getConnection() {
		if (connection == null) {
			throw new RuntimeException("you should use a SenderPlugin instead");
		}
		return connection;
	}

	Dispatcher getDispatcher() {
		return dispatcher;
	}

	private ActionBuilder createBuilder(final BasicMatcher matcher) {
		this.filter = matcher;
		return actionBuilder;
	}
}
