package com.calclab.emite.client.dispatcher;

import com.calclab.emite.client.Components;
import com.calclab.emite.client.bosh.Connection;
import com.calclab.emite.client.log.Logger;
import com.calclab.emite.client.plugin.FilterBuilder;
import com.calclab.emite.client.plugin.Plugin;

public class DispatcherPlugin implements Plugin {

	private final ActionDispatcher dispatcher;

	public DispatcherPlugin(final Parser parser, final Logger logger) {
		dispatcher = new ActionDispatcher(parser, logger);
	}

	public void install(final Components components) {
		final Connection connection = components.getConnection();
		if (connection == null) {
			throw new RuntimeException("dispacther plugin depends of a connection");
		}
		components.setDispatcher(dispatcher);
		connection.addListener(dispatcher);
	}

	public void start(final FilterBuilder when) {
	}

}
