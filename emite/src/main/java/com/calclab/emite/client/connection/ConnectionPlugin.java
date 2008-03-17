package com.calclab.emite.client.connection;

import com.calclab.emite.client.Engine;
import com.calclab.emite.client.im.session.SessionPlugin;
import com.calclab.emite.client.packet.Event;
import com.calclab.emite.client.plugin.Plugin;
import com.calclab.emite.client.subscriber.EventSubscriber;

public class ConnectionPlugin implements Plugin {
	private static final String COMPONENT_NAME = "connection";

	public static Connection getConnection(final Engine engine) {
		return (Connection) engine.getComponent(COMPONENT_NAME);
	}

	public void start(final Engine engine) {
		final Connection connection = new Connection(engine);
		engine.register(COMPONENT_NAME, connection);

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

		engine.addListener(new EventSubscriber(SessionPlugin.SUCCESS) {
			@Override
			protected void handleEvent(final Event event) {
				connection.fireOnConnected();
			}
		});

	}
}
