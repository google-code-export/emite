package com.calclab.emite.client.connection;

import com.calclab.emite.client.Engine;
import com.calclab.emite.client.packet.Event;
import com.calclab.emite.client.plugin.Plugin;
import com.calclab.emite.client.subscriber.EventSubscriber;

public class ConnectionPlugin implements Plugin {

	public ConnectionPlugin() {
	}

	public void start(final Engine engine) {
		final Connection connection = new Connection(engine);
		engine.register("connection", connection);

		engine.addListener(new EventSubscriber("connection:connecting") {
			@Override
			protected void handleEvent(final Event event) {
				connection.fireOnConnecting();
			}

		});

		engine.addListener(new EventSubscriber("connection:disconnected") {
			@Override
			protected void handleEvent(final Event event) {
				connection.fireOnDisconnected();
			}
		});

		engine.addListener(new EventSubscriber("session:success") {
			@Override
			protected void handleEvent(final Event event) {
				connection.fireOnConnected();
			}
		});

	}
}
