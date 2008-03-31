package com.calclab.emite.client.im.session;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.core.bosh.BoshPlugin;
import com.calclab.emite.client.core.bosh.Bosh;
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
		final Bosh bosh = BoshPlugin.getConnection(container);
		final Dispatcher dispatcher = DispatcherPlugin.getDispatcher(container);
		final Session session = new Session(dispatcher, globals);
		container.register(COMPONENT_SESSION, session);
		final SessionManager manager = new SessionManager(dispatcher, bosh, globals, session);
		container.install(COMPONENT_SESSION_MANAGER, manager);
	}
}
