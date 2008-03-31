package com.calclab.emite.client.core.dispatcher;

import com.calclab.emite.client.components.Answer;
import com.calclab.emite.client.core.dispatcher.matcher.Matcher;
import com.calclab.emite.client.core.packet.Event;
import com.calclab.emite.client.core.packet.Packet;

public class PublisherActionsBuilder {
	protected final Dispatcher dispatcher;
	protected final Matcher matcher;

	protected PublisherActionsBuilder(final Matcher matcher, final Dispatcher dispatcher) {
		this.matcher = matcher;
		this.dispatcher = dispatcher;
	}

	public void Do(final Action action) {
		dispatcher.subscribe(matcher, action);
	}

	public void Publish(final Answer answer) {
		dispatcher.subscribe(matcher, new Action() {
			public void handle(final Packet received) {
				dispatcher.publish(answer.respondTo(received));
			}
		});
	}

	public void Publish(final Event event) {
		dispatcher.subscribe(matcher, new Action() {
			public void handle(final Packet received) {
				dispatcher.publish(event);
			}
		});
	}

}
