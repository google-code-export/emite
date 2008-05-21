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
package com.calclab.emite.client.xmpp;

import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.modular.Container;
import com.calclab.emite.client.modular.Module;
import com.calclab.emite.client.modular.Provider;
import com.calclab.emite.client.modular.Scopes;
import com.calclab.emite.client.xmpp.resource.ResourceBindingManager;
import com.calclab.emite.client.xmpp.sasl.SASLManager;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.session.SessionManager;

public class XMPPModule implements Module {

    public static final Class<SessionManager> COMPONENT_SESSION_MANAGER = SessionManager.class;
    private static final Class<Session> COMPONENT_SESSION = Session.class;

    public static Session getSession(final Container container) {
	return container.getInstance(XMPPModule.COMPONENT_SESSION);
    }

    public Class<? extends Module> getType() {
	return XMPPModule.class;
    }

    public void onLoad(final Container container) {

	container.registerProvider(ResourceBindingManager.class, new Provider<ResourceBindingManager>() {
	    public ResourceBindingManager get() {
		return new ResourceBindingManager(container.getInstance(Emite.class));
	    }
	}, Scopes.SINGLETON_EAGER);

	container.registerProvider(SASLManager.class, new Provider<SASLManager>() {
	    public SASLManager get() {
		return new SASLManager(container.getInstance(Emite.class));
	    }
	}, Scopes.SINGLETON_EAGER);

	container.registerProvider(SessionManager.class, new Provider<SessionManager>() {
	    public SessionManager get() {
		final SessionManager sessionManager = new SessionManager(container.getInstance(Emite.class));
		return sessionManager;
	    }
	}, Scopes.SINGLETON_EAGER);

	// FIXME: circular references
	container.registerProvider(Session.class, new Provider<Session>() {
	    public Session get() {
		final SessionManager manager = container.getInstance(SessionManager.class);
		final Session session = new Session(manager);
		manager.setSession(session);
		return session;
	    }
	}, Scopes.SINGLETON_EAGER);

    }

}
