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

import com.calclab.emite.client.core.bosh.BoshManager;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.xmpp.resource.ResourceBindingManager;
import com.calclab.emite.client.xmpp.sasl.SASLManager;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.session.SessionManager;
import com.calclab.emite.client.xmpp.session.SessionScope;
import com.calclab.modular.client.container.Container;
import com.calclab.modular.client.container.Provider;
import com.calclab.modular.client.modules.Module;
import com.calclab.modular.client.modules.ModuleBuilder;
import com.calclab.modular.client.scopes.Scopes;
import com.calclab.modular.client.scopes.SingletonScope;

public class XMPPModule implements Module {

    public static final Class<SessionManager> COMPONENT_SESSION_MANAGER = SessionManager.class;
    private static final Class<Session> COMPONENT_SESSION = Session.class;

    public static Session getSession(final Container container) {
	return container.getInstance(XMPPModule.COMPONENT_SESSION);
    }

    public Class<? extends Module> getType() {
	return XMPPModule.class;
    }

    public void onLoad(final ModuleBuilder builder) {

	Scopes.addScope(SessionScope.class, new SessionScope());
	builder
		.registerProvider(SessionScope.class, Scopes.get().getProvider(SessionScope.class),
			SingletonScope.class);

	builder.registerProvider(ResourceBindingManager.class, new Provider<ResourceBindingManager>() {
	    public ResourceBindingManager get() {
		return new ResourceBindingManager(builder.getInstance(Emite.class));
	    }
	}, SingletonScope.class);

	builder.registerProvider(SASLManager.class, new Provider<SASLManager>() {
	    public SASLManager get() {
		return new SASLManager(builder.getInstance(Emite.class));
	    }
	}, SingletonScope.class);

	builder.registerProvider(Session.class, new Provider<Session>() {
	    public Session get() {
		final Emite emite = builder.getInstance(Emite.class);
		final BoshManager boshManager = builder.getInstance(BoshManager.class);
		final SessionScope scope = builder.getInstance(SessionScope.class);
		final Session session = new Session(boshManager, emite, scope);
		scope.setContext(session);
		return session;
	    }
	}, SingletonScope.class);

	builder.registerProvider(SessionManager.class, new Provider<SessionManager>() {
	    public SessionManager get() {
		final Emite emite = builder.getInstance(Emite.class);
		final Session session = builder.getInstance(Session.class);
		final SASLManager saslManager = builder.getInstance(SASLManager.class);
		final ResourceBindingManager bindingManager = builder.getInstance(ResourceBindingManager.class);
		final SessionManager sessionManager = new SessionManager(session, emite, saslManager, bindingManager);
		return sessionManager;
	    }
	}, SessionScope.class);

    }
}
