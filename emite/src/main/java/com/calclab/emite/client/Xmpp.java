package com.calclab.emite.client;

import com.calclab.emite.client.bosh.BoshOptions;
import com.calclab.emite.client.connection.Connection;
import com.calclab.emite.client.connection.ConnectionListener;
import com.calclab.emite.client.connection.ConnectionPlugin;
import com.calclab.emite.client.im.MessageListener;
import com.calclab.emite.client.im.MessagePlugin;
import com.calclab.emite.client.im.PresencePlugin;
import com.calclab.emite.client.im.roster.Roster;
import com.calclab.emite.client.im.roster.RosterPlugin;
import com.calclab.emite.client.im.session.SessionPlugin;
import com.calclab.emite.client.log.LoggerOutput;
import com.calclab.emite.client.modules.ResourceModule;
import com.calclab.emite.client.modules.SASLModule;
import com.calclab.emite.client.plugin.PluginManager;

public class Xmpp {

	public static Xmpp create(final BoshOptions options, final LoggerOutput output) {
		final Engine engine = new Engine(options, output);
		final PluginManager pluginManager = new PluginManager(engine);
		pluginManager.install(new MessagePlugin(), new RosterPlugin(), new PresencePlugin());
		pluginManager.install(new SASLModule(), new ResourceModule(), new SessionPlugin());
		return new Xmpp(engine, pluginManager);
	}

	private final Connection connection;
	private final Engine engine;

	/**
	 * TODO: pluginManager se usará para desintalar los plugins (si se
	 * necesitase)
	 * 
	 * @param engine
	 * @param pluginManager
	 */
	private Xmpp(final Engine engine, final PluginManager pluginManager) {
		this.engine = engine;
		this.connection = ConnectionPlugin.getConnection(engine);
	}

	public void addConnectionListener(final ConnectionListener listener) {
		connection.addListener(listener);
	}

	public void addMessageListener(final MessageListener listener) {
		MessagePlugin.getMessager(engine).addListener(listener);
	}

	public Roster getRoster() {
		return RosterPlugin.getRoster(engine);
	}

	public void login(final String userName, final String userPassword) {
		connection.login(userName, userPassword);
	}

	public void logout() {
		connection.logout();
	}

	public void send(final String to, final String msg) {
		MessagePlugin.getMessager(engine).send(to, msg);
	}

}
