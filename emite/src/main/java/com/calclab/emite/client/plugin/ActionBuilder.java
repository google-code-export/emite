package com.calclab.emite.client.plugin;

import com.calclab.emite.client.IDispatcher;
import com.calclab.emite.client.action.BussinessLogic;
import com.calclab.emite.client.action.FilteredCommand;
import com.calclab.emite.client.action.LogicAction;
import com.calclab.emite.client.action.PublishAction;
import com.calclab.emite.client.action.SendAction;
import com.calclab.emite.client.bosh.IConnection;
import com.calclab.emite.client.matcher.Matcher;
import com.calclab.emite.client.packet.Packet;

public class ActionBuilder {
	private final IConnection connection;
	private final IDispatcher dispatcher;
	private final Matcher matcher;

	ActionBuilder(final Matcher matcher, final IDispatcher dispatcher, final IConnection connection) {
		this.matcher = matcher;
		this.dispatcher = dispatcher;
		this.connection = connection;
	}

	public void Do(final BussinessLogic logic) {
		final LogicAction action = new LogicAction(logic);
		dispatcher.addListener(new FilteredCommand(matcher, action));
	}

	public void publish(final BussinessLogic bussinessLogic) {
		final PublishAction action = new PublishAction(dispatcher, bussinessLogic);
		dispatcher.addListener(new FilteredCommand(matcher, action));
	}

	public void publish(final Packet packet) {
		final PublishAction action = new PublishAction(dispatcher, packet);
		dispatcher.addListener(new FilteredCommand(matcher, action));
	}

	public void send(final BussinessLogic logic) {
		final SendAction action = new SendAction(connection, logic);
		dispatcher.addListener(new FilteredCommand(matcher, action));
	}

	public void send(final Packet packet) {
		final SendAction action = new SendAction(connection, packet);
		dispatcher.addListener(new FilteredCommand(matcher, action));
	}

}
