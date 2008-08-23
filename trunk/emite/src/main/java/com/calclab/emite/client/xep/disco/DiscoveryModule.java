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
package com.calclab.emite.client.xep.disco;

import com.calclab.emite.client.xmpp.session.Session;
import com.calclab.emite.client.xmpp.session.SessionScope;
import com.calclab.suco.client.module.AbstractModule;
import com.calclab.suco.client.provider.Factory;

/**
 * Implements XEP-0030: Service Discovery
 * 
 * This specification defines an XMPP protocol extension for discovering
 * information about other XMPP entities
 * 
 * @see http://www.xmpp.org/extensions/xep-0030.html
 * 
 *      NOT IMPLEMENTED
 * 
 */
public class DiscoveryModule extends AbstractModule {

    public DiscoveryModule() {
	super();
    }

    @Override
    public void onLoad() {
	register(SessionScope.class, new Factory<DiscoveryManager>(DiscoveryManager.class) {
	    public DiscoveryManager create() {
		return new DiscoveryManager($(Session.class));
	    }
	});
    }
}
