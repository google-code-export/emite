package com.calclab.emite.client;

import com.calclab.emite.client.bosh.BoshOptions;
import com.calclab.emite.client.bosh.BoshPlugin;
import com.calclab.emite.client.connector.Connector;
import com.calclab.emite.client.dispatcher.DispatcherPlugin;
import com.calclab.emite.client.dispatcher.Parser;
import com.calclab.emite.client.log.LoggerAdapter;
import com.calclab.emite.client.log.LoggerOutput;
import com.calclab.emite.client.plugin.DefaultPluginManager;
import com.calclab.emite.client.x.core.ResourcePlugin;
import com.calclab.emite.client.x.core.SASLPlugin;
import com.calclab.emite.client.x.im.ChatPlugin;
import com.calclab.emite.client.x.im.roster.RosterPlugin;
import com.calclab.emite.client.x.im.session.SessionPlugin;

public class Container {
	private final Components c;
	private final LoggerAdapter logger;
	private final DefaultPluginManager manager;

	public Container(final LoggerOutput output) {
		logger = new LoggerAdapter(output);
		c = new ComponentContainer(logger);
		c.setGlobals(new HashGlobals());
		manager = new DefaultPluginManager(c.getLogger(), c);
	}

	public Components getComponents() {
		return c;
	}

	public void installDefaultPlugins(final Parser parser, final Connector connector, final BoshOptions options) {
		manager.install("bosh", new BoshPlugin(connector, options, logger, c.getGlobals()));
		manager.install("dispatcher", new DispatcherPlugin(parser, logger));
		manager.install("chat", new ChatPlugin(c.getConnection(), c.getDispatcher()));
		manager.install("session", new SessionPlugin(c.getGlobals(), c.getDispatcher(), c.getConnection()));
		manager.install("roster", new RosterPlugin());
		manager.install("sasl", new SASLPlugin(c.getGlobals()));
		manager.install("resource", new ResourcePlugin(c.getGlobals()));

		manager.start();
	}
}
