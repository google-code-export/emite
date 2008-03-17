package com.calclab.emite.client;

import com.calclab.emite.client.bosh.BoshOptions;
import com.calclab.emite.client.im.MessageListener;
import com.calclab.emite.client.im.MessagePlugin;
import com.calclab.emite.client.im.PresencePlugin;
import com.calclab.emite.client.im.roster.Roster;
import com.calclab.emite.client.im.roster.RosterPlugin;
import com.calclab.emite.client.im.session.SessionPlugin;
import com.calclab.emite.client.log.LoggerOutput;
import com.calclab.emite.client.modules.ResourceModule;
import com.calclab.emite.client.modules.SASLModule;

public class Xmpp {

	public static Xmpp create(final BoshOptions options, final LoggerOutput output) {
		final Engine engine = new Engine(options, output);
		engine.pluginManager.install(new MessagePlugin(), new RosterPlugin(), new PresencePlugin());
		engine.pluginManager.install(new SASLModule(), new ResourceModule(), new SessionPlugin());
		return new Xmpp(engine);
	}

	private final Engine engine;

	public Xmpp(final Engine engine) {
		this.engine = engine;
	}

	public void addMessageListener(final MessageListener listener) {
		MessagePlugin.getMessager(engine).addListener(listener);
	}

	public Roster getRoster() {
		return RosterPlugin.getRoster(engine);
	}

	public void login(final String userName, final String userPassword) {
		engine.setGlobal(Engine.USER, userName);
		engine.setGlobal(Engine.PASSWORD, userPassword);
		engine.start();
	}

	public void send(final String to, final String msg) {
		MessagePlugin.getMessager(engine).send(to, msg);
	}

}
