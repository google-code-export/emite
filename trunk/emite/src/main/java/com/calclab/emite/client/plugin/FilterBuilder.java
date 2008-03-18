package com.calclab.emite.client.plugin;

import com.calclab.emite.client.Components;
import com.calclab.emite.client.action.BussinessLogic;
import com.calclab.emite.client.action.FilteredAction;
import com.calclab.emite.client.action.LogicAction;
import com.calclab.emite.client.action.PublishAction;
import com.calclab.emite.client.action.SendAction;
import com.calclab.emite.client.bosh.Connection;
import com.calclab.emite.client.dispatcher.Dispatcher;
import com.calclab.emite.client.matcher.BasicMatcher;
import com.calclab.emite.client.packet.Event;
import com.calclab.emite.client.packet.Packet;

public class FilterBuilder {

	public class ActionBuilder {
		public void Do(final BussinessLogic logic) {
			final LogicAction action = new LogicAction(logic);
			getDispatcher().addListener(new FilteredAction(filter, action));
		}

		public void publish(final BussinessLogic bussinessLogic) {
			final PublishAction action = new PublishAction(getDispatcher(), bussinessLogic);
			getDispatcher().addListener(new FilteredAction(filter, action));
		}

		public void publish(final Packet packet) {
			final PublishAction action = new PublishAction(getDispatcher(), packet);
			getDispatcher().addListener(new FilteredAction(filter, action));
		}

		public void send(final BussinessLogic logic) {
			final SendAction action = new SendAction(getConnection(), logic);
			getDispatcher().addListener(new FilteredAction(filter, action));
		}

		public void send(final Packet packet) {
			final SendAction action = new SendAction(getConnection(), packet);
			getDispatcher().addListener(new FilteredAction(filter, action));
		}

		BasicMatcher getCurrentFilter() {
			return filter;
		}

	}

	private final ActionBuilder actionBuilder;
	private final Components container;

	BasicMatcher filter;

	public FilterBuilder(final Components container) {
		this.container = container;
		this.actionBuilder = new ActionBuilder();
	}

	public ActionBuilder Event(final Event event) {
		final BasicMatcher matcher = new BasicMatcher("event", "name", event.getAttribute("name"));
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

	public ActionBuilder Packet(final String name, final String attName, final String attValue) {
		final BasicMatcher matcher = new BasicMatcher(name, attName, attValue);
		return createBuilder(matcher);
	}

	private ActionBuilder createBuilder(final BasicMatcher matcher) {
		this.filter = matcher;
		return actionBuilder;
	}

	Connection getConnection() {
		return container.getConnection();
	}

	Dispatcher getDispatcher() {
		return container.getDispatcher();
	}
}
