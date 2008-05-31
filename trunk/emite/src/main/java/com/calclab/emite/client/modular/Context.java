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
package com.calclab.emite.client.modular;

import java.util.ArrayList;
import java.util.List;

public class Context<C> {
    private final Class<?> contextType;

    public Context(final Class<?> contextType) {
	this.contextType = contextType;
    }

    @SuppressWarnings("unchecked")
    public List<C> getInstances(final Container container) {
	final List<Provider<?>> providers = getContextProviders(container);
	final ArrayList<C> instances = new ArrayList<C>();
	for (final Provider<?> provider : providers) {
	    instances.add((C) provider.get());
	}
	return instances;
    }

    public void register(final Container container, final Provider<?> provider) {
	getContextProviders(container).add(provider);
    }

    private List<Provider<?>> getContextProviders(final Container container) {
	final ContextRegistry registry = getRegistry(container);
	final List<Provider<?>> providers = registry.getProviders(contextType);
	return providers;
    }

    private ContextRegistry getRegistry(final Container container) {
	if (!container.hasProvider(ContextRegistry.class)) {
	    container.registerProvider(ContextRegistry.class, new Provider<ContextRegistry>() {
		final ContextRegistry registry = new ContextRegistry();

		public ContextRegistry get() {
		    return registry;
		}
	    });
	}
	return container.getInstance(ContextRegistry.class);
    }
}
