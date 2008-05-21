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
import com.calclab.emite.client.modular.Container;
import com.calclab.emite.client.modular.Module;
import com.calclab.emite.client.modular.Provider;
import com.calclab.emite.client.modular.Scopes;

public class CoreModule implements Module {
    public static Bosh getBosh(final Container container) {
	return container.getInstance(Bosh.class);
    }

    public static Dispatcher getDispatcher(final Container container) {
	return container.getInstance(Dispatcher.class);
    }

    public static Emite getEmite(final Container container) {
	return container.getInstance(Emite.class);
    }

    public Class<? extends Module> getType() {
	return CoreModule.class;
    }

    public void onLoad(final Container container) {
	container.registerProvider(Dispatcher.class, new Provider<Dispatcher>() {
	    public Dispatcher get() {
		return new DispatcherDefault();
	    }
	}, Scopes.SINGLETON_EAGER);

	container.registerProvider(Stream.class, new Provider<Stream>() {
	    public Stream get() {
		return new Stream();
	    }
	}, Scopes.SINGLETON_EAGER);

	container.registerProvider(Emite.class, new Provider<Emite>() {
	    public Emite get() {
		return new EmiteBosh(container.getInstance(Dispatcher.class), container.getInstance(Stream.class));
	    }
	}, Scopes.SINGLETON_EAGER);

	container.registerProvider(Bosh.class, new Provider<Bosh>() {
	    public Bosh get() {
		return new Bosh(container.getInstance(Stream.class));
	    }
	}, Scopes.SINGLETON_EAGER);

	container.registerProvider(BoshManager.class, new Provider<BoshManager>() {
	    public BoshManager get() {
		final Services services = container.getInstance(Services.class);
		final Emite emite = container.getInstance(Emite.class);
		final Bosh bosh = container.getInstance(Bosh.class);
		return new BoshManager(services, emite, bosh);
	    }
	}, Scopes.SINGLETON_EAGER);

    }

}
