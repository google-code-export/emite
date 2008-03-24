package com.calclab.emite.client.dispatcher;

import com.calclab.emite.client.Components;
import com.calclab.emite.client.log.Logger;
import com.calclab.emite.client.plugin.Plugin;

public class DispatcherPlugin implements Plugin {

	private final DispatcherDefault dispatcher;

	public DispatcherPlugin(final Parser parser, final Logger logger) {
		dispatcher = new DispatcherDefault(parser, logger);
	}

	public void attach(final Dispatcher dispatcher) {
	}

	public void install(final Components components) {
		components.setDispatcher(dispatcher);
	}

}
