package com.calclab.emite.client.core.dispatcher;

import com.calclab.emite.client.components.Container;

public class DispatcherPlugin {

	private static final String COMPONENT_DISPATCHER = "dispatcher";

	public static Dispatcher getDispatcher(final Container container) {
		return (Dispatcher) container.get(COMPONENT_DISPATCHER);
	}

	public static void install(final Container container) {
		final DispatcherDefault dispatcher = new DispatcherDefault();
		setDispatcher(container, dispatcher);
	}

	public static void setDispatcher(final Container container, final Dispatcher dispatcher) {
		container.register(COMPONENT_DISPATCHER, dispatcher);
	}

}
