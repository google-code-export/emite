package com.calclab.emite.client;

import com.calclab.emite.client.bosh.Bosh;
import com.calclab.emite.client.bosh.BoshOptions;
import com.calclab.emite.client.connector.Connector;
import com.calclab.emite.client.dispatcher.ActionDispatcher;
import com.calclab.emite.client.dispatcher.Dispatcher;
import com.calclab.emite.client.dispatcher.Parser;
import com.calclab.emite.client.log.LoggerAdapter;
import com.calclab.emite.client.log.LoggerOutput;
import com.calclab.emite.client.plugin.DefaultPluginManager;
import com.calclab.emite.client.plugin.PluginManager;
import com.calclab.emite.client.x.core.ResourceModule;
import com.calclab.emite.client.x.core.SASLModule;
import com.calclab.emite.client.x.im.ChatPlugin;
import com.calclab.emite.client.x.im.roster.RosterPlugin;
import com.calclab.emite.client.x.im.session.SessionPlugin;

public class Container {
	private final Components c;

	public Container(final LoggerOutput output) {
		c = new ComponentContainer(new LoggerAdapter(output));
	}

	public Components createComponents(final Parser parser, final Connector connector, final BoshOptions options) {
		final Dispatcher dispatcher = new ActionDispatcher(parser, c.getLogger());
		c.setConnection(new Bosh(connector, options, c.getLogger()));

		c.setGlobals(new HashGlobals());
		c.setDispatcher(dispatcher);
		return c;
	}

	public Components getComponents() {
		return c;
	}

	public void installDefaultPlugins() {
		final PluginManager manager = new DefaultPluginManager(c.getLogger(), c);
		manager.install("chat", new ChatPlugin(c.getConnection(), c.getDispatcher()));
		manager.install("session", new SessionPlugin(c.getGlobals(), c.getDispatcher(), c.getConnection()));
		manager.install("roster", new RosterPlugin());
		manager.install("sasl", new SASLModule(c.getGlobals()));
		manager.install("resource", new ResourceModule(c.getGlobals()));
	}
}
