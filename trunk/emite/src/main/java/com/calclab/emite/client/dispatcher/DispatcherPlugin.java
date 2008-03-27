package com.calclab.emite.client.dispatcher;

import com.calclab.emite.client.Components;
import com.calclab.emite.client.plugin.Plugin;

public class DispatcherPlugin implements Plugin {

	public static Dispatcher getDispatcher(final Components components) {
		return components.getDispatcher();
	}

	public static void setDispatcher(final Components components, final Dispatcher dispatcher) {
		components.setDispatcher(dispatcher);
	}

	public void attach(final Dispatcher dispatcher) {
	}

	public void install(final Components components) {
		final DispatcherDefault dispatcher = new DispatcherDefault();
		components.setDispatcher(dispatcher);
	}

}
