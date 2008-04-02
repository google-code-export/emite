package com.calclab.emite.client.xmpp.session;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.core.bosh.BoshPlugin;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.DispatcherPlugin;
import com.calclab.emite.client.core.services.Globals;
import com.calclab.emite.client.core.services.ServicesPlugin;

/**
 * @author dani
 */
public class SessionPlugin {
	private static final String COMPONENT_SESSION = "session";
	private static final String COMPONENT_SESSION_MANAGER = "sessionManager";

	public static Session getSession(final Container container) {
		return (Session) container.get(COMPONENT_SESSION);
	}

	public static void install(final Container container) {
		final Globals globals = ServicesPlugin.getGlobals(container);
		final Dispatcher dispatcher = DispatcherPlugin.getDispatcher(container);
		final Session session = new Session(dispatcher, globals);
		container.register(COMPONENT_SESSION, session);
		final Emite emite = BoshPlugin.getEmite(container);
		final SessionManager manager = new SessionManager(emite, globals, session);
		container.install(COMPONENT_SESSION_MANAGER, manager);
	}
}
