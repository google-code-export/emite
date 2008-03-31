package com.calclab.emite.client.core.bosh;

import com.calclab.emite.client.components.Answer;
import com.calclab.emite.client.core.dispatcher.Action;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.PublisherActionsBuilder;
import com.calclab.emite.client.core.dispatcher.matcher.Matcher;
import com.calclab.emite.client.core.packet.Event;
import com.calclab.emite.client.core.packet.Packet;

public class SenderActionsBuilder extends PublisherActionsBuilder {
	private final Connection connection;

	protected SenderActionsBuilder(final Matcher matcher, final Dispatcher dispatcher, final Connection connection) {
		super(matcher, dispatcher);
		this.connection = connection;
	}

	public void Send(final Answer answer) {
		dispatcher.subscribe(matcher, new Action() {
			public void handle(final Packet received) {
				connection.send(answer.respondTo(received));
			}
		});
	}

	public void Send(final Event event) {
		dispatcher.subscribe(matcher, new Action() {
			public void handle(final Packet received) {
				connection.send(event);
			}
		});
	}
}
