package com.calclab.emite.client.core.bosh;

import com.calclab.emite.client.core.dispatcher.Action;
import com.calclab.emite.client.core.dispatcher.Answer;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.PublisherActionsBuilder;
import com.calclab.emite.client.core.dispatcher.matcher.Matcher;
import com.calclab.emite.client.core.packet.Event;
import com.calclab.emite.client.core.packet.Packet;

public class SenderActionsBuilder extends PublisherActionsBuilder {
	private final Bosh bosh;

	protected SenderActionsBuilder(final Matcher matcher, final Dispatcher dispatcher, final Bosh bosh) {
		super(matcher, dispatcher);
		this.bosh = bosh;
	}

	public void Send(final Answer answer) {
		dispatcher.subscribe(matcher, new Action() {
			public void handle(final Packet received) {
				bosh.send(answer.respondTo(received));
			}
		});
	}

	public void Send(final Event event) {
		dispatcher.subscribe(matcher, new Action() {
			public void handle(final Packet received) {
				bosh.send(event);
			}
		});
	}
}
