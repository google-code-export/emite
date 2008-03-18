package com.calclab.emite.client;

import com.calclab.emite.client.action.ActionDispatcher;
import com.calclab.emite.client.action.Dispatcher;
import com.calclab.emite.client.log.LoggerAdapter;
import com.calclab.emite.client.log.LoggerOutput;
import com.calclab.emite.client.plugin.DefaultPluginManager;
import com.calclab.emite.client.plugin.PluginManager;
import com.calclab.emite.client.x.core.SASLModule;
import com.calclab.emite.client.x.im.ChatPlugin;
import com.calclab.emite.client.x.im.roster.RosterPlugin;
import com.calclab.emite.client.x.im.session.SessionPlugin;

public class XMPPPlugin {
	public static Components createComponents(final LoggerOutput output) {
		final Components c = new ComponentContainer(new LoggerAdapter(output));
		final Dispatcher dispatcher = new ActionDispatcher(c.getLogger());

		c.setGlobals(new HashGlobals());
		c.setDispatcher(dispatcher);
		return c;
	}

	public static void installPlugins(final Components c) {
		final PluginManager manager = new DefaultPluginManager(c);
		manager.install("sasl", new SASLModule(c.getGlobals()));
		manager.install("chat", new ChatPlugin(c.getConnection(), c.getDispatcher()));
		manager.install("session", new SessionPlugin(c.getGlobals(), c.getDispatcher(), c.getConnection()));
		manager.install("roster", new RosterPlugin());
	}
}
