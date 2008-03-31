package com.calclab.emite.client.components;

import com.calclab.emite.client.core.dispatcher.Action;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.matcher.Matcher;
import com.calclab.emite.client.packet.Event;
import com.calclab.emite.client.packet.Packet;
import com.calclab.emite.client.plugin.dsl.Answer;

public class BasicActionsBuilder {
	protected final Dispatcher dispatcher;
	protected final Matcher matcher;

	BasicActionsBuilder(final Matcher matcher, final Dispatcher dispatcher) {
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
