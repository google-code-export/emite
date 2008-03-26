package com.calclab.emite.client;

import com.calclab.emite.client.bosh.BoshOptions;
import com.calclab.emite.client.bosh.BoshPlugin;
import com.calclab.emite.client.bosh.Connection;
import com.calclab.emite.client.connector.Connector;
import com.calclab.emite.client.dispatcher.Dispatcher;
import com.calclab.emite.client.dispatcher.DispatcherPlugin;
import com.calclab.emite.client.packet.XMLService;
import com.calclab.emite.client.plugin.DefaultPluginManager;
import com.calclab.emite.client.x.core.ResourcePlugin;
import com.calclab.emite.client.x.core.SASLPlugin;
import com.calclab.emite.client.x.im.ChatPlugin;
import com.calclab.emite.client.x.im.roster.RosterPlugin;
import com.calclab.emite.client.x.im.session.SessionPlugin;

public class Container {
	private final Components c;
	private final DefaultPluginManager manager;

	public Container() {
		c = new ComponentContainer();
		c.setGlobals(new HashGlobals());
		manager = new DefaultPluginManager(c);
	}

	public Components getComponents() {
		return c;
	}

	public void installDefaultPlugins(final XMLService xmler,
			final Connector connector, final BoshOptions options) {
		manager.install("dispatcher", new DispatcherPlugin());
		manager.install("bosh", new BoshPlugin(connector, xmler, options));

		final Globals globals = c.getGlobals();
		final Connection connection = c.getConnection();
		final Dispatcher dispatcher = c.getDispatcher();

		manager.install("chat", new ChatPlugin(dispatcher, connection));
		manager.install("session", new SessionPlugin(dispatcher, connection,
				globals));
		manager.install("roster", new RosterPlugin(connection));
		manager.install("sasl", new SASLPlugin(connection, globals));
		manager.install("resource", new ResourcePlugin(connection, globals));

		manager.start();
	}
}
