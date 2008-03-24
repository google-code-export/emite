package com.calclab.emite.client;

import com.calclab.emite.client.bosh.BoshOptions;
import com.calclab.emite.client.connector.GWTConnector;
import com.calclab.emite.client.log.LoggerOutput;
import com.calclab.emite.client.packet.GWTXMLService;
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
		final Container container = new Container(output);
		container.installDefaultPlugins(new GWTXMLService(), new GWTConnector(), options);
		return new Xmpp(container.getComponents());
	}

	private final Components components;
	private final Session session;

	public Xmpp(final Components components) {
		this.components = components;
		this.session = SessionPlugin.getSession(components);
	}

	public void addMessageListener(final MessageListener listener) {
		ChatPlugin.getChat(components).addListener(listener);
	}

	public void addSessionListener(final SessionListener listener) {
		session.addListener(listener);
	}

	public Components getComponents() {
		return components;
	}

	public Roster getRoster() {
		return RosterPlugin.getRoster(components);
	}

	public Session getSession() {
		return session;
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
