package com.calclab.emite.client;

import com.calclab.emite.client.action.ActionDispatcher;
import com.calclab.emite.client.action.Dispatcher;
import com.calclab.emite.client.log.Logger;
import com.calclab.emite.client.log.LoggerAdapter;
import com.calclab.emite.client.log.LoggerOutput;
import com.calclab.emite.client.plugin.PluginManager;
import com.calclab.emite.client.plugin.DefaultPluginManager;
import com.calclab.emite.client.x.core.SASLModule;
import com.calclab.emite.client.x.im.ChatPlugin;
import com.calclab.emite.client.x.im.roster.RosterPlugin;
import com.calclab.emite.client.x.im.session.SessionPlugin;

public class XMPPPlugin {
	public static Components createContainer(final LoggerOutput output) {
		final Components components = new ComponentContainer();
		final Logger logger = new LoggerAdapter(output);
		final PluginManager pluginManager = new DefaultPluginManager(components);
		final Dispatcher dispatcher = new ActionDispatcher(logger);

		components.setLogger(logger);
		components.setGlobals(new HashGlobals());
		components.setDispatcher(dispatcher);
		components.setPluginManager(pluginManager);
		return components;
	}

	public static void installPlugins(final Components c) {
		final PluginManager manager = c.getPluginManager();
		manager.install("sasl", new SASLModule(c.getGlobals()));
		manager.install("chat", new ChatPlugin(c.getConnection(), c.getDispatcher()));
		manager.install("session", new SessionPlugin(c.getGlobals(), c.getDispatcher(), c.getConnection()));
		manager.install("roster", new RosterPlugin());
	}

}
