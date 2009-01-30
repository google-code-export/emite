/*
 *
 * ((e)) emite: A pure gwt (Google Web Toolkit) xmpp (jabber) library
 *
 * (c) 2008-2009 The emite development team (see CREDITS for details)
 * This file is part of emite.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.calclab.examples.emite.echo.client;

import com.calclab.emite.core.client.xmpp.session.Session;
import com.calclab.emite.core.client.xmpp.session.SessionComponent;
import com.calclab.suco.client.Suco;
import com.calclab.suco.client.ioc.module.AbstractModule;
import com.calclab.suco.client.ioc.module.Factory;
import com.google.gwt.core.client.EntryPoint;

public class EchoModule extends AbstractModule implements EntryPoint {
    /**
     * Called by GWT when this module is loaded in a browser
     */
    public void onModuleLoad() {
	// install this module in suco
	Suco.install(this);
    }

    /**
     * Called by Suco when this module is installed in Suco
     */
    @Override
    protected void onInstall() {
	// The SessionComponent decorator take care of calling the factories
	// when a Session is created (see Suco for more info)
	register(SessionComponent.class, new Factory<Echo>(Echo.class) {
	    @Override
	    public Echo create() {
		return new Echo($(Session.class));
	    }
	});
    }
}
