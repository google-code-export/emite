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

import com.calclab.emite.client.core.bosh.Bosh;
import com.calclab.emite.client.core.bosh.BoshManager;
import com.calclab.emite.client.core.bosh.Emite;
import com.calclab.emite.client.core.bosh.EmiteBosh;
import com.calclab.emite.client.core.bosh.Stream;
import com.calclab.emite.client.core.dispatcher.Dispatcher;
import com.calclab.emite.client.core.dispatcher.DispatcherDefault;
import com.calclab.emite.client.core.services.Services;
import com.calclab.emite.client.core.services.ServicesAbstractModule;
import com.calclab.emite.client.modular.Container;
import com.calclab.emite.client.modular.Module;

public class CoreModule implements Module {
    public static Bosh getBosh(final Container container) {
	return container.get(Bosh.class);
    }

    public static Dispatcher getDispatcher(final Container container) {
	return container.get(Dispatcher.class);
    }

    public static Emite getEmite(final Container container) {
	return container.get(Emite.class);
    }

    public void load(final Container container) {
	final Services services = ServicesAbstractModule.getServices(container);

	final Dispatcher dispatcher = container.register(Dispatcher.class, new DispatcherDefault());
	final Stream stream = new Stream();
	final Emite emite = container.register(Emite.class, new EmiteBosh(dispatcher, stream));
	final Bosh bosh = container.register(Bosh.class, new Bosh(stream));
	container.register(BoshManager.class, new BoshManager(services, emite, bosh));
    }

}
