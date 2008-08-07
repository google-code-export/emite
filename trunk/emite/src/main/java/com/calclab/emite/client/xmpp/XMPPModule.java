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

import com.calclab.emite.client.core.bosh.Connection;
import com.calclab.emite.client.xmpp.resource.ResourceBindingManager;
import com.calclab.emite.client.xmpp.sasl.SASLManager;
import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.session.SessionScope;
import com.calclab.emite.client.xmpp.session.XmppSession;
import com.calclab.suco.client.modules.AbstractModule;
import com.calclab.suco.client.provider.Factory;
import com.calclab.suco.client.scopes.SingletonScope;

public class XMPPModule extends AbstractModule {

    public XMPPModule() {
	super();
    }

    @Override
    public void onLoad() {

	registerScope(SessionScope.class, new SessionScope());

	register(SingletonScope.class, new Factory<ResourceBindingManager>(ResourceBindingManager.class) {
	    public ResourceBindingManager create() {
		return new ResourceBindingManager($(Connection.class));
	    }
	}, new Factory<SASLManager>(SASLManager.class) {
	    public SASLManager create() {
		return new SASLManager($(Connection.class));
	    }
	}, new Factory<Session>(Session.class) {
	    public Session create() {
		final XmppSession session = new XmppSession($(Connection.class), $(SessionScope.class),
			$(SASLManager.class), $(ResourceBindingManager.class));
		$(SessionScope.class).setContext(session);
		return session;
	    }
	});

	$(Session.class);
    }
}
