package com.calclab.emite.client;

import com.allen_sauer.gwt.log.client.Log;
import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.components.ContainerPlugin;
import com.calclab.emite.client.core.bosh.BoshOptions;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.DispatcherPlugin;
import com.calclab.emite.client.core.services.Connector;
import com.calclab.emite.client.core.services.Scheduler;
import com.calclab.emite.client.core.services.XMLService;
import com.calclab.emite.client.core.services.gwt.GWTConnector;
import com.calclab.emite.client.core.services.gwt.GWTScheduler;
import com.calclab.emite.client.core.services.gwt.GWTXMLService;
import com.calclab.emite.client.im.chat.ChatManagerDefault;
import com.calclab.emite.client.im.chat.ChatPlugin;
import com.calclab.emite.client.im.presence.PresenceManager;
import com.calclab.emite.client.im.presence.PresencePlugin;
import com.calclab.emite.client.im.roster.Roster;
import com.calclab.emite.client.im.roster.RosterPlugin;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.session.SessionOptions;
import com.calclab.emite.client.xmpp.session.SessionPlugin;

public class Xmpp {

    public static Xmpp create(final BoshOptions options) {
	final GWTXMLService xmlService = new GWTXMLService();
	final GWTConnector connector = new GWTConnector();
	final GWTScheduler scheduler = new GWTScheduler();
	return create(connector, xmlService, scheduler, options);
    }

    public static Xmpp create(final Connector connector, final XMLService xmlService, final Scheduler scheduler,
	    final BoshOptions options) {

	final Container container = ContainerPlugin.create();
	Plugins.installDefaultPlugins(container, xmlService, connector, scheduler, options);
	container.start();
	return new Xmpp(container);
    }

    private final Container container;
    private final Session session;

    public Xmpp(final Container container) {
	this.container = container;
	this.session = SessionPlugin.getSession(container);
    }

    public ChatManagerDefault getChat() {
	return ChatPlugin.getChat(container);
    }

    public Container getComponents() {
	return container;
    }

    public Dispatcher getDispatcher() {
	return DispatcherPlugin.getDispatcher(container);
    }

    public PresenceManager getPresenceManager() {
	return PresencePlugin.getManager(container);
    }

    public Roster getRoster() {
	return RosterPlugin.getRoster(container);
    }

    public Session getSession() {
	return session;
    }

    public void login(final String userName, final String userPassword) {
	Log.debug("XMPP Login " + userName + " : " + userPassword);
	session.login(new SessionOptions(userName, userPassword));
    }

    public void logout() {
	session.logout();
    }

    /**
     * DEPRECATED: use Chat.send instead
     * 
     * @param to
     * @param msg
     */
    @Deprecated
    public void send(final String to, final String msg) {
	getChat().send(to, msg);
    }

}
