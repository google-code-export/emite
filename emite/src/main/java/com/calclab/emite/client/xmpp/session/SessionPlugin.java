/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.emite.client.xmpp.session;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.core.CorePlugin;
import com.calclab.emite.client.core.bosh.Emite;

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
	final Emite emite = CorePlugin.getEmite(container);
	final SessionManager manager = new SessionManager(emite);
	final Session session = new Session(manager);
	manager.setSession(session);
	container.register(COMPONENT_SESSION, session);
	container.install(COMPONENT_SESSION_MANAGER, manager);
    }
}
