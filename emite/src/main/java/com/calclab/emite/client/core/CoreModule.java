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
package com.calclab.emite.client.core;

import com.calclab.emite.client.components.Container;
import com.calclab.emite.client.core.bosh.BoshManager;
import com.calclab.emite.client.core.bosh.BoshOptions;
import com.calclab.emite.client.core.bosh.Bosh;
import com.calclab.emite.client.core.bosh.BoshStream;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.bosh.EmiteBosh;
import com.calclab.emite.client.core.bosh.Stream;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.DispatcherDefault;
import com.calclab.emite.client.core.services.Services;
import com.calclab.emite.client.core.services.ServicesAbstractModule;

public class CoreModule {
    public static final String COMPONENT_BOSH = "bosh:manager";
    public static final String COMPONENT_DISPATCHER = "dispatcher";
    public static final String COMPONENT_EMITE = "emite";

    public static Dispatcher getDispatcher(final Container container) {
	return (Dispatcher) container.get(COMPONENT_DISPATCHER);
    }

    public static Emite getEmite(final Container container) {
	return (Emite) container.get(COMPONENT_EMITE);
    }

    public static void load(final Container container, final BoshOptions options) {
	// dependencies
	final Services services = ServicesAbstractModule.getServices(container);

	// injections
	final DispatcherDefault dispatcher = new DispatcherDefault();
	container.register(COMPONENT_DISPATCHER, dispatcher);

	final Stream stream = new BoshStream();
	final EmiteBosh emite = new EmiteBosh(dispatcher, stream);
	container.register(COMPONENT_EMITE, emite);

	final Bosh bosh = new Bosh(stream, options);
	final BoshManager boshManager = new BoshManager(services, emite, bosh);
	container.register(COMPONENT_BOSH, boshManager);

    }

}
