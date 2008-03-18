package com.calclab.emite.client;

import com.calclab.emite.client.bosh.Bosh;
import com.calclab.emite.client.bosh.BoshOptions;
import com.calclab.emite.client.bosh.IConnection;
import com.calclab.emite.client.log.Logger;
import com.calclab.emite.client.log.LoggerAdapter;
import com.calclab.emite.client.log.LoggerOutput;
import com.calclab.emite.client.plugin.IPluginManager;
import com.calclab.emite.client.plugin.PluginManager;
import com.calclab.emite.client.x.core.SASLModule;
import com.calclab.emite.client.x.im.ChatPlugin;
import com.calclab.emite.client.x.im.MessageListener;
import com.calclab.emite.client.x.im.roster.Roster;
import com.calclab.emite.client.x.im.roster.RosterPlugin;
import com.calclab.emite.client.x.im.session.Session;
import com.calclab.emite.client.x.im.session.SessionListener;
import com.calclab.emite.client.x.im.session.SessionOptions;
import com.calclab.emite.client.x.im.session.SessionPlugin;

public class Xmpp {

	public static Xmpp create(final BoshOptions options, final LoggerOutput output) {
		final Logger logger = new LoggerAdapter(output);
		final IContainer components = new Container();
		final IConnection bosh = new Bosh(options, logger);
		final IPluginManager pluginManager = new PluginManager(components);
		final IDispatcher dispatcher = new ActionDispatcher(logger);

		components.setLogger(logger);
		components.setGlobals(new Globals());
		components.setConnection(bosh);
		components.setDispatcher(dispatcher);

		installPlugins(pluginManager, components);
		return new Xmpp(components);
	}

	private static void installPlugins(final IPluginManager manager, final IContainer c) {
		manager.install("sasl", new SASLModule(c.getGlobals()));
		manager.install("chat", new ChatPlugin(c.getConnection(), c.getDispatcher()));
		manager.install("session", new SessionPlugin(c.getGlobals(), c.getDispatcher()));
		manager.install("roster", new RosterPlugin());
	}

	private final IContainer components;
	private final Session session;

	/**
	 * TODO: pluginManager se usará para desintalar los plugins (si se
	 * necesitase)
	 * 
	 * @param queue
	 * @param pluginManager
	 */
	private Xmpp(final IContainer components) {
		this.components = components;
		this.session = SessionPlugin.getSession(components);
	}

	public void addMessageListener(final MessageListener listener) {
		ChatPlugin.getChat(components).addListener(listener);
	}

	public void addSessionListener(final SessionListener listener) {
		session.addListener(listener);
	}

	public Roster getRoster() {
		return RosterPlugin.getRoster(components);
	}

	public void login(final String userName, final String userPassword) {
		session.login(new SessionOptions(userName, userPassword));
	}

	public void logout() {
		session.logout();
	}

	public void send(final String to, final String msg) {
		ChatPlugin.getChat(components).send(to, msg);
	}

}
